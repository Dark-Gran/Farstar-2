package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.battle.players.TechType;

public final class ColorPalette {
    private ColorPalette() { }
    public static final Color MAIN = new Color(0.31f, 0.498f, 0.706f, 1);
    public static final Color DARK = new Color(0f, 0.16f, 0.33f, 1);
    public static final Color LIGHT = new Color(0.43f, 0.7f, 1f, 1);
    public static final Color ENERGY = new Color(0.525f, 0.973f, 0.886f, 1);
    public static final Color MATTER = new Color(0.718f, 0.475f, 0.345f, 1);

    public static Color changeAlpha(Color color, float a) {
        return new Color(color.r, color.g, color.b, color.a*a);
    }

    public static Color getTypeColor(TechType techType) {
        switch (techType) {
            case INFERIOR:
                return Color.GRAY;
            case KINETIC:
                return Color.ORANGE;
            case THERMAL:
                return Color.RED;
            case PARTICLE:
                return Color.SKY;
            case SUPERIOR:
                return Color.PURPLE;
        }
        return Color.WHITE;
    }

}
