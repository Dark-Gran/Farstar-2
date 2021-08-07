package com.darkgran.farstar.util;

public class DeltaCounter {
    private boolean enabled;
    private float countCap;
    private float accumulator;

    /** Uses SimpleCounter to countUp seconds (and holds the accumulated delta in between). */
    public DeltaCounter(boolean enabled, float countCap, float count) {
        this.enabled = enabled;
        this.countCap = countCap;
        this.accumulator = count;
    }

    public void update(float delta) {
        if (isEnabled()) {
            accumulator += delta;
            if (accumulator >= countCap) {
                enabled = false;
                accumulator = countCap;
            }
        }
    }

    public float getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(float accumulator) {
        this.accumulator = accumulator;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getCountCap() {
        return countCap;
    }

    public void setCountCap(float countCap) {
        this.countCap = countCap;
    }

}
