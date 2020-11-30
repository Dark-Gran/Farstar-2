package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.FleetMenu;
import com.darkgran.farstar.battle.gui.FleetToken;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.Fleet;

public class CombatManager {
    private final Battle battle;
    private final DuelManager duelManager;
    private boolean active = false;

    public CombatManager(Battle battle, DuelManager duelManager) {
        this.battle = battle;
        this.duelManager = duelManager;
    }

    public void launchCombat()
    {
        active = true;
        System.out.println("Combat Phase started.");
        if (battle.getWhoseTurn().getFleet().noAttackers()) {
            endCombat();
        }
    }

    public void combatTarget(Token token, Fleet fleet, int position) {
        if (active && !duelManager.isActive() && token instanceof FleetToken) {
            FleetToken fleetToken = (FleetToken) token;
            FleetMenu fleetMenu = fleetToken.getFleetMenu();
            fleetToken.resetPosition();
            if (fleet.getFleetMenu() != fleetMenu) {

            }

        }
    }

    public void endCombat() {
        active = false;
        System.out.println("Combat Phase ended.");
        battle.getRoundManager().afterCombat();
    }

    public boolean isActive() { return active; }

    public Battle getBattle() { return battle; }

    public DuelManager getDuelManager() { return duelManager; }

}
