package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.darkgran.farstar.util.SimpleVector2;

public interface TextDrawer extends JustFont {

    Color getFontColor();
    void setFontColor(Color fontColor);
    boolean getWrap();
    void setWrap(boolean wrap);
    float getWrapWidth();
    void setWrapWidth(float width);

    default void drawText(Batch batch) {}

    default void drawText(BitmapFont font, Batch batch, float x, float y, String txt) {
        font.setColor(getFontColor());
        font.draw(batch, txt, x, y, getWrapWidth(), Align.left, getWrap());
        font.setColor(Color.WHITE);
    }

    default void drawText(BitmapFont font, Batch batch, float x, float y, String txt, Color color) {
        font.setColor(color);
        font.draw(batch, txt, x, y, getWrapWidth(), Align.left, getWrap());
        font.setColor(Color.WHITE);
    }

    static SimpleVector2 getTextWH(BitmapFont font, String txt) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, txt);
        return new SimpleVector2(layout.width, layout.height);
    }

    static SimpleVector2 getTextWH(BitmapFont font, String txt, float wrapWidth, boolean wrap) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, txt, Color.WHITE, wrapWidth, Align.left, wrap);
        return new SimpleVector2(layout.width, layout.height);
    }

}
