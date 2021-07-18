package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

public abstract class FakeToken extends Token implements Dragging { //temporary token

    public FakeToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, boolean noPics) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.FAKE, noPics, false);
        setDragger(new Dragger(this));
        this.addListener(getDragger());
        setGlowState(battleCard.getToken().getGlowState());
    }

}
