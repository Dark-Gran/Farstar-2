package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.Dragger;
import com.darkgran.farstar.gui.Dragging;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.SimpleVector2;

public abstract class FakeToken extends Token implements Dragging { //temporary token

    public FakeToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, boolean noPics) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.FAKE, noPics, false);
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        setPosition(coords.x-getWidth()/2f, Farstar.STAGE_HEIGHT-(coords.y+getHeight()/2f));
        setDragger(new Dragger(this));
        this.addListener(getDragger());
        setGlowState(battleCard.getToken().getGlowState());
    }

}
