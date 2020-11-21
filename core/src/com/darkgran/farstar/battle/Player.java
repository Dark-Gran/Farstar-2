package com.darkgran.farstar.battle;

public class Player {
    private final byte battleID;
    private int energy;
    private int matter;

    public Player(byte battleID, int energy, int matter) {
        this.battleID = battleID;
        setEnergy(energy);
        setMatter(matter);
    }

    public void setEnergy(int energy) { this.energy = energy; }

    public int getEnergy() { return energy; }

    public void setMatter(int energy) { this.matter = energy; }

    public int getMatter() { return matter; }

    public int getBattleID() { return battleID; }
}
