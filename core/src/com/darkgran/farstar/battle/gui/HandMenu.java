package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.Hand;
import com.darkgran.farstar.battle.players.Player;

public class HandMenu extends CardListMenu {

    public HandMenu(Hand hand, float x, float y, BattleStage battleStage, Player player) {
        super(hand, x, y, false, battleStage, player);
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        setOffset(getOffset()*3/2);
    }

    @Override
    public void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().getCards().size(); i++) {
            getTokens().add(new HandToken(getCardList().getCards().get(i), getX() + getOffset()*i, getY(), getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(Card card) {
        getTokens().add(new HandToken(card, getX() + getOffset()*getTokens().size()-1, getY(), getBattleStage(), this));
    }

}
