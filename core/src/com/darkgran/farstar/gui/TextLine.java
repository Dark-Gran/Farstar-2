package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.SimpleVector2;

public class TextLine extends SimpleVector2 implements TextDrawer { //in-future: TextActor variant/implementation
    private Color fontColor = new Color();
    private String fontPath = "";
    private String text = "";

    public TextLine() { }

    public TextLine(Color fontColor, String fontPath, float x, float y, String text) {
        this.fontColor = fontColor;
        setX(x);
        setY(y);
        setFont(fontPath);
        this.text = text;
    }

    @Override
    public void drawText(Batch batch) {
        if (!getFontPath().equals("")) {
            drawText(getFont(), batch, getX(), getY(), text, getFontColor());
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
