package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Text at coordinates with a box around it.
 */
public class TextInTheBox extends SimpleBox2 implements TextDrawer {
    private Color fontColor;
    private Color boxColor;
    private String fontPath = "";
    private String message = "";
    private final boolean noBox;

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String message) {
        setFont(fontPath);
        setFontColor(fontColor);
        this.boxColor = boxColor;
        this.message = message;
        this.noBox = false;
        setupBox(0, 0, 1, 1);
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String message, boolean noBox) {
        setFont(fontPath);
        setFontColor(fontColor);
        this.boxColor = boxColor;
        this.message = message;
        this.noBox = noBox;
        setupBox(0, 0, 1, 1);
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String message, float x, float y, float width, float height) {
        setFont(fontPath);
        setFontColor(fontColor);
        this.boxColor = boxColor;
        this.message = message;
        this.noBox = false;
        setupBox(x, y, width, height);
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String message, float x, float y, float width, float height, boolean noBox) {
        setFont(fontPath);
        setFontColor(fontColor);
        this.boxColor = boxColor;
        this.message = message;
        this.noBox = noBox;
        setupBox(x, y, width, height);
    }

    /** Box outlines keep the same distance from text on all sides. */
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        //BOX
        if (!hasNoBox()) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(getBoxColor());
            shapeRenderer.rect(getX() - getHeight() / 2, getY() + getHeight() / 2, getWidth() + getHeight(), -getHeight() * 2);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.end();
            batch.begin();
        }
        //TEXT
        drawText(batch);
    }

    @Override
    public void drawText(Batch batch) {
        drawText(getFont(), batch, getX(), getY(), getMessage(), getFontColor());
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }

    @Override
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public String getMessage() {
        return message;
    }

    public Color getBoxColor() {
        return boxColor;
    }

    public void setBoxColor(Color boxColor) {
        this.boxColor = boxColor;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean hasNoBox() { return noBox; }
}
