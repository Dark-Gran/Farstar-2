package com.darkgran.farstar.util;

public class SimpleCounter {
    private boolean enabled;
    private int timerCap;
    private int time;

    public SimpleCounter(boolean enabled, int timerCap, int time) {
        this.enabled = enabled;
        this.timerCap = timerCap;
        this.time = time;
    }

    public void update() {
        if (enabled) {
            time++;
            if (time >= timerCap) {
                enabled = false;
                time = 0;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
