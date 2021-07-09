package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

public class JunkButton extends Token implements DropTarget {
    private final Player player;

    public JunkButton(float x, float y, BattleStage battleStage, Player player) {
        super(null, x, y, battleStage, null, TokenType.JUNK, false, false);
        this.player = player;
        setupSimpleBox2(x, y, TokenType.JUNK.getWidth(), TokenType.JUNK.getHeight());
    }

    @Override
    public SimpleBox2 getSimpleBox2() {
        return new SimpleBox2(getX(), getY(), getWidth(), getHeight());
    }

    public Player getPlayer() {
        return player;
    }

}
