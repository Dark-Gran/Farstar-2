package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.battle.players.BattlePlayer;

/**
 * Base for laying out menus.
 */
public abstract class BaseMenu extends BattlePlayerBox2 implements Menu {
    private float offset;
    private boolean negativeOffset;

    public BaseMenu(float x, float y, boolean negativeOffset, BattleStage battleStage, BattlePlayer battlePlayer) {
        super(x, y, battleStage, battlePlayer);
        this.negativeOffset = negativeOffset;
        setupOffset();
    }

    @Override
    public boolean isNegativeOffset() {
        return negativeOffset;
    }

    @Override
    public void setNegativeOffset(boolean negativeOffset) {
        this.negativeOffset = negativeOffset;
    }

    @Override
    public float getOffset() {
        return offset;
    }

    @Override
    public void setOffset(float offset) {
        this.offset = offset;
    }

}
