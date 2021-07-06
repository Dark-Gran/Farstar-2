package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    MS(309f, 184f, "fonts/bahnschrift50b.fnt"),
    FAKE(260f, 155f, "fonts/bahnschrift46b.fnt"),
    FLEET(232f, 138f, "fonts/bahnschrift44b.fnt"),
    YARD(216f, 129f, "fonts/bahnschrift44b.fnt"),
    SUPPORT(190f, 113f, "fonts/bahnschrift40b.fnt"),
    JUNK(88f, 116f, "fonts/bahnschrift30.fnt"); //to be adjusted

    private final float width;
    private final float height;
    private final String fontPath;

    TokenType(float width, float height, String fontPath) {
        this.width = width;
        this.height = height;
        this.fontPath = fontPath;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getFontPath() {
        return fontPath;
    }
}
