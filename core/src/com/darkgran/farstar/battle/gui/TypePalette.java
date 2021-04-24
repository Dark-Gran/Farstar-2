package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.battle.players.TechType;

public abstract class TypePalette {
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
