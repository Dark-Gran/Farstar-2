package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface CardGFX {
    float PORTRAIT_OFFSET_Y = -16f;

    void setCardPic(Texture texture);
    Texture getCardPic();

    default void drawCardGFX(Batch batch, float x, float y) {
        batch.draw(getCardPic(), x, y);
    }
}
