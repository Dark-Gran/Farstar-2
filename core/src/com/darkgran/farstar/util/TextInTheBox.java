package com.darkgran.farstar.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Text at coordinates with a box around it.
 */
public class TextInTheBox extends TextLine {
    private Color boxColor;
    private final SimpleBox2 simpleBox;
    private final boolean noBox;

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text) {
        super(fontColor, fontPath, 0, 0, text);
        this.boxColor = boxColor;
        this.noBox = false;
        simpleBox = new SimpleBox2(0, 0, 1, 1);
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text, boolean noBox) {
        super(fontColor, fontPath, 0, 0, text);
        this.boxColor = boxColor;
        this.noBox = noBox;
        simpleBox = new SimpleBox2(0, 0, 1, 1);
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text, float x, float y, float width, float height) {
        super(fontColor, fontPath, x, y, text);
        this.boxColor = boxColor;
        this.noBox = false;
        simpleBox = new SimpleBox2(x, y, width, height);
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text, float x, float y, float width, float height, boolean noBox) {
        super(fontColor, fontPath, x, y, text);
        setFont(fontPath);
        setFontColor(fontColor);
        this.boxColor = boxColor;
        this.noBox = noBox;
        simpleBox = new SimpleBox2(x, y, width, height);
    }

    public void setupBox(float x, float y, float width, float height) {
        simpleBox.setX(x);
        simpleBox.setY(y);
        simpleBox.setHeight(height);
        simpleBox.setWidth(width);
    }

    /** Box outlines keep the same distance from text on all sides. */
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        //BOX
        if (!hasNoBox()) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(getBoxColor());
            shapeRenderer.rect(getX() - simpleBox.getHeight() / 2, getY() + simpleBox.getHeight() / 2, simpleBox.getWidth() + simpleBox.getHeight(), -simpleBox.getHeight() * 2);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.end();
            batch.begin();
        }
        //TEXT
        drawText(batch);
    }

    public Color getBoxColor() {
        return boxColor;
    }

    public void setBoxColor(Color boxColor) {
        this.boxColor = boxColor;
    }

    public SimpleBox2 getSimpleBox() {
        return simpleBox;
    }

    public boolean hasNoBox() { return noBox; }

}
