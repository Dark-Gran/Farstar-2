package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface DisableMark {
    Texture getMark();
    void setMark(Texture texture);
    boolean isDisabled();

    default void drawMark(Batch batch, float x, float y) {
        if (isDisabled() && getMark() != null) {
            batch.draw(getMark(), x, y);
        }
    }

}
