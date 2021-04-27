package com.darkgran.farstar.util;

public class SimpleCounter {
    private boolean enabled;
    private int countCap;
    private int count;

    /** When enabled, counts up through update(). Disables itself when countCap is reached. */
    public SimpleCounter(boolean enabled, int countCap, int count) {
        this.enabled = enabled;
        this.countCap = countCap;
        this.count = count;
    }

    public void update() {
        if (enabled) {
            count++;
            if (count >= countCap) {
                enabled = false;
                count = 0;
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCountCap() {
        return countCap;
    }

    public void setCountCap(int countCap) {
        this.countCap = countCap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
