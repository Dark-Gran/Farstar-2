package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public abstract class Dragger extends InputListener {
    public boolean canceled = false;

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (button == 1) {
            event.getStage().removeTouchFocus(this, event.getListenerActor(), event.getTarget(), event.getPointer(), event.getButton());
            canceled = true;
        } else {
            canceled = false;
        }
        return true;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0 && !canceled) {
            drop(x, y);
        }
    }

    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        if (!canceled) {
            drag(x, y);
        }
    }

    protected void drag(float x, float y) { }

    protected void drop(float x, float y) { }

}
