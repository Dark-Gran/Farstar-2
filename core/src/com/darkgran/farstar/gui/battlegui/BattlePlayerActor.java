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

    public BattleStage getBattleStage() { return battleStage; }

    public BattlePlayer getBattlePlayer() { return battlePlayer; }

    public void setBattlePlayer(BattlePlayer battlePlayer) { this.battlePlayer = battlePlayer; }

}
