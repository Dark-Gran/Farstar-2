package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.AnchoredToken;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.Hand;
import com.darkgran.farstar.battle.players.Player;

public class HandMenu extends CardListMenu { //in-future: rearrangement of cards

    public HandMenu(Hand hand, float x, float y, BattleStage battleStage, Player player) {
        super(hand, x, y, false, battleStage, player);
    }

    @Override
    public void removeToken(Token token) {
        boolean found = false;
        for (int i = 0; i < getTokens().size(); i++) {
            if (!found && getTokens().get(i) == token) {
                getTokens().remove(token);
                i--;
                found = true;
            } else {
                Token nextToken = getTokens().get(i);
                nextToken.setX(getX() + getOffset()*i);
                if (nextToken instanceof AnchoredToken) { ((AnchoredToken) nextToken).setAnchorX(nextToken.getX()); }
            }
        }
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        setOffset(getOffset()*3/2);
    }

    @Override
    public void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            getTokens().add(new HandToken(getCardList().get(i), getX() + getOffset()*i, getY(), getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(Card card) {
        getTokens().add(new HandToken(card, getX() + getOffset()*getTokens().size(), getY(), getBattleStage(), this));
    }

}
