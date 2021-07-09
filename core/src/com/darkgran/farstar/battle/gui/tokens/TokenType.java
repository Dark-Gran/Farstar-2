package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    //Standard
    MS(309f, 184f, "fonts/bahnschrift50b.fnt"), //Parts: 69x52 (g: 420x295)
    FAKE(260f, 155f, "fonts/bahnschrift46b.fnt"), //60x45
    FLEET(232f, 138f, "fonts/bahnschrift44b.fnt"), //54x41 (314x220)
    YARD(216f, 129f, "fonts/bahnschrift44b.fnt"), //F (292x205?)
    SUPPORT(190f, 113f, "fonts/bahnschrift40b.fnt"), //50x38 (257x180)
    //Other
    JUNK(190f, 113f, "fonts/bahnschrift40b.fnt"), //SS
    HAND(232f, 322f, "fonts/bahnschrift44b.fnt"), //FF
    PRINT(348f, 483f, "fonts/bahnschrift58b.fnt"); //81x61

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
