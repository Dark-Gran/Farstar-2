package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.darkgran.farstar.battle.players.DuelPlayer;

public class DuelOK extends ImageButton {
    private DuelPlayer duelPlayer;

    public DuelOK(Drawable imageUp) {
        super(imageUp);
    }

    public void setDuelPlayer(DuelPlayer duelPlayer) { this.duelPlayer = duelPlayer; }

    public DuelPlayer getDuelPlayer() { return duelPlayer; }

}
