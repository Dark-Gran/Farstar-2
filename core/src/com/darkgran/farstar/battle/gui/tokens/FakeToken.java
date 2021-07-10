package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class FakeToken extends Token { //temporary token for targeted deployment from shipyard

    public FakeToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.FAKE, false, false);
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(true);
        setDragger(new Dragger(this));
        this.addListener(getDragger());
        setPicked(true);
    }

    @Override
    public void destroy() {
        super.destroy();
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(false);
    }
}
