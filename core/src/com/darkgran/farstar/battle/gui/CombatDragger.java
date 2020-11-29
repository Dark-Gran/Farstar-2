package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.CombatManager;

public class CombatDragger extends Dragger {
    private final CombatManager combatManager;

    public CombatDragger(Token token, CombatManager combatManager) {
        super(token);
        this.combatManager = combatManager;
    }

    @Override
    public void drag(float x, float y) {
        if (combatManager.isActive()) {
            System.out.println("OK");
            super.drag(x, y);
        }
    }

    @Override
    public void drop(float x, float y) {
        if (combatManager.isActive()) {
            super.drop(x, y);
        }
    }
}
