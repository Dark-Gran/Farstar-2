package com.darkgran.farstar.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Text at coordinates with a box around it.
 */
public class TextInTheBox extends TextLine {
    private Color boxColor;
    private final SimpleBox2 simpleBox = new SimpleBox2(0, 0, 1, 1);
    private final boolean noBox;

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text) {
        super(fontColor, fontPath, 0, 0, text);
        this.boxColor = boxColor;
        this.noBox = false;
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text, boolean noBox) {
        super(fontColor, fontPath, 0, 0, text);
        this.boxColor = boxColor;
        this.noBox = noBox;
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text, float x, float y, float width, float height) {
        super(fontColor, fontPath, x, y, text);
        this.boxColor = boxColor;
        this.noBox = false;
        simpleBox.setWidth(width);
        simpleBox.setHeight(height);
        centralizeBox();
    }

    public TextInTheBox(Color fontColor, Color boxColor, String fontPath, String text, float x, float y, float width, float height, boolean noBox) {
        super(fontColor, fontPath, x, y, text);
        this.boxColor = boxColor;
        this.noBox = noBox;
        simpleBox.setWidth(width);
        simpleBox.setHeight(height);
        centralizeBox();
    }

    public void centralizeBox() {
        SimpleVector2 boxOrigin = boxOriginFromTextCenter(simpleBox.getWidth(), simpleBox.getHeight(), getFont(), getText(), this.x, this.y, getWrapWidth(), getWrap());
        setupBox(boxOrigin.x, boxOrigin.y, simpleBox.getWidth(), simpleBox.getHeight());
    }

    public static SimpleVector2 boxOriginFromTextCenter(float width, float height, BitmapFont font, String text, float x, float y, float wrapWidth, boolean wrap) {
        return new SimpleVector2((x + TextDrawer.getTextWH(font, text, wrapWidth, wrap).x/2) - width/2,(y - TextDrawer.getTextWH(font, text, wrapWidth, wrap).y/2) - height/2);
    }

    public void setupBox(float x, float y, float width, float height) {
        simpleBox.x = x;
        simpleBox.y = y;
        simpleBox.setHeight(height);
        simpleBox.setWidth(width);
    }

    /** Box outlines keep the same distance from text on all sides. */
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        //BOX
        if (!hasNoBox()) {
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(getBoxColor());
            shapeRenderer.rect(simpleBox.x, simpleBox.y, simpleBox.getWidth(), simpleBox.getHeight());
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
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
