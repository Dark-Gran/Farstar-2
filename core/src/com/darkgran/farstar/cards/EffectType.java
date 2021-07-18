package com.darkgran.farstar.cards;

public enum EffectType {
    NONE, CHANGE_STAT, FIRST_STRIKE, GUARD, REACH, DEAL_DMG, REPAIR, CHANGE_RESOURCE;

    public static boolean isAttribute(EffectType effectType) {
        return effectType == FIRST_STRIKE || effectType == GUARD || effectType == REACH;
    }
}
