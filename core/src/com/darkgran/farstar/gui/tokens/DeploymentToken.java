package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.battle.players.BattleCard;

public class DeploymentToken extends FakeToken {
    private Dragger dragger;

    public DeploymentToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, false);
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(CardType.isShip(battleCard.getCardInfo().getCardType()));
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
