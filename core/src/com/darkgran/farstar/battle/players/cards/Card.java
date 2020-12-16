package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityRecord;
import com.darkgran.farstar.battle.players.abilities.Effect;

import java.util.ArrayList;

public class Card {
    private Player player;
    private final CardInfo cardInfo;
    private final CardInfo originalInfo;
    private final ArrayList<Effect> effects = new ArrayList<Effect>();
    private final ArrayList<AbilityRecord> history = new ArrayList<AbilityRecord>();
    private boolean used; //for AbilityStarter.USE
    private int damage = 0;
    private boolean possible; //for PossibilityAdvisor

    public Card(CardInfo cardInfo, Player player) {
        originalInfo = cardInfo;
        this.cardInfo = instanceCardInfo(originalInfo);
        this.player = player;
    }

    public Card() {
        originalInfo = Farstar.CARD_LIBRARY.getCard(0);
        cardInfo = instanceCardInfo(originalInfo);
        player = null;
    }

    public Card(int id) {
        originalInfo = Farstar.CARD_LIBRARY.getCard(id);
        cardInfo = instanceCardInfo(originalInfo);
        player = null;
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

    private CardInfo instanceCardInfo(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getCardType(), cardInfo.getTier(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense(), cardInfo.getOffenseType(), cardInfo.getDefenseType(), cardInfo.getAbilities());
    }

    public void death() { }

    public void addToHistory (Card card, AbilityInfo ability) { history.add(new AbilityRecord(card, ability));  }

    public void addToEffects (Effect effect) { effects.add(effect); }

    public CardInfo getCardInfo() { return cardInfo; }

    public CardInfo getOriginalInfo() { return originalInfo; }

    public ArrayList<Effect> getEffects() { return effects; }

    public ArrayList<AbilityRecord> getHistory() { return history; }

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }

    public int getDamage() { return damage; }

    public void setDamage(int damage) { this.damage = damage; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    public boolean isPossible() { return possible; }

    public void setPossible(boolean possible) { this.possible = possible; }


}
