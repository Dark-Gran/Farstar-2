package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.Fleet;

public class CombatManager {
    private final Battle battle;
    private boolean active = false;
    private boolean inDuel = false;

    public CombatManager(Battle battle) {
        this.battle = battle;
    }

    public void launchCombat()
    {
        active = true;
    }

    public void combatTarget(Token token, Fleet fleet, int position) { //TODO
        if (active && !inDuel && token.getTokenMenu() != null) {

        }
    }

    public void duel() {
        inDuel = true;
    }

    public void duelEnd() {
        inDuel = false;
    }

    public void endCombat() {
        active = false;
        battle.getRoundManager().afterCombat();
    }

    public boolean isActive() { return active; }

    public Battle getBattle() { return battle; }

}
