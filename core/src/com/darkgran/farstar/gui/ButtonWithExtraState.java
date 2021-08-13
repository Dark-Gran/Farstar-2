package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ButtonWithExtraState extends ActorButton {
    private final TextureRegion extraUp;
    private final TextureRegion extraOver;
    private final TextureRegion extraDown;
    private boolean extraState;

    public ButtonWithExtraState(TextureRegion imageUp, TextureRegion imageOver, TextureRegion extraUp, TextureRegion extraOver) {
        super(imageUp, imageOver);
        this.extraUp = extraUp;
        this.extraOver = extraOver;
        this.extraDown = extraOver;
    }

    public ButtonWithExtraState(TextureRegion imageUp, TextureRegion imageOver, TextureRegion imageDown, TextureRegion extraUp, TextureRegion extraOver, TextureRegion extraDown) {
        super(imageUp, imageOver, imageDown);
        this.extraUp = extraUp;
        this.extraOver = extraOver;
        this.extraDown = extraDown;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (extraState) {
            if (!isDisabled() && getClickListener().isPressed()) {
                batch.draw(extraDown, getX(), getY());
            } else if (!isDisabled() && getClickListener().isOver()) {
                batch.draw(extraOver, getX(), getY());
            } else {
                batch.draw(extraUp, getX(), getY());
            }
        } else {
            super.draw(batch, parentAlpha);
        }
    }

    public boolean isExtraState() {
        return extraState;
    }

    public void setExtraState(boolean extraState) {
        this.extraState = extraState;
    }
}
