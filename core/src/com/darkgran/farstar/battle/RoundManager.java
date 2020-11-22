package com.darkgran.farstar.battle;

public class RoundManager {
    private final Battle battle;
    private int roundNum = 0;

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    public void newRound() {
        System.out.println("New Round.");
        roundNum++;
        newTurn();
    }

    public void newTurn() {
        System.out.println("New Turn.");
        battle.getWhoseTurn().drawCards(battle.CARDS_PER_TURN);

    }

    public int getRoundNum() { return roundNum; }

}
