package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.Player;

public class Ship extends JunkableCard {
    private boolean fought = false;
    private final Fleet fleet;

    public Ship(Fleet fleet, CardInfo cardInfo, Player player) {
        super(cardInfo, player);
        this.fleet = fleet;
    }

    @Override
    public void death() {
        super.death();
        if (fleet != null) { fleet.removeShip(this); }
    }

    public boolean haveFought() { return fought; }

    public void setFought(boolean fought) { this.fought = fought; }

    public Fleet getFleet() { return fleet; }

}
