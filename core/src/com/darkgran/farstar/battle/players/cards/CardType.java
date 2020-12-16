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
        return cardType == CardType.YARDPRINT || cardType == CardType.BLUEPRINT;
    }

    public static boolean isSpell(CardType cardType) {
        return cardType == CardType.ACTION || cardType == CardType.UPGRADE || cardType == CardType.TACTIC;
    }

}
