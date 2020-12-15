package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.MothershipToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.DuelPlayer;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.ai.Automaton;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.battle.players.abilities.EffectType;

public class CombatManager {
    private final Battle battle;
    private final DuelManager duelManager;
    private boolean active = false;
    private BattleStage battleStage; //must be set after ini - before RM.launch (see BattleScreen constructor)

    public CombatManager(Battle battle, DuelManager duelManager) {
        this.battle = battle;
        this.duelManager = duelManager;
    }

    public void launchCombat()
    {
        active = true;
        System.out.println("Combat Phase started.");
        fleetCheck();
    }

    private void fleetCheck() {
        if (!battle.isEverythingDisabled()) {
            if (battle.getWhoseTurn().getFleet().noAttackers()) {
                endCombat();
            } else if (battle.getWhoseTurn() instanceof Automaton) {
                ((Automaton) battle.getWhoseTurn()).newCombat();
            } else {
                battleStage.enableCombatEnd();
            }
        }
    }

    //in future: needs upgrade for other mods than 1v1 (use battle.getEnemies)
    public void processDrop(Token token, DropTarget dropTarget, Token targetToken) {
        if (token instanceof FleetToken) {
            ((FleetToken) token).resetPosition();
        }
        if (active && !duelManager.isActive() && token != targetToken) {
            Player playerA = new Player();
            if (token instanceof FleetToken) {
                playerA = ((FleetToken) token).getFleetMenu().getPlayer();
            }
            Player playerD = new Player();
            if (dropTarget instanceof FleetMenu) {
                playerD = ((FleetMenu) dropTarget).getPlayer();
            } else if (dropTarget instanceof MothershipToken) {
                playerD = ((MothershipToken) dropTarget).getCard().getPlayer();
            }
            if (playerA.getBattleID() != -1 && playerD.getBattleID() != -1 && playerA != playerD) {
                if (canReach(token, targetToken, playerD.getFleet())) {
                    duelManager.launchDuel(this, token, targetToken, new DuelPlayer[]{playerToDuelPlayer(playerA)}, new DuelPlayer[]{playerToDuelPlayer(playerD)});
                }
            }
        }
    }

    public static boolean canReach(Token attacker, Token targetToken, Fleet targetFleet) {
        if (targetToken instanceof FleetToken && AbilityManager.hasAttribute(targetToken.getCard(), EffectType.GUARD)) {
            return true;
        }
        int enemyGuards = 0;
        for (Ship ship : targetFleet.getShips()) {
            if (ship != null && AbilityManager.hasAttribute(ship.getToken().getCard(), EffectType.GUARD)) {
                enemyGuards++;
            }
        }
        if (enemyGuards == 0) {
            return true;
        } else {
            int reach = AbilityManager.getReach(attacker.getCard());
            return reach >= enemyGuards;
        }
    }

    private static DuelPlayer playerToDuelPlayer(Player player) {
        return new DuelPlayer(player);
    }

    public void afterDuel() {
        fleetCheck();
    }

    public void endCombat() {
        battleStage.disableCombatEnd();
        battle.setUsedForAllFleets(false);
        active = false;
        System.out.println("Combat Phase ended.");
        battle.getRoundManager().afterCombat();
    }

    public boolean isActive() { return active; }

    public Battle getBattle() { return battle; }

    public DuelManager getDuelManager() { return duelManager; }

    public BattleStage getBattleStage() { return battleStage; }

    public void setBattleStage(BattleStage battleStage) { this.battleStage = battleStage; }

}
