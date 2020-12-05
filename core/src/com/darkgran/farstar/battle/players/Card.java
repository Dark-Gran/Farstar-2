package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;

public class Card {
    private final CardInfo cardInfo;
    private final CardInfo originalInfo;

    public Card(CardInfo cardInfo) {
        this.originalInfo = cardInfo;
        this.cardInfo = cardInfoInstance(this.originalInfo);
    }

    public Card() {
        this.originalInfo = Battle.CARD_LIBRARY.getCard(0);
        this.cardInfo = cardInfoInstance(this.originalInfo);
    }

    public Card(int id) {
        this.originalInfo = Battle.CARD_LIBRARY.getCard(id);
        this.cardInfo = cardInfoInstance(this.originalInfo);
    }

    private CardInfo cardInfoInstance(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getSource(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense());
    }

    public CardInfo getCardInfo() { return cardInfo; }

    public CardInfo getOriginalInfo() { return originalInfo; }

}
