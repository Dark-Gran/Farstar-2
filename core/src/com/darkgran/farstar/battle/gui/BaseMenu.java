package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.util.TextDrawer;

//base for laying out menus
public abstract class BaseMenu extends SimpleBox2 {
    private float offset;
    private boolean negativeOffset;
    private final BattleStage battleStage;
    private Player player;

    public BaseMenu(float x, float y, boolean negativeOffset, BattleStage battleStage, Player player) {
        setX(x);
        setY(y);
        this.player = player;
        this.negativeOffset = negativeOffset;
        setupOffset();
        this.battleStage = battleStage;
    }

    protected void setupOffset() {
        SimpleVector2 textWH = TextDrawer.getTextWH(Farstar.ASSET_LIBRARY.getAssetManager().get("fonts/arial15.fnt"), "Battlestation");
        offset = textWH.getX();
        if (negativeOffset) { offset *= -1; }
    }

    public boolean isEmpty() { return true; }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public boolean isNegativeOffset() { return negativeOffset; }

    public void setNegativeOffset(boolean negativeOffset) { this.negativeOffset = negativeOffset; }

    public BattleStage getBattleStage() { return battleStage; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}
