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
        if (!roundManager.getBattle().isEverythingDisabled()) {
            if (!(this.getToken().getCard() instanceof Ship) || !((Ship) this.getToken().getCard()).haveFought()) {
                if (!combatManager.getDuelManager().isActive() && ownsToken(roundManager.getBattle().getWhoseTurn()) && forCombat == combatManager.isActive()) {
                    return true;
                } else if (this.getToken().getCard().getCardInfo().getCardType() == CardType.TACTIC && combatManager.getDuelManager().isActive() && ownsToken(combatManager.getDuelManager().getActivePlayer())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ownsToken(Player player) {
        if (getToken() instanceof FleetToken) {
            return player.getBattleID() == ((FleetToken) getToken()).getFleetMenu().getPlayer().getBattleID();
        } else {
            return player.getBattleID() == getToken().getTokenMenu().getPlayer().getBattleID();
        }
    }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isForCombat() { return forCombat; }

}
