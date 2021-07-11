package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.gui.ActorButton;

public abstract class CombatMenu {
    private final CombatManager combatManager;
    private BattleStage battleStage;  //must be set after ini - before RM.launch (see BattleScreen constructor)
    private final ActorButton cancelButton = new ActorButton(Farstar.ASSET_LIBRARY.get("images/duel_cancel.png"), Farstar.ASSET_LIBRARY.get("images/duel_cancelO.png")){
        @Override
        public void clicked() {

        }
    };

    public CombatMenu(CombatManager combatManager) {
        this.combatManager = combatManager;
        combatManager.setCombatMenu(this);
        cancelButton.setDisabled(true);
    }

    public void setBattleStage(BattleStage battleStage) { this.battleStage = battleStage; }

    public void removeAllOKs() { }

    public void addOK(CombatOK combatOK) {
        if (combatOK.getDuelPlayer().getPlayer() instanceof LocalPlayer) {
            getBattleStage().addActor(combatOK);
            combatOK.setDisabled(false);
        }
    }

    public void removeOK(CombatOK combatOK) {
        combatOK.remove();
        combatOK.setDisabled(true);
    }

    public void addCancel() {
        getBattleStage().addActor(cancelButton);
        cancelButton.setDisabled(false);
    }

    public void removeCancel() {
        cancelButton.remove();
        cancelButton.setDisabled(true);
    }

    public CombatManager getCombatManager() { return combatManager; }

    public BattleStage getBattleStage() { return battleStage; }

    public ActorButton getCancelButton() { return cancelButton; }



}
