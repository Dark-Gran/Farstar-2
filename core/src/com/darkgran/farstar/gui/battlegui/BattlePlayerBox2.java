package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.util.SimpleBox2;

/**
 * SimpleBox2 with battleStage+player (for simple drawing use PB2Drawer).
 */
public abstract class BattlePlayerBox2 extends SimpleBox2 {
    private final BattleStage battleStage;
    private BattlePlayer battlePlayer;

    public BattlePlayerBox2(float x, float y, BattleStage battleStage, BattlePlayer battlePlayer) {
        setX(x);
        setY(y);
        this.battlePlayer = battlePlayer;
        this.battleStage = battleStage;
    }

    public boolean isEmpty() { return true; }

    public BattleStage getBattleStage() { return battleStage; }

    public BattlePlayer getPlayer() { return battlePlayer; }

    public void setPlayer(BattlePlayer battlePlayer) { this.battlePlayer = battlePlayer; }

}
