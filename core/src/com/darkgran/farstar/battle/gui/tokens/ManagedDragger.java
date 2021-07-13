package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.battle.players.LocalPlayer;

public class ManagedDragger extends Dragger {
    private final RoundManager roundManager;
    private final CombatManager combatManager;
    private final boolean forCombat;

    public ManagedDragger(Token token, RoundManager roundManager, boolean forCombat) {
        super(token);
        this.roundManager = roundManager;
        this.combatManager = roundManager.getBattle().getCombatManager();
        this.forCombat = forCombat;
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
        if (getToken().getCard().getPlayer() instanceof LocalPlayer && !roundManager.getBattle().isEverythingDisabled() && !roundManager.isTargetingActive() && !roundManager.getAbilityPicker().isActive()) {
            //if (!(this.getToken().getCard() instanceof Ship) || !((Ship) this.getToken().getCard()).haveFought()) {
                if (!combatManager.getDuelManager().isActive() && RoundManager.ownsToken(roundManager.getBattle().getWhoseTurn(), getToken()) && (!combatManager.isActive() || (combatManager.isTacticalPhase() && this.getToken().getCard().isTactic()))) { //forCombat == combatManager.isActive()
                    return true;
                } else if (!combatManager.getDuelManager().isActive() && this.getToken().getCard().isTactic() && combatManager.isTacticalPhase() && RoundManager.ownsToken(combatManager.getActivePlayer().getPlayer(), getToken())) {
                    return true;
                }
            //}
        }
        return false;
    }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isForCombat() { return forCombat; }

}
