package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.gui.Draggable;
import com.darkgran.farstar.gui.ListeningStage;

public class ManagedTokenDragger extends TokenDragger {
    private final RoundManager roundManager;
    private final CombatManager combatManager;

    public ManagedTokenDragger(Draggable draggable, ListeningStage listeningStage, RoundManager roundManager) {
        super(draggable, listeningStage);
        this.roundManager = roundManager;
        this.combatManager = roundManager.getBattle().getCombatManager();
    }

    @Override
    public void drag(float x, float y) {
        if (isEnabled()) {
            super.drag(x, y);
        }
    }

    @Override
    public void drop(float x, float y) {
        if (isEnabled()) {
            super.drop(x, y);
        }
    }

    public boolean isEnabled() {
        if (getDraggable() instanceof TargetingToken) {
            return roundManager.isCombatMoveEnabled((Token) getDraggable());
        }
        return roundManager.isTokenMoveEnabled((Token) getDraggable());
    }

    public CombatManager getCombatManager() { return combatManager; }

}