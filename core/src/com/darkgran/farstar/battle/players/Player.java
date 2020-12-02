package com.darkgran.farstar.battle.players;

public class Player {
    private final byte battleID;
    private int energy;
    private int matter;
    private Mothership ms; //MotherShip
    private final Deck deck;
    private final Shipyard shipyard;
    private final Hand hand = new Hand();
    private final Fleet fleet;

    public Player(byte battleID, int energy, int matter, Mothership ms, Deck deck, Shipyard shipyard) {
        this.battleID = battleID;
        setEnergy(energy);
        setMatter(matter);
        this.ms = ms;
        this.deck = deck;
        this.shipyard = shipyard;
        fleet = new Fleet();
    }

    public Player() {
        this.battleID = (byte) -1;
        setEnergy(-1);
        setMatter(-1);
        this.ms = null;
        this.deck = null;
        this.shipyard = null;
        fleet = null;
    }

    public boolean canAfford(Card card) {
        return (energy>=card.getCardInfo().getEnergy() && matter>=card.getCardInfo().getMatter());
    }

    public void payday(Card card) {
        setEnergy(energy-card.getCardInfo().getEnergy());
        setMatter(matter-card.getCardInfo().getMatter());
    }

    public void setEnergy(int energy) { this.energy = energy; }

    public void setMatter(int matter) { this.matter = matter; }

    public void addEnergy(int energy) { this.energy += energy; }

    public void addMatter(int matter) { this.matter += matter; }

    public int getEnergy() { return energy; }

    public int getMatter() { return matter; }

    public int getBattleID() { return battleID; }

    public Mothership getMs() { return ms; }

    public Hand getHand() { return hand; }

    public Deck getDeck() { return deck; }

    public Shipyard getShipyard() { return shipyard; }

    public Fleet getFleet() { return fleet; }

}
