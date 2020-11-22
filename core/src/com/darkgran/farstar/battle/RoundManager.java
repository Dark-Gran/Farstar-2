package com.darkgran.farstar.battle;

public class RoundManager {
    private final Battle battle;
    private int roundNum = 0;

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    public void newRound() {
        roundNum++;
        newTurn();
    }

    public void newTurn() {
        battle.getWhoseTurn().drawCards(battle.CARDS_PER_TURN);
        battle.getWhoseTurn().addEnergy(roundNum);
        battle.getWhoseTurn().addMatter(roundNum);
        System.out.println("Player #"+battle.getWhoseTurn().getBattleID()+" may play his cards.");
    }

    public int getRoundNum() { return roundNum; }

}
