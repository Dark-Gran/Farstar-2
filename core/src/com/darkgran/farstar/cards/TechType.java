package com.darkgran.farstar.cards;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.gui.ColorPalette;

public enum TechType {
    //Inferior+Superior have only placeholder art (currently NOT on any of the cards)
    NONE(ColorPalette.TechColors.INFERIOR),
    INFERIOR(ColorPalette.TechColors.INFERIOR),
    KINETIC(ColorPalette.TechColors.KINETIC),
    THERMAL(ColorPalette.TechColors.THERMAL),
    PARTICLE(ColorPalette.TechColors.PARTICLE),
    SUPERIOR(ColorPalette.TechColors.SUPERIOR);

    private final Color color;

    TechType(Color color) {
        this.color = color;
    }

    public static boolean isInferior(TechType techType) {
        return techType == NONE || techType == INFERIOR;
    }

    public Color getColor() {
        return color;
    }
}
