package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.battle.players.CardType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.Ship;

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

    private boolean isEnabled() {
        if (!roundManager.getBattle().isEverythingDisabled() && !roundManager.isTargetingActive()) {
            if (!(this.getToken().getCard() instanceof Ship) || !((Ship) this.getToken().getCard()).haveFought()) {
                if (!combatManager.getDuelManager().isActive() && ownsToken(roundManager.getBattle().getWhoseTurn()) && (forCombat == combatManager.isActive() || (combatManager.isActive() && this.getToken().getCard().getCardInfo().getCardType() == CardType.TACTIC))) {
                    return true;
                } else if (this.getToken().getCard().getCardInfo().getCardType() == CardType.TACTIC && combatManager.getDuelManager().isActive() && ownsToken(combatManager.getDuelManager().getActivePlayer().getPlayer())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ownsToken(Player player) {
        if (getToken() instanceof FleetToken) {
            return player == ((FleetToken) getToken()).getFleetMenu().getPlayer();
        } else {
            return player == getToken().getCardListMenu().getPlayer();
        }
    }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isForCombat() { return forCombat; }

}
