package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.cards.CardInfo;

public class Ship extends JunkableBattleCard {
    private final Fleet fleet;

    public Ship(Fleet fleet, CardInfo cardInfo, BattlePlayer battlePlayer) {
        super(cardInfo, battlePlayer);
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
