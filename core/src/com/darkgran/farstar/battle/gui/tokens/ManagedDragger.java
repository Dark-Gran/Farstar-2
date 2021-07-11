package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Ship;

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
                if (!combatManager.getDuelManager().isActive() && RoundManager.ownsToken(roundManager.getBattle().getWhoseTurn(), getToken()) && (forCombat == combatManager.isActive() || (combatManager.isActive() && this.getToken().getCard().isTactic()))) {
                    return true;
                } else if (this.getToken().getCard().isTactic() && combatManager.getDuelManager().isActive() && RoundManager.ownsToken(combatManager.getActivePlayer().getPlayer(), getToken())) {
                    return true;
                }
            //}
        }
        return false;
    }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isForCombat() { return forCombat; }

}
