package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class JunkButton extends BaseMenu implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();

    public JunkButton(float x, float y, BattleStage battleStage, Player player) {
        super(x, y, false, battleStage, player);
        setHeight(BattleStage.TOKEN_WIDTH/2);
        setWidth(BattleStage.TOKEN_WIDTH);
        setupSimpleBox2(x, y, getWidth(), getHeight());
    }

    public void draw(Batch batch) {
        if (DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getShapeRenderer(), batch); }
    }

    @Override
    public SimpleBox2 getSimpleBox2() {
        return simpleBox2;
    }
}
