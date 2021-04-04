package com.darkgran.farstar.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class JustFont extends SimpleVector2 { //see FontActor for the Actor extension
    private BitmapFont font;

    public JustFont(String path) {
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
