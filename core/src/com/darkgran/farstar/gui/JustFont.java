package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.Farstar;

/**
 * Holds a String path to the font and provides a way for default - the actual handling (and disposal) of fonts is done by the AssetManager/AssetLibrary.
 */
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
