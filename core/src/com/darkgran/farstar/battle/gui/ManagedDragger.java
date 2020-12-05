package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.RoundManager;
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
        if (ownsToken(roundManager.getBattle().getWhoseTurn()) && forCombat == combatManager.isActive() && !combatManager.getDuelManager().isActive()) {
            if (!(this.getToken().getCard() instanceof Ship) || !((Ship) this.getToken().getCard()).haveFought()) {
                super.drag(x, y);
            }
        }
    }

    @Override
    public void drop(float x, float y) {
        if (ownsToken(roundManager.getBattle().getWhoseTurn()) && forCombat == combatManager.isActive() && !combatManager.getDuelManager().isActive()) {
            if (!(this.getToken().getCard() instanceof Ship) || !((Ship) this.getToken().getCard()).haveFought()) {
                super.drop(x, y);
            }
        }
    }

    public boolean ownsToken(Player player) {
        if (getToken() instanceof FleetToken) {
            return player == ((FleetToken) getToken()).getFleetMenu().getPlayer();
        } else {
            return player == getToken().getTokenMenu().getPlayer();
        }
    }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isForCombat() { return forCombat; }

}
