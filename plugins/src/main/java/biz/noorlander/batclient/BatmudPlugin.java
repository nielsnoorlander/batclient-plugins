package biz.noorlander.batclient;

import biz.noorlander.batclient.handlers.AbstractHandler;
import com.mythicscape.batclient.interfaces.*;

import java.util.Objects;

public abstract class BatmudPlugin<T extends AbstractHandler> extends BatClientPlugin
        implements BatClientPluginCommandTrigger, BatClientPluginTrigger, BatClientPluginUtil {
    protected T handler;
    private boolean enabled;
    private String name;

    public BatmudPlugin() {
    }

    public abstract T createHandler();

    @Override
    public ParsedResult trigger(ParsedResult incoming) {
        return enabled ? handler.handleOutputTriggers(incoming) : null;
    }

    @Override
    public String trigger(String command) {
        return enabled ? handler.handleCommandTriggers(command) : null;
    }

    @Override
    public void loadPlugin() {
        this.handler = createHandler();
        this.enabled = true;
        this.name = this.getClass().getSimpleName();
        PluginRepository.getInstance().registerPlugin(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void clientExit() {
        this.handler.destroyHandler();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatmudPlugin<?> that = (BatmudPlugin<?>) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public void displayInfo() {
        handler.displayPluginInfo();
    }
}
