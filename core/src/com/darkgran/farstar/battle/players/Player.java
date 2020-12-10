package com.darkgran.farstar.battle.players;

public class Player {
    private final byte battleID; //must be unique for each Player in match
    private int energy;
    private int matter;
    private Mothership ms; //MotherShip
    private final Deck deck;
    private final Yard yard;
    private final Hand hand = new Hand();
    private final Fleet fleet;
    private final Junkpile junkpile;

    public Player(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard) {
        this.battleID = battleID;
        setEnergy(energy);
        setMatter(matter);
        this.ms = ms;
        this.deck = deck;
        this.yard = yard;
        junkpile = new Junkpile();
        fleet = new Fleet(junkpile);

    }

    public Player() { //must be set-up if constructed this way
        this.battleID = (byte) -1;
        setEnergy(-1);
        setMatter(-1);
        this.ms = null;
        this.deck = null;
        this.yard = null;
        fleet = null;
        junkpile = null;
    }

    public boolean canAfford(Card card) {
        return (energy>=card.getCardInfo().getEnergy() && matter>=card.getCardInfo().getMatter());
    }

    public boolean canAfford(int energy, int matter) {
        return (this.energy>=energy && this.matter>=matter);
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

    public Yard getYard() { return yard; }

    public Mothership getMs() { return ms; }

    public Hand getHand() { return hand; }

    public Deck getDeck() { return deck; }

    public Yard getShipyard() { return yard; }

    public Fleet getFleet() { return fleet; }

    public Junkpile getJunkpile() { return junkpile; }


}
