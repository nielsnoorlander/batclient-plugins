package biz.noorlander.batclient.model;

import java.util.Optional;

public class PlayerScore {
    String name;
    String lastName;
    String race;
    int level = 0;
    int ascensionLevel = 0;
    long experience;
    float money;
    float bank;
    int roomsExplored;
    int totalRooms;
    int specialExplored;
    int taskPoints;
    int taskPointsSpent;
    Attribute hitPoints = new Attribute("HP");
    Attribute spellPoints = new Attribute("SP");
    Attribute endurancePoints = new Attribute("EP");
    Attribute strength = new Attribute("strength");
    int baseStrength;

    public static class Attribute {
        private String name;
        private Integer value = null;
        private Integer base = null;
        private Integer max = null;;
        Attribute(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Integer getBase() {
            return base;
        }

        public void setBase(Integer base) {
            this.base = base;
        }

        public Optional<Integer> getMax() {
            return Optional.ofNullable(max);
        }

        public void setMax(Integer max) {
            this.max = max;
        }
    }
}
