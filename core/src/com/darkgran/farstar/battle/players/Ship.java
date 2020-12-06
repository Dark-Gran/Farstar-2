package com.darkgran.farstar.battle.players;

public class Ship extends TokenizedCard {
    private boolean fought = false;
    private final Fleet fleet;

    public Ship(Fleet fleet, CardInfo cardInfo) {
        super(cardInfo);
        this.fleet = fleet;
    }

    public Ship(Fleet fleet, int id) {
        super(id);
        this.fleet = fleet;
    }

    @Override
    public void death() { //TODO end in junkpile (only if source is HAND?)
        if (fleet != null) { fleet.removeShip(this); }
        if (getToken() != null) { getToken().destroy(); }
    }

    public boolean haveFought() { return fought; }

    public void setFought(boolean fought) { this.fought = fought; }

    public Fleet getFleet() { return fleet; }

}
