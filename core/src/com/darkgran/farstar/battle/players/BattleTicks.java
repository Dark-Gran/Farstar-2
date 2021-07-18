package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;

public interface BattleTicks {
    CardList getCardList();

    default void setUsedOnAll(boolean used) {
        for (BattleCard battleCard : getCardList()) { if (battleCard != null) { battleCard.setUsed(used); } }
    }

    default void tickEffectsOnAll(AbilityManager abilityManager) {
        for (BattleCard battleCard : getCardList()) { if (battleCard != null) { battleCard.tickEffects(abilityManager); } }
    }

    default void checkEffectsOnAll(AbilityManager abilityManager) {
        for (BattleCard battleCard : getCardList()) { if (battleCard != null) { battleCard.checkEffects(abilityManager); } }
    }

}