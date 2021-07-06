package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    MS(340f, 180f),
    FAKE(260f, 155f),
    FLEET(232f, 138f),
    YARD(216f, 120f),
    SUPPORT(190f, 100f),
    JUNK(88f, 116f);

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
