package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.Player;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

/**
 * SimpleBox2 with battleStage+player references meant for drawing.
 * For simple objects (see BaseMenu subclasses for more complexity (eg. CardListMenu)).
 */
public class PB2Drawer extends PlayerBox2 {
    public PB2Drawer(float x, float y, BattleStage battleStage, Player player) {
        super(x, y, battleStage, player);
    }

    public void draw(Batch batch) {
        if (DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(this, getBattleStage().getBattleScreen().getShapeRenderer(), batch); }
    }

}
