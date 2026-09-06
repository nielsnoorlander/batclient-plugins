# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A multi-module Maven build for **batclient**, a Java/Swing MUD client for BatMUD (by Mythicscape). It has two modules:

- `plugins/` — the actual client plugins: hook into the client's trigger pipeline to parse MUD output, colorize/gag text, track combat/ticker state, and render auxiliary Swing windows (health bars, player stats, etc). This is what gets deployed to batclient.
- `ui-components/` — a small standalone Swing component library (`biz.noorlander.batclient.ui.components`), consumed by `plugins/` as a reactor dependency. Currently has one component (`AnimistSoulInfo`). See "Multi-module layout" below for how it got here — the existing `plugins/.../ui/` classes (`AnimistSoulFrame`, `PlayerStatsFrame`, `BatGauge`) are pre-existing duplicates *not yet* migrated to consume it.

## Build & test

`mvn` is not on PATH in this environment — use the Maven Wrapper instead (`./mvnw`), which downloads and pins Maven 3.9.9 on first run. Run it from the repo root; it builds the whole reactor (both modules):

```
./mvnw package                                        # builds ui-components-1.0-SNAPSHOT.jar, plugins-1.0-SNAPSHOT.jar, and a shaded plugins-1.0-SNAPSHOT-full.jar (bundles gson, guava, ui-components, etc.)
./mvnw test                                           # runs all *Test classes across the reactor (surefire only picks up classes matching *Test)
./mvnw test -pl plugins -Dtest=NPCHealthPluginTest              # single test class, scoped to the plugins module
./mvnw test -pl plugins -Dtest=NPCHealthPluginTest#shapePatternTest   # single test method
```

Java source/target is **1.8** (set once on the parent POM, inherited by both modules). Tests use JUnit 5 (Jupiter) + Mockito 2.

### The `bat_interfaces` dependency

The client's plugin API (`com.mythicscape.batclient.interfaces.*`) isn't on Maven Central — it's an Ant-built jar vendored into `plugins/repo/`, which is a committed local Maven repository (declared in `plugins/pom.xml` as `<repository><url>file://${basedir}/repo</url>`, so it's scoped to that module only). That means `plugins/repo/**/*.jar` and `*.pom` are real, intentionally-committed binary artifacts, not build output — don't treat modifications/staleness under there as something to clean up. To update the vendored interfaces jar:

```
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file \
  -Dfile=bat_interfaces.jar -DgroupId=bat -DartifactId=bat_interfaces \
  -Dversion=<N> -Dpackaging=jar -DlocalRepositoryPath=plugins/repo/
```

The project currently depends on `bat_interfaces` version `2`.

## Multi-module layout

The root `pom.xml` is a parent POM (`packaging=pom`) aggregating `ui-components` and `plugins`, and centralizes groupId/version/Java-8 compiler config so both modules stay in sync. `plugins` depends on `ui-components` via `${project.version}` (a reactor dependency) rather than an externally-installed artifact.

`ui-components/` was brought in from a separate GitHub repo (`batclient-ui-library`) via `git subtree add --prefix=ui-components`, which preserves that repo's own commit history as real ancestors of this branch (visible in `git log`). **This means the branch history isn't safe to rebase** — a plain `git rebase` replays those foreign commits individually at their original (unprefixed) paths, corrupting the tree and dropping the subtree merge that actually placed them under `ui-components/`. If you need to bring another branch's commits into a branch containing this history, merge it in rather than rebasing.

## Commit messages

Use Conventional Commits, regardless of this repo's prior history:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

Types: `feat`, `fix`, `refactor`, `perf`, `test`, `docs`, `style`, `chore`, `build`, `ci`, `ops`, `revert`.

Rules:
- Subject line ≤50 chars, capitalized, no trailing period.
- Imperative mood in the subject ("Add", "Fix", "Refactor" — not "Added"/"Fixes").
- Separate the subject from the body with a blank line.
- Wrap the body at ~72 chars.
- Use the body to explain *what* and *why* — not *how* (the diff shows that).

## Architecture (`plugins` module)

### `BatmudPlugin<T>` → Handler → (Config / Events / Timers / PluginRepository) layering

Every feature's `*Plugin` class (in `plugins/`) extends the generic `BatmudPlugin<T extends AbstractHandler>` (package `biz.noorlander.batclient`), which implements the `bat_interfaces` lifecycle generically and delegates everything to a Handler:

