package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface DisableMark {
    TextureRegion getMark();
    void setMark(TextureRegion texture);
    boolean isDisabled();

    default void drawMark(Batch batch, float x, float y) {
        if (isDisabled() && getMark() != null) {
            batch.draw(getMark(), x, y);
        }
    }

}
