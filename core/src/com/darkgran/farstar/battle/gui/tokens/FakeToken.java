package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleVector2;

public abstract class FakeToken extends Token implements Dragging { //temporary token

    public FakeToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, boolean noPics) {
        super(card, x, y, battleStage, cardListMenu, TokenType.FAKE, noPics, false);
        setDragger(new Dragger(this));
        this.addListener(getDragger());
        setGlowState(card.getToken().getGlowState());
    }

}
