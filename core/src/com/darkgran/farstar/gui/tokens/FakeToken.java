package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.Gdx;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

public abstract class FakeToken extends Token implements Dragging { //temporary token

    public FakeToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, boolean noPics) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.FAKE, noPics, false);
        setPosition(Gdx.input.getX()-getWidth()/2, Farstar.STAGE_HEIGHT-(Gdx.input.getY()+getHeight()/2));
        setDragger(new Dragger(this));
        this.addListener(getDragger());
        setGlowState(battleCard.getToken().getGlowState());
    }

}
