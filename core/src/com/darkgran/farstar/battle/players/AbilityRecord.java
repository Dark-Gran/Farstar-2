package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.cards.AbilityInfo;

public class AbilityRecord {
    private final BattleCard battleCard;
    private final AbilityInfo ability;

    public AbilityRecord(BattleCard battleCard, AbilityInfo ability) {
        this.battleCard = battleCard;
        this.ability = ability;
    }

    public BattleCard getCard() {
        return battleCard;
    }

    public AbilityInfo getAbility() {
        return ability;
    }
}
