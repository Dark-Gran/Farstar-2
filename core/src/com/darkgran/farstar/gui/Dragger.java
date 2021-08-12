package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.SuperScreen;


import static com.darkgran.farstar.Farstar.STAGE_HEIGHT;

public abstract class Dragger extends InputListener {
    protected boolean canceled = false;
    protected boolean active = false;
    private final Draggable draggable;
    private final ListeningStage listeningStage;

    public Dragger(Draggable draggable, ListeningStage listeningStage) {
        this.draggable = draggable;
        this.listeningStage = listeningStage;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (button == 1) {
            event.getStage().removeTouchFocus(this, event.getListenerActor(), event.getTarget(), event.getPointer(), event.getButton());
            canceled = true;
        } else {
            canceled = false;
            active = true;
            listeningStage.setMainDrag(draggable);
        }
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        if (button == 0 && !canceled) {
            SimpleVector2 coords = SuperScreen.getMouseCoordinates();
            drop(coords.x, coords.y);
        }
    }

    public void drag(float x, float y) {
        if (active) {
            if (!canceled) {
                if (draggable.getActor() != null) {
                    draggable.getActor().setPosition(x - draggable.getActor().getWidth() / 2, STAGE_HEIGHT - (y + draggable.getActor().getHeight() / 2));
                }
            } else {
                active = false;
                listeningStage.setMainDrag(null);
            }
        }
    }

    public void drop(float x, float y) {
        active = false;
        listeningStage.setMainDrag(null);
    }

    public Draggable getDraggable() {
        return draggable;
    }
}
