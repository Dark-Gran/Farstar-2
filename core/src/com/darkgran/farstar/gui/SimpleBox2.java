package com.darkgran.farstar.gui;

public class SimpleBox2 extends SimpleVector2 {
    private float width;
    private float height;

    public SimpleBox2() {
    }

    public SimpleBox2(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void setupBox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        setHeight(height);
        setWidth(width);
    }

    public float getWidth() { return width; }

    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }

    public void setHeight(float height) { this.height = height; }

}
