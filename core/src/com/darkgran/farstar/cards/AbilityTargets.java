package com.darkgran.farstar.cards;

public enum AbilityTargets {
    NONE,
    ANY,
    SELF,
    ANY_ALLY,
    ALLIED_FLEET,
    ALLIED_MS,
    ENTIRE_ALLIED_FLEET,
    ANY_ENEMY,
    ENEMY_FLEET,
    ENEMY_MS,
    ENTIRE_ENEMY_FLEET;

    public static boolean isAoE(AbilityTargets targets) {
        switch (targets) {
            case ALLIED_FLEET:
            case ENEMY_FLEET:
            case ENTIRE_ALLIED_FLEET:
            case ENTIRE_ENEMY_FLEET:
                return true;
            default:
                return false;
        }
    }

}
