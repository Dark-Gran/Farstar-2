package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.CardList;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

public class SupportMenu extends CardListMenu implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();

    public SupportMenu(CardList cardList, float x, float y, float width, float height, boolean negativeOffset, BattleStage battleStage, Player player) {
        super(cardList, x, y, negativeOffset, battleStage, player);
        setWidth(width);
        setHeight(height);
        setupSimpleBox2(x, y, height, width);
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        if (isNegativeOffset()) { setOffset(getOffset()*-1); } //switching back, SupportMenu handles offset differently
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
}
