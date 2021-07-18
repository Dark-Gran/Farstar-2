package com.darkgran.farstar.cards;

public enum TechType {
    NONE, INFERIOR, KINETIC, THERMAL, PARTICLE, SUPERIOR; //Inferior+Superior have only placeholder art (currently NOT on any of the cards)

    public static boolean isInferior(TechType techType) {
        return techType == NONE || techType == INFERIOR;
    }

}
