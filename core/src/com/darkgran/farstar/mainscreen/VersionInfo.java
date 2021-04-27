package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextLine;

public class VersionInfo extends TextLine {

    public VersionInfo(float x, float y, Color fontColor) {
        super(fontColor, "fonts/barlow24.fnt", x, y, Farstar.APP_VERSION);
    }

}
