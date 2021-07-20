package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.gui.battlegui.CombatOK;

/**
 * Special combat wrapper (does not extend Player).
 */
public class CombatPlayer {
    private final BattlePlayer battlePlayer;
    private CombatOK combatOK = null;
    private boolean ready = false;

    public CombatPlayer(BattlePlayer battlePlayer) {
        this.battlePlayer = battlePlayer;
    }

    public CombatOK getCombatButton() {
        return combatOK;
    }

    public void setCombatOK(CombatOK combatOK) {
        this.combatOK = combatOK;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public BattlePlayer getBattlePlayer() { return battlePlayer; }
}
