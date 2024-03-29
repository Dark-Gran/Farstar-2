package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.gui.TextLine;

public class TierCounter extends TextLine {
    private final Battle battle;

    public TierCounter(Battle battle, float x, float y) {
        super(ColorPalette.MAIN, "fonts/bahnschrift40b.fnt", x, y, "I.");
        this.battle = battle;
    }

    @Override
    public void drawText(Batch batch) {
        drawText(getFont(), batch, x, y, getTierText(), getFontColor());
    }

    private String getTierText() {
        int roundNum = battle.getRoundManager().getRoundNum();
        if (roundNum < 5) {
            switch (roundNum) {
                default:
                    return "I.";
                case 2:
                    return "II.";
                case 3:
                    return "III.";
                case 4:
                    return "IV.";
            }
        }
        return "V.";
    }
}
