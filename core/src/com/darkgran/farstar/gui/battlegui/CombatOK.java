package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.players.CombatPlayer;
import com.darkgran.farstar.gui.ButtonWithExtraState;

public class CombatOK extends ButtonWithExtraState {
    private CombatPlayer combatPlayer;
    private CombatManager combatManager;

    public CombatOK(Texture imageUp, Texture imageOver, Texture extraUp, Texture extraOver, CombatManager combatManager) {
        super(imageUp, imageOver, extraUp, extraOver);
        this.combatManager = combatManager;
        setDisabled(true);
        setExtraState(true);
    }

    @Override
    public void clicked() {
        combatManager.tacticalOK(this);
    }

    public void setDuelPlayer(CombatPlayer combatPlayer) { this.combatPlayer = combatPlayer; }

    public CombatPlayer getDuelPlayer() { return combatPlayer; }

}
