package com.darkgran.farstar.battle.players;

public interface BotSettings {
    enum BotTier {
        AUTOMATON(1f);
        private final float timerDelay;
        BotTier(float timerDelay) {
            this.timerDelay = timerDelay;
        }
        public float getTimerDelay(boolean simulation) {
            return simulation ? 0.1f : timerDelay;
        }
    }

}
