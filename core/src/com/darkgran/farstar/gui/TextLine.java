package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.SimpleVector2;

public class TextLine extends SimpleVector2 implements TextDrawer { //in-future: TextActor variant/implementation
    private boolean wrap = false;
    private float wrapWidth = 0f;
    private Color fontColor;
    private String fontPath = "";
    private String text = "";

    public TextLine() {
        this.fontColor = Color.WHITE;
    }

    public TextLine(Color fontColor, String fontPath, float x, float y, String text) {
        this.fontColor = fontColor;
        this.x = x;
        this.y = y;
        setFont(fontPath);
        this.text = text;
    }

    public TextLine(String fontPath) {
        this.fontColor = Color.WHITE;
        setFontPath(fontPath);
    }

    @Override
    public void drawText(Batch batch) {
        if (!getFontPath().equals("")) {
            drawText(getFont(), batch, x, y, text, getFontColor());
        }
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }

    @Override
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    @Override
    public boolean getWrap() {
        return wrap;
    }

    @Override
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    @Override
    public float getWrapWidth() {
        return wrapWidth;
    }

    @Override
    public void setWrapWidth(float width) {
        wrapWidth = width;
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
