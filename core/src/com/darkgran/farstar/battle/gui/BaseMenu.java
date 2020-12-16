package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.util.SimpleBox2;

//base for laying out menus
public abstract class BaseMenu extends SimpleBox2 {
    private GlyphLayout layout = new GlyphLayout();
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

    public void setupOffset() {
        String res = "Battlestation";
        layout.setText(new BitmapFont(), res);
        offset = layout.width;
        if (negativeOffset) { offset *= -1; }
    }

    public boolean isEmpty() { return true; }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public GlyphLayout getLayout() { return layout; }

    public boolean isNegativeOffset() { return negativeOffset; }

    public void setNegativeOffset(boolean negativeOffset) { this.negativeOffset = negativeOffset; }

    public BattleStage getBattleStage() { return battleStage; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}
