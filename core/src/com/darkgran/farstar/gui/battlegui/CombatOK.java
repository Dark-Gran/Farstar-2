package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.players.CombatPlayer;
import com.darkgran.farstar.gui.ButtonWithExtraState;

public class CombatOK extends ButtonWithExtraState {
    private CombatPlayer combatPlayer;
    private final CombatManager combatManager;

    public CombatOK(TextureRegion imageUp, TextureRegion imageOver, TextureRegion extraUp, TextureRegion extraOver, CombatManager combatManager) {
        super(imageUp, imageOver, extraUp, extraOver);
        this.combatManager = combatManager;
        setDisabled(true);
        setExtraState(true);
    }

    @Override
    public void clicked() {
        if (!combatManager.getBattle().getRoundManager().isTargetingActive() && !combatManager.getBattle().getRoundManager().getAbilityPicker().isActive()) {
            combatManager.getBattleStage().getBattleScreen().hideScreenConceder();
            combatManager.tacticalOK(this);
        } else {
            combatManager.getBattle().getRoundManager().tryCancel();
        }
    }

    public void setDuelPlayer(CombatPlayer combatPlayer) { this.combatPlayer = combatPlayer; }

    public CombatPlayer getDuelPlayer() { return combatPlayer; }

}
