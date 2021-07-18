package com.darkgran.farstar.cards;

public enum CardType {
    MS,
    SUPPORT,
    YARDPRINT,
    BLUEPRINT,
    ACTION,
    TACTIC;

    public static boolean isShip(CardType cardType) {
        return cardType == YARDPRINT || cardType == BLUEPRINT;
    }

    public static boolean isSpell(CardType cardType) {
        return cardType == ACTION || cardType == TACTIC;
    }

    public static boolean needsDefense(CardType cardType) {
        return isShip(cardType) || cardType == SUPPORT || cardType == MS;
    }

}
