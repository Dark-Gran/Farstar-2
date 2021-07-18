package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;

public final class ColorPalette {
    private ColorPalette() { }
    public static final Color MAIN = new Color(0.31f, 0.498f, 0.706f, 1);
    public static final Color DARK = new Color(0f, 0.16f, 0.33f, 1);
    public static final Color LIGHT = new Color(0.43f, 0.7f, 1f, 1);
    public static final Color ENERGY = new Color(0.243f, 0.749f, 0.918f, 1);
    public static final Color MATTER = new Color(0.702f, 0.4f, 0.176f, 1);
    public static final Color PITCH_BLACK = new Color(0f, 0f, 0f, 1);
    public static final Color BLACK = new Color(0.09f, 0.09f, 0.09f, 1);
    public static final Color BLACKISH = new Color(0.11f, 0.11f, 0.11f, 1);
    public static final Color RED = new Color(0.6f, 0.06f, 0.06f, 1);
    public static final Color GREEN = new Color(0.066f, 0.6f, 0.066f, 1);

    public static Color changeAlpha(Color color, float a) {
        return new Color(color.r, color.g, color.b, color.a*a);
    }

}
