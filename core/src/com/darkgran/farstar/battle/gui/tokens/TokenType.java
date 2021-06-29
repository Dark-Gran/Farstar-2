package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    MS(340f, 180f),
    SUPPORT(190f, 100f),
    FLEET(232f, 137f);

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
