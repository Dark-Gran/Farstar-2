package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class DeploymentToken extends FakeToken {
    private Dragger dragger;

    public DeploymentToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, false);
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(true);
    }

    @Override
    public void destroy() {
        getCard().getToken().setPicked(false);
        super.destroy();
        if (getCardListMenu() != null) { getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(false); }
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
