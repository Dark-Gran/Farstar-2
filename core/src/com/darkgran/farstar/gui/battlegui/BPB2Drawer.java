package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.battle.players.BattlePlayer;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

/**
 * SimpleBox2 with battleStage+player references meant for drawing.
 * For simple objects (see BaseMenu subclasses for more complexity (eg. CardListMenu)).
 */
public class BPB2Drawer extends BattlePlayerBox2 {
    public BPB2Drawer(float x, float y, BattleStage battleStage, BattlePlayer battlePlayer) {
        super(x, y, battleStage, battlePlayer);
    }

    public void draw(Batch batch) {
        if (DEBUG_RENDER) { SuperScreen.drawDebugSimpleBox2(this, getBattleStage().getBattleScreen().getShapeRenderer(), batch); }
    }

}
