package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.battle.players.BattlePlayer;

/**
 * "PlayerBox2 but Actor"
 */
public abstract class BattlePlayerActor extends Actor {
    private final BattleStage battleStage;
    private BattlePlayer battlePlayer;

    public BattlePlayerActor(float x, float y, BattleStage battleStage, BattlePlayer battlePlayer) {
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
