package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.Farstar;

public interface JustFont { //see FontActor for the Actor extension
    StringHolder path = new StringHolder();

    default void setFont(String path) {
        if (path != "") {
            this.path.setString(path);
        } else {
            this.path.setString("fonts/arial15.fnt");
        }
    }

    default BitmapFont getFont() {
        return Farstar.ASSET_LIBRARY.getAssetManager().get(path.getString(), BitmapFont.class);
    }

}
