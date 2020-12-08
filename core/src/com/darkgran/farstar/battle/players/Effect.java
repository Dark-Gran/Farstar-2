package com.darkgran.farstar.battle.players;

public class Effect {
    private EffectType effectType;
    private int duration;

    public Effect() {
    }

    public Effect(EffectType effectType, int duration) {
        this.effectType = effectType;
        this.duration = duration;
    }

    public void setDuration(int duration) { this.duration = duration; }

    public EffectType getEffectType() { return effectType; }

    public int getDuration() { return duration; }

    public void setEffectType(EffectType effectType) { this.effectType = effectType; }

}
