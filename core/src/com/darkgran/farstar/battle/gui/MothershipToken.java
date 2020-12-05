package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class MothershipToken extends Token implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private final Player player;

    public MothershipToken(Player player, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(player.getMs(), x, y, battleStage, tokenMenu);
        setupSimpleBox2(x, y, getHeight(), getWidth());
        this.player = player;
        player.getMs().setToken(this);
    }

    public void draw(Batch batch) {
        super.draw(batch);
        //draws DropTarget SimpleBox2
        //if (DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getDebugRenderer(), batch); }
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
