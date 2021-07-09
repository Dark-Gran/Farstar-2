package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.DropTarget;
import com.darkgran.farstar.battle.gui.SupportMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleBox2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class MothershipToken extends ClickToken implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private final SupportMenu supportMenu;

    public MothershipToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, SupportMenu supportMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.MS, false, true);
        this.supportMenu = supportMenu;
        setupSimpleBox2(x, y, getWidth(), getHeight());
        getCard().getPlayer().getMs().setToken(this);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        //Draws DropTarget SimpleBox2
        if (DEBUG_RENDER) { getBattleStage().getBattleScreen().drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getShapeRenderer(), batch); }
    }

    @Override
    public void click(int button) {
        super.click(button);
        if (button == 0) {
            getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(this, getCard().getPlayer());
        }
    }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }

    public SupportMenu getSupportMenu() {
        return supportMenu;
    }
}
