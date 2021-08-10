package com.darkgran.farstar.battle;

public final class BattleSettings {
    private static BattleSettings battleSettings;
    private BattleSettings() {}
    public static BattleSettings getInstance() {
        if (battleSettings == null) {
            battleSettings = new BattleSettings();
        }
        return battleSettings;
    }
    public void dispose() {
        battleSettings = null;
    }
    //Cards.json setup
    public final int BONUS_CARD_ID = 21;
    //Battle
    public final int STARTING_ENERGY = 0;
    public final int STARTING_MATTER = 1;
    public final int STARTING_CARDS_ATT = 3;
    public final int STARTING_CARDS_DEF = 4;
    public final int CARDS_PER_TURN = 1;
    public final int MAX_INCOME = 6; //in-future: test higher settings with more powerful(expensive) high-end ships
    public final int MAX_IN_HAND = 8;
    public final int DECK_SIZE = 30;
    public final int YARD_SIZE = 5;
    public final boolean OUTNUMBERED_DEBUFF_ENABLED = false;
}
