package com.darkgran.farstar.battle;

public class Card {
    private final CardInfo cardInfo;

    public Card(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public Card() {
        cardInfo = BattleManager.CARD_LIBRARY.getCard(0);
    }

}
