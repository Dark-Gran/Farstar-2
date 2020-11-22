package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class CardPart {

    public void draw(BitmapFont font, Batch batch, float x, float y, String txt) {
        font.draw(batch, txt, x, y);
    }
}
