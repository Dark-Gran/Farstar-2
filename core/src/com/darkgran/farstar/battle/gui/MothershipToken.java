package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

public class MothershipToken extends Token implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private final Player player;

    public MothershipToken(Player player, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(player.getMs(), x, y, battleStage, cardListMenu);
        setupSimpleBox2(x, y, getHeight(), getWidth());
        this.player = player;
        player.getMs().setToken(this);
    }

    public void draw(Batch batch) {
        super.draw(batch);
        //Draws DropTarget SimpleBox2
        //if (DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getDebugRenderer(), batch); }
    }

    @Override
    public void setupListener() {
        super.setupListener();
        this.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(getThis(), getPlayer());
                return false;
            }
        });
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
