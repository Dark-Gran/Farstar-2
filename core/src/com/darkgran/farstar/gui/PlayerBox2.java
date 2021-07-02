package com.darkgran.farstar.gui;

import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

/**
 * SimpleBox2 with player+battleStage references.
 */
public class PlayerBox2 extends SimpleBox2 {
    private final BattleStage battleStage;
    private Player player;

    public PlayerBox2(float x, float y, BattleStage battleStage, Player player) {
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
