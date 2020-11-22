package com.darkgran.farstar.battle;

public class CardInfo {
    private final byte id;
    private final String name;
    private final CardSource source;
    private final int energy; //resource-price
    private final int matter; //resource-price
    private final int offense;
    private final int defense;

    public CardInfo(byte id, String name, CardSource source, int energy, int matter, int offense, int defense) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.energy = energy;
        this.matter = matter;
        this.offense = offense;
        this.defense = defense;
    }

    public CardInfo() {
        this.id = 0;
        this.name = "X";
        this.source = CardSource.HAND;
        this.energy = 0;
        this.matter = 0;
        this.offense = 0;
        this.defense = 0;
    }

    public byte getId() { return id; }

    public String getName() { return name; }

    public CardSource getSource() { return source; }

    public int getEnergy() { return energy; }

    public int getMatter() { return matter; }

    public int getOffense() { return offense; }

    public int getDefense() { return defense; }

}
