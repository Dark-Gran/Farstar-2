package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SimpleImage2 extends SimpleVector2 {
    private final TextureRegion image;

    public SimpleImage2(float x, float y, TextureRegion image) {
        super(x, y);
        this.image = image;
    }

    public void draw(Batch batch) {
        batch.draw(image, x, y);
    }
}
