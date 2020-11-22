package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.Player;

public class RoundManager {
    private final Battle battle;
    private int roundNum = 0;
    private boolean firstTurnThisRound;

    public RoundManager(Battle battle) {
        this.battle = battle;
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
        player.addEnergy(income);
        player.addMatter(income);
    }

    public void endTurn() {
        battle.passTurn();
        if (firstTurnThisRound) {
            firstTurnThisRound = false;
            newTurn();
        } else {
            newRound();
        }
    }

    public int getRoundNum() { return roundNum; }

}
