package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.CombatManager;

public class ManagedDragger extends Dragger {
    //private final RoundManager roundManager;
    private final CombatManager combatManager;
    private final boolean forCombat;

    public ManagedDragger(Token token, CombatManager combatManager, boolean forCombat) {
        super(token);
        this.combatManager = combatManager;
        this.forCombat = forCombat;
    }

    @Override
    public void drag(float x, float y) {
        if (forCombat == combatManager.isActive() && !combatManager.isInDuel()) {
            super.drag(x, y);
        }
    }

    @Override
    public void drop(float x, float y) {
        if (forCombat == combatManager.isActive() && !combatManager.isInDuel()) {
            super.drop(x, y);
        }
    }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isForCombat() { return forCombat; }

}
