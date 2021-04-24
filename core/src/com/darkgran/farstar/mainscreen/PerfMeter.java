package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.TextLine;

public class PerfMeter extends TextLine {

    public PerfMeter(float x, float y, Color fontColor) {
        super(fontColor, "fonts/barlow24.fnt", x, y, "-");
    }

    @Override
    public void drawText(Batch batch) {
        drawText(getFont(), batch, getX(), getY(), getPerfText(), getFontColor());
    }

    private String getPerfText() {
        String fps = String.valueOf(Gdx.graphics.getFramesPerSecond());
        //String javaHeap = String.valueOf(Gdx.app.getJavaHeap());
        String nativeHeap = String.valueOf(Gdx.app.getNativeHeap());
        return nativeHeap + " - " + fps;
    }

}
