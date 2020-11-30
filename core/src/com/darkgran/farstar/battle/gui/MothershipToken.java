package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

public class MothershipToken extends Token implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private final Player player;

    public MothershipToken(Player player, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(player.getMs(), x, y, battleStage, tokenMenu);
        setupSimpleBox2(x, y, getHeight(), getWidth());
        this.player = player;
    }

    @Override
    public void setupSimpleBox2(float x, float y, float height, float width) {
        simpleBox2.setX(x);
        simpleBox2.setY(y);
        simpleBox2.setHeight(height);
        simpleBox2.setWidth(width);
    }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }

    public Player getPlayer() { return player; }

}
