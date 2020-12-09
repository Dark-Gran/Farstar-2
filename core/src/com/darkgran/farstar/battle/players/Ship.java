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
    public void death() {
        if (getToken() != null) {
            getToken().addCardToJunk();
            getToken().destroy();
        }
        if (fleet != null) { fleet.removeShip(this); }
    }

    public boolean haveFought() { return fought; }

    public void setFought(boolean fought) { this.fought = fought; }

    public Fleet getFleet() { return fleet; }

}
