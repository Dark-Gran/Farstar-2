package com.darkgran.farstar.battle;

public class Player {
    private final byte id;
    private int energy;

    public Player(byte id, int energy) {
        this.id = id;
        setEnergy(energy);
    }

    public void setEnergy(int energy) { this.energy = energy; }

    public int getEnergy() { return energy; }

    public int getId() { return id; }
}
