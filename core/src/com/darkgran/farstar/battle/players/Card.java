package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;

import java.util.ArrayList;

public class Card {
    private final CardInfo cardInfo;
    private final CardInfo originalInfo;
    private final ArrayList<Card> permanents = new ArrayList<Card>();
    private final ArrayList<Effect> temps = new ArrayList<Effect>();
    private final ArrayList<Card> history = new ArrayList<Card>();


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

    public boolean receiveDMG(int dmg) { //returns survival
        this.cardInfo.setDefense(this.cardInfo.getDefense()-dmg);
        return this.cardInfo.getDefense() > 0;
    }

    public void checkEffects() {
        for (Effect effect : temps) {
            effect.setDuration(effect.getDuration()-1);
            if (effect.getDuration() <= 0) {
                //TODO undoEffect
            }
        }
    }

    public boolean applyUpgrade(Card card, Effect effect) {
        boolean success = false;
        CardInfo upgradeInfo = card.getCardInfo();
        if (upgradeInfo.getCardType() == CardType.UPGRADE) {
            cardInfo.changeOffense(upgradeInfo.getOffense());
            cardInfo.changeDefense(upgradeInfo.getDefense());
            if (upgradeInfo.getOffenseType() != TechType.NONE) { cardInfo.setOffenseType(upgradeInfo.getOffenseType()); }
            if (upgradeInfo.getDefenseType() != TechType.NONE) { cardInfo.setDefenseType(upgradeInfo.getDefenseType()); }
            if (effect.getDuration() > 0) {
                temps.add(effect);
            } else {
                permanents.add(card);
            }
            history.add(card);
            success = true;
        }
        return success;
    }

    public void death() { }

    private CardInfo cardInfoInstance(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getCardType(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense(), cardInfo.getOffenseType(), cardInfo.getDefenseType(), cardInfo.getAbility());
    }

    public CardInfo getCardInfo() { return cardInfo; }

    public CardInfo getOriginalInfo() { return originalInfo; }

}
