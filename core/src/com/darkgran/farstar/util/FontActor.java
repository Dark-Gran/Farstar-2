package com.darkgran.farstar.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class FontActor extends Actor {
    private final BitmapFont font;

    public FontActor(String path) {
        if (path == "") {
            font = new BitmapFont();
        } else {
            font = new BitmapFont(Gdx.files.internal(path));
        }
    }

    public BitmapFont getFont() { return font; }

    public void dispose() {
        font.dispose();
    }

}
