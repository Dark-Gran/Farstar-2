package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.battle.players.DuelPlayer;
import com.darkgran.farstar.gui.ActorButton;

public class DuelOK extends ActorButton {
    private DuelPlayer duelPlayer;
    private DuelManager duelManager;

    public DuelOK(Texture imageUp, Texture imageOver, DuelManager duelManager) {
        super(imageUp, imageOver);
        this.duelManager = duelManager;
        setDisabled(true);
    }

    @Override
    public void clicked() {
        duelManager.OK(this);
    }

    public void setDuelPlayer(DuelPlayer duelPlayer) { this.duelPlayer = duelPlayer; }

    public DuelPlayer getDuelPlayer() { return duelPlayer; }

}