- `loadPlugin()` — calls the plugin's `createHandler()`, marks it enabled, derives its name from the simple class name, and self-registers into the `PluginRepository` singleton.
- `trigger(ParsedResult)` / `trigger(String command)` — delegate to `handler.handleOutputTriggers(...)` / `handler.handleCommandTriggers(...)` when the plugin is enabled, else no-op.
- `clientExit()` — delegates to `handler.destroyHandler()`.

Because of this, concrete `*Plugin` classes are now near-empty — typically just `extends BatmudPlugin<XHandler>` implementing `createHandler()` (see `AnimistPlugin`, `NPCHealthPlugin` for the pattern). All the actual regex-matching/business logic lives in the corresponding **Handler** (`handlers/`):

- `AbstractHandler` wraps a `ClientGUI` reference and declares the two triggers as abstract (`handleOutputTriggers(ParsedResult)`, `handleCommandTriggers(String)`), plus exposes `command()`, `reportToGui()`, `createWindow()`, `getBaseDir()`, and `displayPluginInfo()` (used by plugin management, below).
- `AbstractWindowedHandler` extends it for handlers that own a persistent `BatWindow`: it loads/saves window position & size as a `WindowsConfig` JSON file via `ConfigService`.
- Concrete handlers (`AnimistHandler`, `CombatRoundHandler`, `PlayerStatsHandler`, `TargetHandler`, `TickerHandler`, `MonkSpecialSkillHandler`, `CommandQueueHandler`, `NPCHealthHandler`) hold the feature-specific state, regex patterns, and Swing views.

### Plugin registry & management

`PluginRepository` (singleton, package `biz.noorlander.batclient`) collects every `BatmudPlugin` as it self-registers on load. `PluginManagementPlugin`/`PluginManagementHandler` is a built-in plugin that adds an in-client `plugin` command (`plugin list`, `plugin help`) which echoes the registry — which plugins are loaded and which are currently enabled.

### Config persistence

`ConfigService` (singleton) serializes/deserializes config objects to/from JSON with Gson, stored under `<baseDir>/conf/`. Config classes extend `AbstractConfig`, which derives the on-disk filename from `getPrefix() + configName + ".config"`. `WindowsConfig` (window geometry) and `CustomConfig` (feature-specific settings, e.g. `PLAYER_STATS`) are the two config kinds in use.

### Cross-plugin events

`EventServiceManager` (singleton) hands out generic pub/sub `EventService<T>` instances (backed by `EventServiceImpl`) for two event types: `ActionEvent` and `CombatEvent` (in `services/events/`). Handlers subscribe via `EventListener<T>` to react to state changes raised by other handlers/plugins without direct coupling — e.g. combat state raised by one plugin can be observed by another.

### Timers

`timers/` (`CombatTimerTask`, `TickerTimerTask`) drive periodic UI updates (e.g. combat round countdowns, ticker resets) independent of incoming MUD text.

### UI

Swing components in `ui/` (`AnimistSoulFrame`, `PlayerStatsFrame`, `BatGauge`) are embedded into a `BatWindow` (obtained from `ClientGUI`/`AbstractHandler.createWindow`) rather than shown as standalone frames. These predate the `ui-components` module and haven't been migrated to it yet (see "What this is" above).

### Text parsing utilities

- `CommonPatterns` — shared regex fragments (e.g. `PATTERN_NPC_NAME`) reused across handler trigger patterns.
- `ParsedResultUtil` — helpers for gagging/transforming a `ParsedResult`.
- `AttributedMessageBuilder` — builds styled/colored `ParsedResult` text; `append(String, List<Attribute>)` takes arbitrary `Attribute`s (`Attribute.fgColor(Color)`, `.bgColor(Color)`, or `.build(TextAttribute, Object)` for anything else), not just a foreground/background color pair.

When adding a new plugin, follow the existing pattern: a thin `*Plugin` class extending `BatmudPlugin<T>` for the `createHandler()` wiring, with real logic in a `*Handler`, and tests (where present) matching only the regex/parsing logic (see `AnimistHandlerTest`, `MonkSpecialSkillPluginTest`, `NPCHealthPluginTest`) rather than exercising Swing/`ClientGUI`.
