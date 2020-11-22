package com.darkgran.farstar.battle;

public class Player {
    private final byte battleID;
    private int energy;
    private int matter;
    private Card ms; //MotherShip
    private Deck deck;
    private Shipyard shipyard;

    public Player(byte battleID, int energy, int matter, Card ms, Deck deck, Shipyard shipyard) {
        this.battleID = battleID;
        setEnergy(energy);
        setMatter(matter);
        this.ms = ms;
        this.deck = deck;
        this.shipyard = shipyard;
    }

    public void setEnergy(int energy) { this.energy = energy; }

    public void setMatter(int energy) { this.matter = energy; }

    public int getEnergy() { return energy; }

    public int getMatter() { return matter; }

    public int getBattleID() { return battleID; }

    public Deck getDeck() { return deck; }

    public Shipyard getShipyard() { return shipyard; }

}
