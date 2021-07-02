package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.PB2Drawer;
import com.darkgran.farstar.util.SimpleBox2;

public class JunkButton extends PB2Drawer implements DropTarget {

    public JunkButton(float x, float y, BattleStage battleStage, Player player) {
        super(x, y, battleStage, player);
        setupSimpleBox2(x, y, TokenType.JUNK.getWidth(), TokenType.JUNK.getHeight());
    }

    @Override
    public SimpleBox2 getSimpleBox2() {
        return this;
    }
}
