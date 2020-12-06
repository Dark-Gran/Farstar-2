package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;

public class RoundManager {
    private final Battle battle;
    private int roundNum = 0;
    private boolean firstTurnThisRound;

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    public void launch() {
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
        if (token.getTokenMenu() != null && !battle.getCombatManager().isActive() && !battle.getCombatManager().getDuelManager().isActive()) {
            Player whoseTurn = battle.getWhoseTurn();
            if (token.getTokenMenu().getPlayer() == whoseTurn) {
                if (dropTarget instanceof FleetMenu) { //Fleet Deploy
                    Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                    if (fleet == whoseTurn.getFleet() && whoseTurn.canAfford(token.getCard())) {
                        success = fleet.addShip(token, position);
                    }
                    if (success) { whoseTurn.payday(token.getCard()); }
                } else if (dropTarget instanceof JunkButton && token instanceof HandToken) { //Discard
                    Junkpile junkpile = ((JunkButton) dropTarget).getPlayer().getJunkpile();
                    if (junkpile == whoseTurn.getJunkpile()) {
                        junkpile.addCard(token.getCard());
                        success = true;
                    }
                }
            }
        }
        if (token instanceof HandToken) {
            if (!success) { ((HandToken) token).resetPosition(); }
            else { token.destroy(); }
        }
    }

    public int getRoundNum() { return roundNum; }

    public Battle getBattle() { return battle; }

    public boolean isFirstTurnThisRound() { return firstTurnThisRound; }

}
