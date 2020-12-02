package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.DuelPlayer;
import com.darkgran.farstar.battle.players.Player;

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
        if (battle.getWhoseTurn().getFleet().noAttackers()) {
            endCombat();
        } else {
            battleStage.enableCombatEnd();
        }
    }

    public void processDrop(Token token, DropTarget dropTarget, Token targetToken) {
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
                battleStage.disableCombatEnd();
                duelManager.launchDuel(this, token, targetToken, new DuelPlayer[]{playerToDuelPlayer(playerA)}, new DuelPlayer[]{playerToDuelPlayer(playerD)});
            }
        }
    }

    private DuelPlayer playerToDuelPlayer(Player player) {
        return new DuelPlayer((byte) player.getBattleID(), player.getEnergy(), player.getMatter(), player.getMs(), player.getDeck(), player.getShipyard());
    }

    public void afterDuel() {
        battleStage.enableCombatEnd();
    }

    public void endCombat() {
        battleStage.disableCombatEnd();
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
