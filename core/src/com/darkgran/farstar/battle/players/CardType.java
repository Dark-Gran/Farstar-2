package com.darkgran.farstar.battle.players;

public enum CardType {
    MS,
    SUPPORT,
    YARD,
    BLUEPRINT,
    UPGRADE,
    TACTIC,
    ACTION;

    public static boolean isShip(CardType cardType) {
        return cardType == CardType.YARD || cardType == CardType.BLUEPRINT;
    }

    public static boolean isSpell(CardType cardType) {
        return cardType == CardType.ACTION || cardType == CardType.UPGRADE || cardType == CardType.TACTIC;
    }

}
