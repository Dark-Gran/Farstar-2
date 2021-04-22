package com.darkgran.farstar.util;

public class SimpleCounter {
    private boolean enabled;
    private int timerCap;
    private int timer;

    public SimpleCounter(boolean enabled, int timerCap, int timer) {
        this.enabled = enabled;
        this.timerCap = timerCap;
        this.timer = timer;
    }

    public void update() {
        if (enabled) {
            timer++;
            if (timer >= timerCap) {
                enabled = false;
                timer = 0;
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTimerCap() {
        return timerCap;
    }

    public void setTimerCap(int timerCap) {
        this.timerCap = timerCap;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
