package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.darkgran.farstar.battle.players.DuelPlayer;

public class DuelOK extends ImageButton {
    private DuelPlayer duelPlayer;

    public DuelOK(Skin skin) {
        super(skin);
    }

    public DuelOK(Skin skin, String styleName) {
        super(skin, styleName);
    }

    public DuelOK(ImageButtonStyle style) {
        super(style);
    }

    public DuelOK(Drawable imageUp) {
        super(imageUp);
    }

    public DuelOK(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown);
    }

    public DuelOK(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
        super(imageUp, imageDown, imageChecked);
    }

    public DuelPlayer getDuelPlayer() { return duelPlayer; }

    public void setDuelPlayer(DuelPlayer duelPlayer) { this.duelPlayer = duelPlayer; }
}
