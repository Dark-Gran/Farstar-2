package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextLine;

/**
 *  Atm works just like TextLine, but might get upgraded in the future (eg. button-link to authors/future-features).
 */
public class VersionInfo extends TextLine {

    public VersionInfo(float x, float y, Color fontColor, String version) {
        super(fontColor, "fonts/bahnschrift24.fnt", x, y, version);
    }

}
