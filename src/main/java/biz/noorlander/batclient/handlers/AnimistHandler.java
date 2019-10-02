package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.AnimalSoul;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;
import com.mythicscape.batclient.interfaces.ClientGUI;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimistHandler extends AbstractHandler {
    public enum Quality { POOR, WEAK, AVERAGE, EXCELLENT, AWESOME }
    private static final Map<Quality,Color> QUALITY_COLOR_MAP = new HashMap<>();
    private List<AnimalSoul> soulDefinitions;
    private Pattern selectSoulPattern;
    private Pattern soulListPattern;
    private Pattern soulReportPattern;
    private Pattern soulListHeaderPattern;
    private Pattern selectCommandPattern;
    private Pattern soulScorePattern;

    private Map<String, Integer> soulStatistics;
    private Set<Soul> mySouls;
    private int soulHealthPercent = 100;

    private static class Soul {
        String race;
        int id;
        Quality quality;

        Soul(int id, String race, String quality) {
            this.id = id;
            this.race = race;
            this.quality = Quality.valueOf(quality.toUpperCase());
        }

        String getRace() {
            return race;
        }

        int getId() {
            return id;
        }

        Quality getQuality() {
            return quality;
        }

        int getPower() {
            return quality.ordinal();
        }
    }
    public AnimistHandler(ClientGUI gui) {
		super(gui, "ANIMIST");
		loadAnimalSouls();
		loadQualityColors();
		buildSoulPatterns();
		resetSoulsAndStatistics();
	}

    private void buildSoulPatterns() {
	    StringBuilder animalSoulPattern = new StringBuilder("(");
        String animals = soulDefinitions.stream().map(AnimalSoul::getRace).collect(Collectors.joining("|"));
        String quality = "(weak|poor|average|excellent|awesome)";
        animalSoulPattern.append(animals);
	    animalSoulPattern.append(")");
        selectSoulPattern = Pattern.compile("^You select [A-Z][a-z]+, the " + quality + " (" + animalSoulPattern.toString() + ")\\.$");
        soulListPattern = Pattern.compile("^([0-9]+)[ ]+([-][>] )?[A-Z][a-z]+[ ]+" + quality + "[ ]+(" + animalSoulPattern.toString() + ")[ ]*$");
        soulReportPattern = Pattern.compile("^Total of ([0-9]+) soul shown. Maximum you can control is ([0-9]+)\\.$");
        soulListHeaderPattern = Pattern.compile("^Id[ ]+Name[ ]+Power[ ]+Type$");
        selectCommandPattern = Pattern.compile("^as[ ]+(" + animals + ")[ ]+([A-z]+)[ ]*?"+quality+"?$");
        soulScorePattern = Pattern.compile("^Your soul companion: [a-z]+ [(]([0-9]+)[%][)] [+-]+$");
    }

    private void loadQualityColors() {
        QUALITY_COLOR_MAP.put(Quality.POOR, new Color(255, 0, 32));
        QUALITY_COLOR_MAP.put(Quality.WEAK, new Color(255, 67, 36));
        QUALITY_COLOR_MAP.put(Quality.AVERAGE, new Color(255, 165, 53));
        QUALITY_COLOR_MAP.put(Quality.EXCELLENT, new Color(54, 169, 19));
        QUALITY_COLOR_MAP.put(Quality.AWESOME, new Color(20, 253, 0));
    }
    public void resetSoulsAndStatistics() {
        soulStatistics = new HashMap<>();
        mySouls = new HashSet<>();
    }

    public Map<String, Integer> getSoulStatistics() {
        return soulStatistics;
    }

    public Matcher getSelectSoulMatcher(String text) {
	    return selectSoulPattern.matcher(text);
    }

    public Matcher getSoulListHeaderMatcher(String text) {
        return soulListHeaderPattern.matcher(text);
    }

    public Matcher getSoulReportMatcher(String text) {
        return soulReportPattern.matcher(text);
    }

    public Matcher getSoulListMatcher(String text) {
        return soulListPattern.matcher(text);
    }

    public Matcher getSoulScoreMatcher(String text) {
        return soulScorePattern.matcher(text);
    }

    public AnimalSoul getAnimistSoul(String race) {
	    return this.soulDefinitions.stream().filter(soulDefinition -> race.equalsIgnoreCase(soulDefinition.getRace())).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public int getSoulHealthPercent() {
        return soulHealthPercent;
    }

    public void setSoulHealthPercent(int soulHealthPercent) {
        this.soulHealthPercent = soulHealthPercent;
    }

    private void loadAnimalSouls() {
	    soulDefinitions = new ArrayList<>();
	    soulDefinitions.add(AnimalSoul.create("chicken").rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("squirrel").charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("sheep").charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("fox").intelligence().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("badger").strength().constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("antelope").dexterity().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("reindeer").dexterity().charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("goat").strength().wisdom().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("pig").strength().constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("jackal").dexterity().constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("giraffe").intelligence().charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("cow").constitution().wisdom().charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("wolf").strength().dexterity().constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("zebra").dexterity().constitution().charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("bull").strength().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("elk").strength().constitution().wisdom().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("tortoise").constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("unicorn").intelligence().wisdom().charisma().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("bear").strength().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("sabretooth").dexterity().intelligence().wisdom().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("lion").strength().wisdom().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("buffalo").strength().constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("leopard").dexterity().intelligence().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("polar bear").strength().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("rhinoceros").strength().dexterity().constitution().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("tiger").strength().intelligence().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("griffon").strength().dexterity().constitution().intelligence().wisdom().rank(soulDefinitions.size()));
        soulDefinitions.add(AnimalSoul.create("elephant").strength().intelligence().wisdom().rank(soulDefinitions.size()));
	}


	@Override
	public void destroyHandler() {
	}

    public void registerSoulStatistic(String race, String quality, int id) {
        if ( ! soulStatistics.containsKey(race)) {
            soulStatistics.put(race, 1);
        } else {
            soulStatistics.put(race, 1 + soulStatistics.get(race));
        }
        this.mySouls.add(new Soul(id, race, quality));
    }

    public void reportSoulStatistics(int current, int max) {
        final Map<String, Integer> sortedByCount = soulStatistics.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        sortedByCount.forEach( (race, count) -> {
            Set<Quality> distinctQualities = mySouls.stream().filter(soul -> race.equalsIgnoreCase(soul.getRace())).map(Soul::getQuality).collect(Collectors.toSet());
            AttributedMessageBuilder message = AttributedMessageBuilder.create()
                     .append(String.format("%-13s", race), Optional.of(Color.WHITE), Optional.empty())
                     .append(String.format("%2d   ", count), Optional.of(Color.CYAN), Optional.empty());
            Arrays.stream(Quality.values()).forEach(quality -> addQualityIndicator(quality, message, distinctQualities));
            message.append("     ").append(getAnimistSoul(race).toString());
            reportToGui(message.build());
         });
        reportToGui(AttributedMessageBuilder.create().append(System.lineSeparator() + "-----< Free: ")
                .append(String.format("%2d", max - current), Optional.of(Color.CYAN), Optional.empty())
                .append(" >---------------------<")
                .append(" Souls  ").append(String.format("%2d", current), Optional.of(Color.CYAN), Optional.empty())
                .append(" of ").append(String.format("%-2d ", max), Optional.of(Color.CYAN), Optional.empty())
                .append("  >---").build());
    }

    private void addQualityIndicator(Quality quality, AttributedMessageBuilder message, Set<Quality> distinctQualities) {
        if (distinctQualities.contains(quality)) {
            message.append(" " + quality.name().substring(0,1) + " ", Optional.of(Color.BLACK), Optional.of(QUALITY_COLOR_MAP.get(quality)));
        } else {
            message.append("   ");
        }
    }

    public String selectSoul(String command) {
        Matcher selectMatcher = selectCommandPattern.matcher(command);
        if (selectMatcher.find()) {
            String race = selectMatcher.group(1);
            String player = selectMatcher.group(2);
            String optionalQuality = selectMatcher.group(3);
            Stream<Soul> soulStream = mySouls.stream().filter(soul -> race.equalsIgnoreCase(soul.getRace()));
            if (selectMatcher.group(3) != null) {
                soulStream = soulStream.filter(soul -> Quality.valueOf(optionalQuality.toUpperCase()) == soul.getQuality());
            } else {
                soulStream = soulStream.sorted(Comparator.comparingInt(Soul::getPower));
            }
            Optional<Soul> match = soulStream.findFirst();
            if (match.isPresent()) {
                return "animist select " + match.get().getId() + ";;cast 'animal aspect' " + player
                        + ";;@party report Animal aspect (" + getAnimistSoul(race).toString() + ") -> " + player;
            } else {
                reportToGui(AttributedMessageBuilder.create().append("ERROR! ", Optional.of(Color.RED), Optional.empty())
                        .append(" Could not find race '" + race + "' with quality '" + optionalQuality + "'").build());
                return "";
            }
        } else {
            reportToGui(AttributedMessageBuilder.create().append("ERROR! ", Optional.of(Color.RED), Optional.empty())
            .append(" Usage: as <animal> <player> [<quality>]").build());
            return "";
        }
    }
}
