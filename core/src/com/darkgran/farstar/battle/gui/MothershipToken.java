package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.util.SimpleBox2;

public class MothershipToken extends ClickToken implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();

    public MothershipToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
        setupSimpleBox2(x, y, getHeight(), getWidth());
        getCard().getPlayer().getMs().setToken(this);
    }

    public void draw(Batch batch) {
        super.draw(batch);
        //Draws DropTarget SimpleBox2
        //if (BattleScreen.DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getDebugRenderer(), batch); }
    }

    @Override
    public void click(int button) {
        if (button == 0) {
            getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(getThis(), getCard().getPlayer());
        }
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
