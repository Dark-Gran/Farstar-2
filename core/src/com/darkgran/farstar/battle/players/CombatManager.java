package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;

public class CombatManager {
    private final Battle battle;
    private boolean active = false;

    public CombatManager(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() { return battle; }

    public void launchCombat() {
        active = true;

        endCombat();
    }

    public void endCombat() {
        active = false;
        battle.getRoundManager().afterCombat();
    }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
