package biz.noorlander.batclient.model;

public class AnimalSoul {
    private String race;
    private boolean strength = false;
    private boolean dexterity = false;
    private boolean constitution = false;
    private boolean intelligence = false;
    private boolean wisdom = false;
    private boolean charisma = false;
    int rank;

    private AnimalSoul(String race) {
        this.race = race;
    }

    public static AnimalSoul create(String race) {
        return new AnimalSoul(race);
    }

    public AnimalSoul rank(int rank) {
        this.rank = rank;
        return this;
    }

    public AnimalSoul strength() {
        this.strength = true;
        return this;
    }

    public AnimalSoul dexterity() {
        this.dexterity = true;
        return this;
    }

    public AnimalSoul constitution() {
        this.constitution = true;
        return this;
    }

    public AnimalSoul intelligence() {
        this.intelligence = true;
        return this;
    }

    public AnimalSoul wisdom() {
        this.wisdom = true;
        return this;
    }

    public AnimalSoul charisma() {
        this.charisma = true;
        return this;
    }

    public String getRace() {
        return race;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        int attrCount = 0;
        attrCount += addAttribute("str", strength, sb, attrCount);
        attrCount += addAttribute("dex", dexterity, sb, attrCount);
        attrCount += addAttribute("con", constitution, sb, attrCount);
        attrCount += addAttribute("int", intelligence, sb, attrCount);
        attrCount += addAttribute("wis", wisdom, sb, attrCount);
        attrCount += addAttribute("cha", charisma, sb, attrCount);
        if (attrCount == 0) { sb.append("none"); }
        return sb.toString();
    }

    private int addAttribute(String attrName, boolean hasAttr, StringBuilder sb, int attrCount) {
        if (hasAttr) {
            if (attrCount > 0) { sb.append(", "); }
            sb.append(attrName);
            return 1;
        } else {
            return 0;
        }
    }
}
