package com.darkgran.farstar.battle.players.abilities;

import java.util.ArrayList;

public class Effect {
    private EffectType effectType;
    private ArrayList effectInfo;
    private int duration;

    public Effect() {
    }

    public Effect(EffectType effectType, int duration) {
        this.effectType = effectType;
        this.duration = duration;
    }

    public void setDuration(int duration) { this.duration = duration; }

    public int getDuration() { return duration; }

    public EffectType getEffectType() { return effectType; }

    public void setEffectType(EffectType effectType) { this.effectType = effectType; }

    public ArrayList getEffectInfo() { return effectInfo; }

    public void setEffectInfo(ArrayList effectInfo) { this.effectInfo = effectInfo; }

}
