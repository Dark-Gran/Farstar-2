package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class SupportToken extends ClickToken {
    public SupportToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
    }

    @Override
    public void click(int button) {
        getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(getThis(), getCard().getPlayer());
    }

    @Override
    public void destroy() {
        remove();
        if (getCardListMenu()!=null) {
            getCardListMenu().getTokens().remove(this);
            getCardListMenu().getCardList().getCards().remove(getCard());
        }
    }
}
