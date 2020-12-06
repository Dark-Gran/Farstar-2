package com.darkgran.farstar.battle.players;

import java.util.ArrayList;

public class AbilityInfo {
    private final AbilityStarter starter;
    private final ArrayList<Integer> effects;

    public AbilityInfo(AbilityStarter starter, ArrayList<Integer> effects) {
        this.starter = starter;
        this.effects = effects;
    }

    public AbilityInfo() {
        this.starter = AbilityStarter.DEPLOY;
        this.effects = new ArrayList<>();
    }

    public AbilityStarter getStarter() { return starter; }

    public ArrayList<Integer> getEffects() { return effects; }

}
