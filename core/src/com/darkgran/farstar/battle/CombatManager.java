package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.Player;

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

    public void processDrop(Token token, DropTarget dropTarget, int position) {
        if (token instanceof FleetToken) {
            ((FleetToken) token).resetPosition();
        }
        if (active && !duelManager.isActive()) {
            Player playerA = new Player();
            if (token instanceof FleetToken) {
                playerA = ((FleetToken) token).getFleetMenu().getPlayer();
            }
            Player playerD = new Player();
            if (dropTarget instanceof FleetMenu) {
                playerD = ((FleetMenu) dropTarget).getPlayer();
            } else if (dropTarget instanceof MothershipToken) {
                playerD = ((MothershipToken) dropTarget).getPlayer();
            }
            if (playerA.getBattleID() != -1 && playerD.getBattleID() != -1) {
                //TODO duelManager.launchDuel(token, null, playerA, playerD);
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
