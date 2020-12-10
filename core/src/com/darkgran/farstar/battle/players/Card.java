package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.abilities.AbilityRecord;
import com.darkgran.farstar.battle.players.abilities.Effect;

import java.util.ArrayList;

public class Card {
    private final CardInfo cardInfo;
    private final CardInfo originalInfo;
    private final ArrayList<Effect> effects = new ArrayList<Effect>();
    private final ArrayList<AbilityRecord> history = new ArrayList<AbilityRecord>();


    public Card(CardInfo cardInfo) {
        this.originalInfo = cardInfo;
        this.cardInfo = InstanceFactory.instanceCardInfo(this.originalInfo);
    }

    public Card() {
        this.originalInfo = Battle.CARD_LIBRARY.getCard(0);
        this.cardInfo = InstanceFactory.instanceCardInfo(this.originalInfo);
    }

    public Card(int id) {
        this.originalInfo = Battle.CARD_LIBRARY.getCard(id);
        this.cardInfo = InstanceFactory.instanceCardInfo(this.originalInfo);
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

    public void addToHistory (Card card, int abilityIX) { history.add(new AbilityRecord(card, abilityIX));  }

    public void addToEffects (Effect effect) { effects.add(effect); }

    public CardInfo getCardInfo() { return cardInfo; }

    public CardInfo getOriginalInfo() { return originalInfo; }

    public ArrayList<Effect> getEffects() { return effects; }

    public ArrayList<AbilityRecord> getHistory() { return history; }

}
