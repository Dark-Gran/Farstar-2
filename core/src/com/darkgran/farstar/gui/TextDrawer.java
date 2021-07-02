package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.util.SimpleVector2;

public interface TextDrawer extends JustFont {
    void drawText(Batch batch);

    default void drawText(BitmapFont font, Batch batch, float x, float y, String txt) {
        font.setColor(getFontColor());
        font.draw(batch, txt, x, y);
        font.setColor(Color.WHITE);
    }

    default void drawText(BitmapFont font, Batch batch, float x, float y, String txt, Color color) {
        font.setColor(color);
        font.draw(batch, txt, x, y);
        font.setColor(Color.WHITE);
    }

    Color getFontColor();
    void setFontColor(Color fontColor);

    static SimpleVector2 getTextWH(BitmapFont font, String txt) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, txt);
        return new SimpleVector2(layout.width, layout.height);
    }

}
