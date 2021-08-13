package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ActorButton extends Actor {
    private final TextureRegion imageUp;
    private final TextureRegion imageOver;
    private TextureRegion imageDown;
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

    public ActorButton(TextureRegion imageUp, TextureRegion imageOver) {
        this.imageUp = imageUp;
        this.imageOver = imageOver;
        this.imageDown = imageOver;
        setWidth(imageOver.getRegionWidth());
        setHeight(imageOver.getRegionHeight());
        addListener(clickListener);
    }

    public ActorButton(TextureRegion imageUp, TextureRegion imageOver, TextureRegion imageDown) {
        this.imageUp = imageUp;
        this.imageOver = imageOver;
        this.imageDown = imageDown;
        setWidth(imageOver.getRegionWidth());
        setHeight(imageOver.getRegionHeight());
        addListener(clickListener);
    }

    public void clicked() { }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isDisabled() && clickListener.isPressed()) {
            batch.draw(imageDown, getX(), getY());
        } else if (!isDisabled() && clickListener.isOver()) {
            batch.draw(imageOver, getX(), getY());
        } else {
            batch.draw(imageUp, getX(), getY());
        }
    }

    public void dispose() {
        removeListener(clickListener);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    protected void setImageDown(TextureRegion imageDown) {
        this.imageDown = imageDown;
    }
}
