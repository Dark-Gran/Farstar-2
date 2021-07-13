package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class TargetingToken extends FakeToken {
    private Dragger dragger;
    private Texture aimPic;

    public TargetingToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, true);
        aimPic = Farstar.ASSET_LIBRARY.get("images/combat_aim.png");
        setWidth(aimPic.getWidth());
        setHeight(aimPic.getHeight());
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        batch.draw(aimPic, getX(), getY());
    }

    @Override
    public Dragger getDragger() {
        return dragger;
    }

    @Override
    public void setDragger(Dragger dragger) {
        this.dragger = dragger;
    }
}
