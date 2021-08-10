package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;

public final class ColorPalette { //in-future: make into a SuperScreen instance as there will be multiple ColorPallets from which the player may choose
    private ColorPalette(){}
    public static final Color MAIN = new Color(0.31f, 0.498f, 0.706f, 1); //4f7fb4
    public static final Color DARK = new Color(0f, 0.16f, 0.33f, 1); //002954
    public static final Color LIGHT = new Color(0.43f, 0.7f, 1f, 1); //6eb3ff
    public static final Color ENERGY = new Color(0.243f, 0.749f, 0.918f, 1);
    public static final Color MATTER = new Color(0.702f, 0.4f, 0.176f, 1);
    public static final Color PITCH_BLACK = new Color(0f, 0f, 0f, 1);
    public static final Color BLACK = new Color(0.08f, 0.08f, 0.08f, 1);
    public static final Color BLACKISH = new Color(0.09f, 0.09f, 0.09f, 1);
    public static final Color RED = new Color(0.6f, 0.06f, 0.06f, 1); //990f0f
    public static final Color GREEN = new Color(0.066f, 0.6f, 0.066f, 1);

    public static final class TechColors {
        private TechColors(){}
        public static final Color INFERIOR = new Color(0.5f, 0.5f, 0.5f, 1);
        public static final Color KINETIC = new Color(0.8f, 0.442f, 0.08f, 1); //cc7114
        public static final Color THERMAL = new Color(0.8f, 0.08f, 0.08f, 1); //cc1414
        public static final Color PARTICLE = new Color(0.08f, 0.368f, 0.8f, 1); //145ecc
        public static final Color SUPERIOR = new Color(0.8f, 0.8f, 0.8f, 1);
    }

    public static Color changeAlpha(Color color, float a) {
        return new Color(color.r, color.g, color.b, color.a*a);
    }

}
