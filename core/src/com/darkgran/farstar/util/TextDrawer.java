package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextDrawer {

    public void draw(BitmapFont font, Batch batch, float x, float y, String txt) {
        font.draw(batch, txt, x, y);
    }

    public void draw (BitmapFont font, Batch batch, float x, float y, String txt, Color color) {
        font.setColor(color);
        font.draw(batch, txt, x, y);
    }

}
