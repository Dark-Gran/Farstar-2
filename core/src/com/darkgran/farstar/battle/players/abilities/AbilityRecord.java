package com.darkgran.farstar.battle.players.abilities;

import com.darkgran.farstar.battle.players.Card;

public class AbilityRecord {
    private final Card card;
    private final int abilityIX;

    public AbilityRecord(Card card, int abilityIX) {
        this.card = card;
        this.abilityIX = abilityIX;
    }

    public Card getCard() {
        return card;
    }

    public int getAbilityID() {
        return abilityIX;
    }
}
