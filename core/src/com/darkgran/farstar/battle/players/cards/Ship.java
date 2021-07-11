package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.Player;

public class Ship extends JunkableCard {
    private final Fleet fleet;

    public Ship(Fleet fleet, CardInfo cardInfo, Player player) {
        super(cardInfo, player);
        this.fleet = fleet;
    }

    public void deathInAfterMath() {
        super.death();
        if (fleet != null) { fleet.removeShip(this, true); }
    }

    @Override
    public void death() {
        super.death();
        if (fleet != null) { fleet.removeShip(this, false); }
    }

    public Fleet getFleet() { return fleet; }

}
