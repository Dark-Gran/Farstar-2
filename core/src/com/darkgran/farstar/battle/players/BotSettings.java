package com.darkgran.farstar.battle.players;

public interface BotSettings {
    enum BotTier {
        AUTOMATON;
    }

    static float getTimerDelay(BotTier botTier) {
        return 1f;
    }

}
