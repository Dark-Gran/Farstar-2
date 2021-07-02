package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.PlayerBox2;
import com.darkgran.farstar.util.SimpleBox2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class JunkButton extends PlayerBox2 implements DropTarget {

    public JunkButton(float x, float y, BattleStage battleStage, Player player) {
        super(x, y, battleStage, player);
        setupSimpleBox2(x, y, TokenType.JUNK.getWidth(), TokenType.JUNK.getHeight());
    }

    public void draw(Batch batch) {
        if (DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getShapeRenderer(), batch); }
    }

    @Override
    public SimpleBox2 getSimpleBox2() {
        return this;
    }
}
