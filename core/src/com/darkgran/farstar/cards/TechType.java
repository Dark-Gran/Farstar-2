package com.darkgran.farstar.cards;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.gui.battlegui.ShotManager;

public enum TechType {
    //Inferior+Superior have only placeholder art (currently NOT on any of the cards)
    NONE(ShotManager.ShotType.BULLET, ColorPalette.TechColors.INFERIOR),
    INFERIOR(ShotManager.ShotType.BULLET, ColorPalette.TechColors.INFERIOR),
    KINETIC(ShotManager.ShotType.BULLET, ColorPalette.TechColors.KINETIC),
    THERMAL(ShotManager.ShotType.BLAST, ColorPalette.TechColors.THERMAL),
    PARTICLE(ShotManager.ShotType.BLAST, ColorPalette.TechColors.PARTICLE),
    SUPERIOR(ShotManager.ShotType.BLAST, ColorPalette.TechColors.SUPERIOR);

    private final ShotManager.ShotType shotType;
    private final Color color;

    TechType(ShotManager.ShotType shotType, Color color) {
        this.shotType = shotType;
        this.color = color;
    }

    public static boolean isInferior(TechType techType) {
        return techType == NONE || techType == INFERIOR;
    }

    public ShotManager.ShotType getShotType() {
        return shotType;
    }

    public Color getColor() {
        return color;
    }
}
