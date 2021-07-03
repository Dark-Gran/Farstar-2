package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.Player;

/**
 * "PlayerBox2 but Actor"
 */
public abstract class PlayerActor extends Actor {
    private final BattleStage battleStage;
    private Player player;

    public PlayerActor(float x, float y, BattleStage battleStage, Player player) {
        setX(x);
        setY(y);
        this.player = player;
        this.battleStage = battleStage;
    }

    public boolean isEmpty() { return true; }

    public BattleStage getBattleStage() { return battleStage; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}
