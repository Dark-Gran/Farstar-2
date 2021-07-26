package com.darkgran.farstar.gui;

public class SimpleVector2 {
    public float x;
    public float y;

    public SimpleVector2() {
    }

    public SimpleVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean isSame(SimpleVector2 otherVector) {
        if (otherVector == null) { return false; }
        return x == otherVector.x && y == otherVector.y;
    }

}
