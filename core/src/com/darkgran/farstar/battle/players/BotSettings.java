package com.darkgran.farstar.battle.players;

public interface BotSettings { //in-future: rework so the enum holds timerDelay?
    enum BotTier {
        AUTOMATON;
    }

    default float getTimerDelay(BotTier botTier) {
        return 1f;
    }

}
