package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.Farstar;

public interface JustFont {

    default void setFont(String path) { //Must be set
        if (path != "") {
            setFontPath(path);
        } else {
            setFontPath("fonts/arial15.fnt");
        }
    }

    default BitmapFont getFont() {
        return Farstar.ASSET_LIBRARY.getAssetManager().get(getFontPath(), BitmapFont.class);
    }

    String getFontPath();
    void setFontPath(String path);

}
