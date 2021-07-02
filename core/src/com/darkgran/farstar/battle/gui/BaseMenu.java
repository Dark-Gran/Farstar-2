package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.PlayerBox2;

/**
 * Base for laying out menus.
 */
public abstract class BaseMenu extends PlayerBox2 {
    private float offset;
    private boolean negativeOffset;

    public BaseMenu(float x, float y, boolean negativeOffset, BattleStage battleStage, Player player) {
        super(x, y, battleStage, player);
        this.negativeOffset = negativeOffset;
        setupOffset();
    }

    protected void setupOffset() {
        offset = TokenType.FLEET.getWidth();
        if (negativeOffset) { offset *= -1; }
    }

    public boolean isEmpty() { return true; }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public boolean isNegativeOffset() { return negativeOffset; }

    public void setNegativeOffset(boolean negativeOffset) { this.negativeOffset = negativeOffset; }


}
