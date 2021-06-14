package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    MS(100f, 50f),
    SUPPORT(100f, 50f),
    FLEET(100f, 50f);

    private final float width;
    private final float height;

    TokenType(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
