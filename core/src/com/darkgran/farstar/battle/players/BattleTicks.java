package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.players.cards.Card;

public interface BattleTicks {
    CardList getCardList();

    default void setUsedOnAll(boolean used) {
        for (Card card : getCardList()) { if (card != null) { card.setUsed(used); } }
    }

    default void tickEffectsOnAll(AbilityManager abilityManager) {
        for (Card card : getCardList()) { if (card != null) { card.tickEffects(abilityManager); } }
    }

    default void checkEffectsOnAll(AbilityManager abilityManager) {
        for (Card card : getCardList()) { if (card != null) { card.checkEffects(abilityManager); } }
    }

}