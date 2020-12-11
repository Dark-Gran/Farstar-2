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
    private boolean used; //for AbilityStarter.USE
    private int damage = 0;

    public Card(CardInfo cardInfo) {
        originalInfo = cardInfo;
        this.cardInfo = InstanceFactory.instanceCardInfo(originalInfo);
    }

    public Card() {
        originalInfo = Battle.CARD_LIBRARY.getCard(0);
        cardInfo = InstanceFactory.instanceCardInfo(originalInfo);
    }

    public Card(int id) {
        originalInfo = Battle.CARD_LIBRARY.getCard(id);
        cardInfo = InstanceFactory.instanceCardInfo(originalInfo);
    }

    public boolean receiveDMG(int dmg) { //returns survival
        damage += dmg;
        return getHealth() > 0;
    }

    public void repairDMG(int dmg) {
        damage -= dmg;
        if (damage < 0) { damage = 0; }
    }

    public int getHealth() { return cardInfo.getDefense()-damage; }

    public void tickEffects(AbilityManager abilityManager) { //does not remove
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect.getDuration() > 0) {
                effect.setDuration(effect.getDuration() - 1);
            }
        }
    }

    public void checkEffects(AbilityManager abilityManager) { //does not tick
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect.getDuration() == 0) {
                if (abilityManager.executeEffect(this, effect, true)) {
                    effects.remove(effect);
                    i--;
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

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }

    public int getDamage() { return damage; }

}
