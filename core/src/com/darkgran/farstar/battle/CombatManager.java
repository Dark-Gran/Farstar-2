package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.FleetMenu;
import com.darkgran.farstar.battle.gui.FleetToken;
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
        System.out.println("Combat Phase started.");
    }

    public void combatTarget(Token token, Fleet fleet, int position) {
        if (active && !inDuel && token instanceof FleetToken) {
            FleetToken fleetToken = (FleetToken) token;
            FleetMenu fleetMenu = fleetToken.getFleetMenu();

            if (fleet.getFleetMenu() != fleetMenu) { //TODO

            }

            fleetToken.resetPosition();
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

    public boolean isInDuel() { return inDuel; }

}
