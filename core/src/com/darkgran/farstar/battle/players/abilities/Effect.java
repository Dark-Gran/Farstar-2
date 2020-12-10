package com.darkgran.farstar.battle.players.abilities;

import java.util.ArrayList;

public class Effect {
    //libgdx jsonReader example uses raw type - to apply generics, the jsonReader may need an upgrade
    //until then, it is mandatory to use only Strings and Floats (in cards.json-effectInfo)
    private ArrayList effectInfo;
    private EffectType effectType;
    private int duration;

    public Effect() {
    }

    public Effect(EffectType effectType, ArrayList effectInfo, int duration) {
        this.effectType = effectType;
        this.effectInfo = effectInfo;
        this.duration = duration;
    }

    public void setDuration(int duration) { this.duration = duration; }

    public int getDuration() { return duration; }

    public EffectType getEffectType() { return effectType; }

    public void setEffectType(EffectType effectType) { this.effectType = effectType; }

    public ArrayList getEffectInfo() { return effectInfo; }

    public void setEffectInfo(ArrayList effectInfo) { this.effectInfo = effectInfo; }

}
