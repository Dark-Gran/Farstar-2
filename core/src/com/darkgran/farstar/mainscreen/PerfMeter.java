package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.util.TextDrawer;

public class PerfMeter extends SimpleVector2 implements TextDrawer { //possibly: create TextDrawer+Vector parent implementation? ("TextWithVector" x "TextActor" x "TextInTheBox")
    private String fontPath = "";
    private Color fontColor = new Color();

    public PerfMeter(float x, float y, Color fontColor) {
        setFont("fonts/barlow24.fnt");
        setX(x);
        setY(y);
        setFontColor(fontColor);
    }

    private String getPerfText() {
        String fps = String.valueOf(Gdx.graphics.getFramesPerSecond());
        //String javaHeap = String.valueOf(Gdx.app.getJavaHeap());
        String nativeHeap = String.valueOf(Gdx.app.getNativeHeap());
        return nativeHeap + " - " + fps;
    }

    @Override
    public void drawText(Batch batch) {
        drawText(getFont(), batch, getX(), getY(), getPerfText(), fontColor);
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

}
