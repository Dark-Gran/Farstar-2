package com.darkgran.farstar.battle.cards;

import com.darkgran.farstar.battle.Battle;

public class Card {
    private final CardInfo cardInfo;

    public Card(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public Card() {
        cardInfo = Battle.CARD_LIBRARY.getCard(0);
    }

}
