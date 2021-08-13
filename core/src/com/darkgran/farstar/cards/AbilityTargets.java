package com.darkgran.farstar.cards;

public enum AbilityTargets {
    NONE,
    ANY,
    SELF, //= ship
    OWNER, //= player
    ADJACENT,
    ANY_ALLY,
    ALLIED_MS,
    ALLIED_FLEET, //"single fleet" targeting-process (see AbilityManager() for askForTargets()) possibly untested (no cards with such targets atm); not really needed until other mods than 1v1 become a thing and need special balance
    //in 1v1, "ENTIRE" is more suiting as there is no need to ask for targets ("which fleet")
    ENTIRE_ALLIED_FLEET,
    ANY_ENEMY,
    ENEMY_MS,
    ENEMY_FLEET,
    ENTIRE_ENEMY_FLEET;

    public static boolean isAoE(AbilityTargets targets) {
        switch (targets) {
            case ALLIED_FLEET:
            case ENEMY_FLEET:
            case ENTIRE_ALLIED_FLEET:
            case ENTIRE_ENEMY_FLEET:
            case ADJACENT:
                return true;
            default:
                return false;
        }
    }

}
