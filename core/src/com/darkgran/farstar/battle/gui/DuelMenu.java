package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.gui.ActorButton;

public abstract class DuelMenu {
    private final DuelManager duelManager;
    private BattleStage battleStage;  //must be set after ini - before RM.launch (see BattleScreen constructor)
    private final ActorButton cancelButton = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/duel_cancel.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/duel_cancelO.png")){
        @Override
        public void clicked() {
            getDuelManager().cancelDuel();
        }
    };

    public DuelMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        duelManager.receiveDuelMenu(this);
        cancelButton.setDisabled(true);
    }

    public void setBattleStage(BattleStage battleStage) { this.battleStage = battleStage; }

    public void removeAllOKs() { }

    public void addOK(DuelOK duelOK) {
        if (duelOK.getDuelPlayer().getPlayer() instanceof LocalPlayer) {
            getBattleStage().addActor(duelOK);
            duelOK.setDisabled(false);
        }
    }

    public void removeOK(DuelOK duelOK) {
        duelOK.remove();
        duelOK.setDisabled(true);
    }

    public void addCancel() {
        getBattleStage().addActor(cancelButton);
        cancelButton.setDisabled(false);
    }

    public void removeCancel() {
        cancelButton.remove();
        cancelButton.setDisabled(true);
    }

    public DuelManager getDuelManager() { return duelManager; }

    public BattleStage getBattleStage() { return battleStage; }

    public ActorButton getCancelButton() { return cancelButton; }



}
