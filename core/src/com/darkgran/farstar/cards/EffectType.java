package com.darkgran.farstar.cards;

public enum EffectType {
    NONE, CHANGE_STAT, FIRST_STRIKE, GUARD, REACH, DEAL_DMG, REPAIR, CHANGE_RESOURCE, ARMOR;

    public static boolean isAttribute(EffectType effectType) {
        return effectType == FIRST_STRIKE || effectType == GUARD || effectType == REACH || effectType == ARMOR;
    }

    public static boolean isParaoffenseEffect(EffectType effectType) { //for disabling upgrading certain abilities on motherships
        return effectType == FIRST_STRIKE || effectType == GUARD || effectType == REACH;
    }

}
