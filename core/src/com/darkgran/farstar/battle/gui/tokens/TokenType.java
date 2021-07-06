package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    MS(309f, 184f),
    FAKE(260f, 155f),
    FLEET(232f, 138f),
    YARD(216f, 128f),
    SUPPORT(190f, 113f),
    JUNK(88f, 116f); //to be adjusted

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
