package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ButtonWithExtraState extends ActorButton {
    private final Texture extraUp;
    private final Texture extraOver;
    private final Texture extraDown;
    private boolean possible;

    public ButtonWithExtraState(Texture imageUp, Texture imageOver, Texture extraUp, Texture extraOver) {
        super(imageUp, imageOver);
        this.extraUp = extraUp;
        this.extraOver = extraOver;
        this.extraDown = extraOver;
    }

    public ButtonWithExtraState(Texture imageUp, Texture imageOver, Texture imageDown, Texture extraUp, Texture extraOver, Texture extraDown) {
        super(imageUp, imageOver, imageDown);
        this.extraUp = extraUp;
        this.extraOver = extraOver;
        this.extraDown = extraDown;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (possible) {
            if (getClickListener().isPressed()) {
                batch.draw(extraDown, getX(), getY());
            } else if (getClickListener().isOver()) {
                batch.draw(extraOver, getX(), getY());
            } else {
                batch.draw(extraUp, getX(), getY());
            }
        } else {
            super.draw(batch, parentAlpha);
        }
    }
}
