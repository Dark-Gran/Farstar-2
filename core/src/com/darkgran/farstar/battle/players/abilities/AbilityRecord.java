package com.darkgran.farstar.battle.players.abilities;

import com.darkgran.farstar.battle.players.cards.Card;

public class AbilityRecord {
    private final Card card;
    private final AbilityInfo ability;

    public AbilityRecord(Card card, AbilityInfo ability) {
        this.card = card;
        this.ability = ability;
    }

    public Card getCard() {
        return card;
    }

    public AbilityInfo getAbility() {
        return ability;
    }
}
