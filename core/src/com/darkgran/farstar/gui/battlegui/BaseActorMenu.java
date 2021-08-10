package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.battle.players.BattlePlayer;

/**
 * "BaseMenu but Actor"
 */

public abstract class BaseActorMenu extends BattlePlayerActor implements Menu {
    private float offset;
    private boolean negativeOffset;

    public BaseActorMenu(float x, float y, boolean negativeOffset, BattleStage battleStage, BattlePlayer battlePlayer) {
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
