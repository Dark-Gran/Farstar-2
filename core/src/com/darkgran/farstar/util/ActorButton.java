package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ActorButton extends Actor {
    private final Texture imageUp;
    private final Texture imageOver;
    private final ClickListener clickListener = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            ActorButton.this.clicked();
        }
    };

    public ActorButton(Texture imageUp, Texture imageOver) {
        this.imageUp = imageUp;
        this.imageOver = imageOver;
        setWidth(imageOver.getWidth());
        setHeight(imageOver.getHeight());
        addListener(clickListener);
    }

    public void clicked() { }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (clickListener.isOver()) {
            batch.draw(imageOver, getX(), getY());
        } else {
            batch.draw(imageUp, getX(), getY());
        }
    }

}
