package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ActorButton extends Actor {
    private final Texture imageUp;
    private final Texture imageOver;
    private final Texture imageDown;
    private boolean disabled = false;
    private final ClickListener clickListener = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            if (!disabled) {
                ActorButton.this.clicked();
            }
        }
    };

    public ActorButton(Texture imageUp, Texture imageOver) {
        this.imageUp = imageUp;
        this.imageOver = imageOver;
        this.imageDown = imageOver;
        setWidth(imageOver.getWidth());
        setHeight(imageOver.getHeight());
        addListener(clickListener);
    }

    public ActorButton(Texture imageUp, Texture imageOver, Texture imageDown) {
        this.imageUp = imageUp;
        this.imageOver = imageOver;
        this.imageDown = imageDown;
        setWidth(imageOver.getWidth());
        setHeight(imageOver.getHeight());
        addListener(clickListener);
    }

    public void clicked() { }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (clickListener.isPressed()) {
            batch.draw(imageDown, getX(), getY());
        } else if (clickListener.isOver()) {
            batch.draw(imageOver, getX(), getY());
        } else {
            batch.draw(imageUp, getX(), getY());
        }
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
