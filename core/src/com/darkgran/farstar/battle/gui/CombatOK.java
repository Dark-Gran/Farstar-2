package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.battle.players.CombatPlayer;
import com.darkgran.farstar.gui.ActorButton;

public class CombatOK extends ActorButton {
    private CombatPlayer combatPlayer;
    private CombatManager combatManager;

    public CombatOK(Texture imageUp, Texture imageOver, CombatManager combatManager) {
        super(imageUp, imageOver);
        this.combatManager = combatManager;
        setDisabled(true);
    }

    @Override
    public void clicked() {
        combatManager.tacticalOK(this);
    }

    public void setDuelPlayer(CombatPlayer combatPlayer) { this.combatPlayer = combatPlayer; }

    public CombatPlayer getDuelPlayer() { return combatPlayer; }

}
