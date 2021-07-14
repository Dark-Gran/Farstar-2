package com.darkgran.farstar.battle.players;

public interface BotSettings {
    enum BotTier {
        AUTOMATON(0.3f);
        private final float timerDelay;
        BotTier(float timerDelay) {
            this.timerDelay = timerDelay;
        }
        public float getTimerDelay() { return timerDelay; }
    }

}
