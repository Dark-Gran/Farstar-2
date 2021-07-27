package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.RoundManager;

public class ManagedTokenDragger extends TokenDragger {
    private final RoundManager roundManager;
    private final CombatManager combatManager;

    public ManagedTokenDragger(Token token, RoundManager roundManager) {
        super(token);
        this.roundManager = roundManager;
        this.combatManager = roundManager.getBattle().getCombatManager();
    }

    @Override
    protected void drag(float x, float y) {
        if (isEnabled()) {
            super.drag(x, y);
        }
    }

    @Override
    protected void drop(float x, float y) {
        if (isEnabled()) {
            super.drop(x, y);
        }
    }

    public boolean isEnabled() {
        if (getToken() instanceof TargetingToken) {
            return roundManager.isCombatMoveEnabled(getToken());
        }
        return roundManager.isTokenMoveEnabled(getToken());
    }

    public CombatManager getCombatManager() { return combatManager; }

}