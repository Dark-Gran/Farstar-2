package com.darkgran.farstar.battle;

public final class BattleSettings { //in-future: apply Singleton-pattern
    private BattleSettings() { }
    //Cards.json setup
    public static final int BONUS_CARD_ID = 21;
    //Battle
    public static final int STARTING_ENERGY = 0;
    public static final int STARTING_MATTER = 1;
    public static final int STARTING_CARDS_ATT = 3;
    public static final int STARTING_CARDS_DEF = 4;
    public static final int CARDS_PER_TURN = 1;
    public static final int MAX_INCOME = 6; //in-future: test higher settings with more powerful(expensive) high-end ships
    public static final int MAX_IN_HAND = 8;
    public static final int DECK_SIZE = 30;
    public static final int YARD_SIZE = 5;
    public static final boolean OUTNUMBERED_DEBUFF_ENABLED = false;
}
