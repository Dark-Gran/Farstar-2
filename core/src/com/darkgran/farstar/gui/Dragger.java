package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.gui.tokens.TokenType;


import static com.darkgran.farstar.Farstar.STAGE_HEIGHT;

public abstract class Dragger extends InputListener {
    protected boolean canceled = false;
    protected boolean countingHold = false;
    protected boolean held = false;
    protected boolean active = false;
    private final Draggable draggable;
    private final ListeningStage listeningStage;
    private float timer = 0f;
    private float distanceTraveled = 0f;
    private static final float HOLD_TIME = 0.25f;
    private static final float HOLD_DISTANCE = TokenType.FLEET.getWidth();

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
            if (!active) {
                activate();
            } else {
                dropAtMouse();
            }
        }
        return true;
    }

    public void deactivate() {
        active = false;
        listeningStage.setMainDrag(null);
        resetHold();
    }

    private void resetHold() {
        countingHold = false;
        held = false;
        timer = 0f;
        distanceTraveled = 0f;
    }

    public void activate() {
        active = true;
        listeningStage.setMainDrag(draggable);
        resetHold();
        countingHold = true;
    }

    public void update(float delta, float x, float y) {
        x = x - draggable.getActor().getWidth() / 2;
        y = STAGE_HEIGHT - (y + draggable.getActor().getHeight() / 2);
        if (countingHold) {
            timer += delta;
            distanceTraveled += Math.sqrt(Math.pow(x - draggable.getActor().getX(), 2) + Math.pow(y - draggable.getActor().getY(), 2));
            if (timer >= HOLD_TIME || distanceTraveled >= HOLD_DISTANCE) {
                resetHold();
                held = true;
            }
        }
        drag(x, y);
    }

    public void drag(float x, float y) {
        if (active) {
            if (!canceled) {
                if (draggable.getActor() != null) {
                    draggable.getActor().setPosition(x, y);
                }
            } else {
                deactivate();
            }
        }
    }

    private void dropAtMouse() {
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        drop(coords.x, coords.y);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        if (button == 0 && !canceled) {
            if (held) {
                dropAtMouse();
            } else {
                resetHold();
            }
        }
    }

    public void drop(float x, float y) {
        deactivate();
    }

    public Draggable getDraggable() {
        return draggable;
    }
}
