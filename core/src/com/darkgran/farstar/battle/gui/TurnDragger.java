package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.CombatManager;

public class TurnDragger extends Dragger {
    private final CombatManager combatManager;

    public TurnDragger(Token token, CombatManager combatManager) {
        super(token);
        this.combatManager = combatManager;
        System.out.println(combatManager==null);
    }

    @Override
    public void drag(float x, float y) {
        if (!combatManager.isActive()) {
            super.drag(x, y);
        }
    }

    @Override
    public void drop(float x, float y) {
        if (!combatManager.isActive()) {
            super.drop(x, y);
        }
    }
}
