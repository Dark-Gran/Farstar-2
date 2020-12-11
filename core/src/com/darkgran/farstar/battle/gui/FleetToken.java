package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class FleetToken extends AnchoredToken {
    private FleetMenu fleetMenu;

    public FleetToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, FleetMenu fleetMenu) {
        super(card, x, y, battleStage, cardListMenu);
        this.fleetMenu = fleetMenu;
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), true));
        this.addListener(getDragger());
    }

    @Override
    public void click(int button) {
        getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(getThis(), getFleetMenu().getPlayer());
    }

    @Override
    public void destroy() {
        remove();
        if (getCardListMenu()!=null) {
            getCardListMenu().getTokens().remove(this);
            getCardListMenu().getCardList().getCards().remove(getCard());
        }
    }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public void setFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

}
