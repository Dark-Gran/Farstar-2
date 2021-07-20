package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;

public final class ColorPalette {
    private ColorPalette(){}
    public static final Color MAIN = new Color(0.31f, 0.498f, 0.706f, 1); //4f7fb4
    public static final Color DARK = new Color(0f, 0.16f, 0.33f, 1); //002954
    public static final Color LIGHT = new Color(0.43f, 0.7f, 1f, 1); //6eb3ff
    public static final Color ENERGY = new Color(0.243f, 0.749f, 0.918f, 1);
    public static final Color MATTER = new Color(0.702f, 0.4f, 0.176f, 1);
    public static final Color PITCH_BLACK = new Color(0f, 0f, 0f, 1);
    public static final Color BLACK = new Color(0.09f, 0.09f, 0.09f, 1);
    public static final Color BLACKISH = new Color(0.11f, 0.11f, 0.11f, 1); //1c1c1c
    public static final Color RED = new Color(0.6f, 0.06f, 0.06f, 1); //990f0f
    public static final Color GREEN = new Color(0.066f, 0.6f, 0.066f, 1);

    public static final class TechColors {
        private TechColors(){}
        public static final Color INFERIOR = new Color(0.5f, 0.5f, 0.5f, 1);
        public static final Color KINETIC = new Color(0.898f, 0.502f, 0.102f, 1); //e5801a
        public static final Color THERMAL = new Color(0.622f, 0.115f, 0.115f, 1); //9f1d1d (different for pad)
        public static final Color PARTICLE = new Color(0.243f, 0.51f, 0.918f, 1); //3e82ea
        public static final Color SUPERIOR = new Color(0.8f, 0.8f, 0.8f, 1);
    }

    public static Color changeAlpha(Color color, float a) {
        return new Color(color.r, color.g, color.b, color.a*a);
    }

}
