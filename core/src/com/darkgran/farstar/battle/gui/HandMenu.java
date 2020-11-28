package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.Hand;

public class HandMenu extends TokenMenu {

    public HandMenu(Hand hand, float x, float y, Stage stage) {
        super(hand, x, y, false, stage);
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
            getTokens().add(new HandCard(getCardList().getCards().get(i), getX() + getOffset()*i, getY(), getStage()));
        }
    }

    @Override
    public void generateNewToken(Card card) {
        getTokens().add(new HandCard(card, getX() + getOffset()*getTokens().size()-1, getY(), getStage()));
    }

}
