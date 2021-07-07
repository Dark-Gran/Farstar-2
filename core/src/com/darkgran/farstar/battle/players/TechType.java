package com.darkgran.farstar.battle.players;

public enum TechType {
    NONE, INFERIOR, KINETIC, THERMAL, PARTICLE, SUPERIOR;

    public static boolean isInferior(TechType techType) {
        return techType == NONE || techType == INFERIOR;
    }

}
