package com.darkgran.farstar.battle.players;

public class Ship extends Card {
    private boolean fought = false;

    public Ship(CardInfo cardInfo) { super(cardInfo); }

    public Ship() { }

    public Ship(int id) { super(id); }

    @Override
    public void death() {
        //TODO
    }

    public boolean haveFought() { return fought; }

    public void setFought(boolean fought) { this.fought = fought; }

}
