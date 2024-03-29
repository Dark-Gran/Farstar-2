package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.cards.CardInfo;

public class Ship extends JunkableBattleCard {
    private final Fleet fleet;
    private int dmgDoneThisBattle = 0;
    private boolean dead = false;

    public Ship(Fleet fleet, CardInfo cardInfo, CardInfo originalInfo, BattlePlayer battlePlayer) {
        super(cardInfo, originalInfo, battlePlayer);
        this.fleet = fleet;
    }

    public Ship(Fleet fleet, CardInfo cardInfo, BattlePlayer battlePlayer) {
        super(cardInfo, cardInfo, battlePlayer);
        this.fleet = fleet;
    }

    public void deathInAfterMath() {
        dead = true;
        super.death();
        if (fleet != null) { fleet.removeShip(this, true); }
    }

    @Override
    public void death() {
        dead = true;
        super.death();
        if (fleet != null) { fleet.removeShip(this, false); }
    }

    public Fleet getFleet() { return fleet; }

    public int getDmgDoneThisBattle() {
        return dmgDoneThisBattle;
    }

    public void setDmgDoneThisBattle(int dmgDoneThisBattle) {
        this.dmgDoneThisBattle = dmgDoneThisBattle;
    }

    public boolean isDead() {
        return dead;
    }

}
