package com.darkgran.farstar.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

public interface Delayer {

    default void delayAction(Runnable runnable, float timerDelay) {
        Timer.schedule(new Timer.Task() {
            public void run() {
                if (runnable != null) { Gdx.app.postRunnable(runnable); }
            }
        }, timerDelay);
    }

}
