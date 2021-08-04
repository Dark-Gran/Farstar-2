package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.SimpleImage2;

public class BattleHelp1v1 extends BattleHelp {
    public BattleHelp1v1(float x, float y) {
        setImage(new SimpleImage2(x, y, Farstar.ASSET_LIBRARY.getAtlasRegion("f1-1v1")));
    }
}
