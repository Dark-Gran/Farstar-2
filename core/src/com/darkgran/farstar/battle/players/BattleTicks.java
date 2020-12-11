package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;

public interface BattleTicks {
    void setUsedOnAll(boolean used);
    void tickEffectsOnAll(AbilityManager abilityManager);
    void checkEffectsOnAll(AbilityManager abilityManager);
}
