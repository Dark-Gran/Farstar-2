package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    MS(309f, 184f, "fonts/bahnschrift50b.fnt"), //Parts: 69x52
    FAKE(260f, 155f, "fonts/bahnschrift46b.fnt"), //60x45
    FLEET(232f, 138f, "fonts/bahnschrift44b.fnt"), //54x41
    YARD(216f, 129f, "fonts/bahnschrift44b.fnt"), //F
    SUPPORT(190f, 113f, "fonts/bahnschrift40b.fnt"), //50x38
    JUNK(88f, 116f, "fonts/bahnschrift30.fnt"), //to be adjusted
    HAND(232f, 322f, "fonts/bahnschrift44b.fnt"); //FF

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

    public static boolean isDeployed(TokenType tokenType) {
        return tokenType == MS || tokenType == SUPPORT || tokenType == FLEET;
    }
}
