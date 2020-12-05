package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DropTarget;
import com.darkgran.farstar.battle.gui.FleetMenu;
import com.darkgran.farstar.battle.gui.HandToken;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.*;

public class RoundManager {
    private final Battle battle;
    private int roundNum;
    private boolean firstTurnThisRound;

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    public void launch() {
        roundNum = 0;
        newRound();
    }

    public void newRound() {
        roundNum++;
        firstTurnThisRound = true;
        newTurn();
    }

    public void newTurn() {
        battle.getWhoseTurn().getHand().drawCards(battle.getWhoseTurn().getDeck(), battle.CARDS_PER_TURN);
        resourceIncomes(battle.getWhoseTurn());
        System.out.println("Player #"+battle.getWhoseTurn().getBattleID()+" may play his cards.");
    }

    public void resourceIncomes(Player player) {
        int income = roundNum;
        if (income > battle.MAX_TECH_INCOME) {
            income = battle.MAX_TECH_INCOME;
        }
        //player.setEnergy(income);
        player.addEnergy(income);
        player.addMatter(income);
    }

    public void endTurn() {
        if (!battle.getCombatManager().isActive() && !battle.isEverythingDisabled()) {
            battle.closeYards();
            battle.getCombatManager().launchCombat();
        }
    }

    public void afterCombat() {
        if (!battle.isEverythingDisabled()) {
            battle.passTurn();
            if (firstTurnThisRound) {
                firstTurnThisRound = false;
                newTurn();
            } else {
                newRound();
            }
        }
    }

    public void processDrop(Token token, DropTarget dropTarget, int position) {
        boolean success = false;
        if (dropTarget instanceof FleetMenu && token.getTokenMenu() != null && !battle.getCombatManager().isActive() && !battle.getCombatManager().getDuelManager().isActive()) {
            Fleet fleet = ((FleetMenu) dropTarget).getFleet();
            Player whoseTurn = battle.getWhoseTurn();
            if (fleet == whoseTurn.getFleet() && token.getTokenMenu().getPlayer() == whoseTurn && whoseTurn.canAfford(token.getCard())) {
                success = fleet.addShip(token, position);
            }
            if (success) {
                whoseTurn.payday(token.getCard());
                if (token instanceof HandToken) { token.destroy(); }
            }
        }
        if (!success && token instanceof HandToken) { ((HandToken) token).resetPosition(); }
    }

    public int getRoundNum() { return roundNum; }

    public Battle getBattle() { return battle; }

    public boolean isFirstTurnThisRound() { return firstTurnThisRound; }

}
