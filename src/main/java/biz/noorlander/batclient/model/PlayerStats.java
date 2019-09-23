package biz.noorlander.batclient.model;

import java.util.Objects;

public class PlayerStats {
    String name;
    int strength = 0;
    int dexterity = 0;
    int constitution = 0;
    int intelligence = 0;
    int wisdom = 0;
    int charisma = 0;
    int size = 0;
    String alignment = "neutral";
    String age = "very young";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String sb = "PlayerStats{" + "name='" + name + '\'' +
                ", strength=" + strength +
                ", dexterity=" + dexterity +
                ", constitution=" + constitution +
                ", intelligence=" + intelligence +
                ", wisdom=" + wisdom +
                ", charisma=" + charisma +
                ", size=" + size +
                ", alignment='" + alignment + '\'' +
                ", age='" + age + '\'' +
                '}';
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStats that = (PlayerStats) o;
        return strength == that.strength &&
                dexterity == that.dexterity &&
                constitution == that.constitution &&
                intelligence == that.intelligence &&
                wisdom == that.wisdom &&
                charisma == that.charisma &&
                size == that.size &&
                name.equals(that.name) &&
                alignment.equals(that.alignment) &&
                age.equals(that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, strength, dexterity, constitution, intelligence, wisdom, charisma, size, alignment, age);
    }
}
