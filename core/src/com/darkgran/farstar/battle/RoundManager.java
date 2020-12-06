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
            if (token.getTokenMenu().getPlayer() == whoseTurn) { //CAN DEPLOY
                CardType cardType = token.getCard().getCardInfo().getCardType();
                if (dropTarget instanceof FleetMenu) { //Target: Fleet
                    Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                    if (fleet == whoseTurn.getFleet() && whoseTurn.canAfford(token.getCard())) {
                        if (cardType == CardType.BLUEPRINT || cardType == CardType.YARD) {
                            success = fleet.addShip(token, position);
                        } else if (cardType == CardType.UPGRADE) {
                            success = battle.getAbilityManager().playAbility(token, fleet.getShips()[position].getToken());
                        }
                    }
                } else if (dropTarget instanceof MothershipToken) { //Target: MS
                    MothershipToken ms = (MothershipToken) dropTarget;
                    if (ms.getPlayer() == whoseTurn && whoseTurn.canAfford(token.getCard()) && cardType == CardType.UPGRADE) {
                        success = battle.getAbilityManager().playAbility(token, ms);
                    }
                }
                if (success) {
                    whoseTurn.payday(token.getCard());
                    token.addToJunk();
                } else if (dropTarget instanceof JunkButton && token instanceof HandToken) { //Target: Discard
                    Junkpile junkpile = ((JunkButton) dropTarget).getPlayer().getJunkpile();
                    if (junkpile == whoseTurn.getJunkpile()) {
                        token.addToJunk();
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
