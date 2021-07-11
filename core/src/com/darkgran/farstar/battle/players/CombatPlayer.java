package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.CombatOK;

/**
 * Special combat wrapper (does not extend Player).
 */
public class CombatPlayer {
    private final Player player;
    private CombatOK combatOK = null;
    private boolean ready = false;

    public CombatPlayer(Player player) {
        this.player = player;
    }

    public CombatOK getDuelButton() {
        return combatOK;
    }

    public void setDuelOK(CombatOK combatOK) {
        this.combatOK = combatOK;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Player getPlayer() { return player; }
}
