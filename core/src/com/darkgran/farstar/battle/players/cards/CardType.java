package com.darkgran.farstar.battle.players.cards;

public enum CardType {
    MS,
    SUPPORT,
    YARDPRINT,
    BLUEPRINT,
    UPGRADE,
    TACTIC,
    ACTION;

    public static boolean isShip(CardType cardType) {
        return cardType == YARDPRINT || cardType == BLUEPRINT;
    }

    public static boolean isSpell(CardType cardType) {
        return cardType == ACTION || cardType == UPGRADE || cardType == TACTIC;
    }

    public static boolean needsDefense(CardType cardType) {
        return isShip(cardType) || cardType == SUPPORT || cardType == MS;
    }

}
