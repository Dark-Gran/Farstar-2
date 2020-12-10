package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.abilities.Effect;

import java.util.ArrayList;

public class Card {
    private final CardInfo cardInfo;
    private final CardInfo originalInfo;
    private final ArrayList<Effect> effects = new ArrayList<Effect>();
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

    public void checkEffects(AbilityManager abilityManager) {
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect.getDuration() > 0) {
                effect.setDuration(effect.getDuration() - 1);
                if (effect.getDuration() == 0) {
                    if (abilityManager.executeEffect(this, effect, true)) {
                        effects.remove(effect);
                        i--;
                    }
                }
            }
        }
    }

    public void death() { }

    private CardInfo cardInfoInstance(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getCardType(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense(), cardInfo.getOffenseType(), cardInfo.getDefenseType(), cardInfo.getAbility());
    }

    public void addToHistory (Card card) { history.add(card);  }

    public void addToEffects (Effect effect) { effects.add(effect); }

    public CardInfo getCardInfo() { return cardInfo; }

    public CardInfo getOriginalInfo() { return originalInfo; }

    public ArrayList<Effect> getEffects() { return effects; }

    public ArrayList<Card> getHistory() { return history; }

}
