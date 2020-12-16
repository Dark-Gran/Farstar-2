package com.darkgran.farstar.battle.players;

public interface BotSettings {
    enum BotTier {
        AUTOMATON;
    }

    default float getTimerDelay(BotTier botTier) {
        return 1f;
    }

}
