package com.darkgran.farstar.util;

public class DeltaCounter extends SimpleCounter {
    private float accumulator = 0f;

    /** Uses SimpleCounter to countUp seconds (and holds the accumulated delta in between). */
    public DeltaCounter(boolean enabled, int countCap, int count) {
        super(enabled, countCap, count);
    }

    public void update(float delta) {
        if (isEnabled()) {
            accumulator += delta;
            if (accumulator > 1f) {
                accumulator -= 1f;
                update();
            }
        }
    }

    public float getAccumulator() {
        return accumulator;
    }

}
