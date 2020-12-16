package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class FontActor extends Actor {
    private BitmapFont font = new BitmapFont();

    public BitmapFont getFont() { return font; }

}
