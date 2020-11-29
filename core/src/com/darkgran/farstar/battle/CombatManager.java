package com.darkgran.farstar.battle;

public class CombatManager {
    private final Battle battle;
    private boolean active = false;

    public CombatManager(Battle battle) {
        this.battle = battle;
    }

    public void launchCombat() { //TODO
        active = true;
    }

    public void duel() {

    }

    public void endCombat() {
        active = false;
        battle.getRoundManager().afterCombat();
    }

    public boolean isActive() { return active; }

    public Battle getBattle() { return battle; }

}
