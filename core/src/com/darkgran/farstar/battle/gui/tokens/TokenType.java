package com.darkgran.farstar.battle.gui.tokens;

public enum TokenType {
    //Standard
    MS(309f, 184f, "48"), //Parts: 69x52 (g: 420x295)
    FAKE(260f, 155f, "44"), //60x45 (352x247)
    FLEET(232f, 138f, "40"), //54x41 (314x220)
    YARD(216f, 129f, "40"), //F (292x205)
    SUPPORT(190f, 113f, "38"), //50x38 (257x180)
    //Other
    JUNK(190f, 113f, "38"), //SS
    HAND(232f, 322f, "38"), //FF (355x445)
    PRINT(348f, 483f, "58"); //(=Z) 81x61

    private final float width;
    private final float height;
    private final String fontSize;

    TokenType(float width, float height, String fontSize) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getFontSize() { return fontSize; }

    public static boolean isDeployed(TokenType tokenType) {
        return tokenType == MS || tokenType == SUPPORT || tokenType == FLEET;
    }
}
