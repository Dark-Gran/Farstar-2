package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.AnchoredToken;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.Hand;
import com.darkgran.farstar.battle.players.Player;

public class HandMenu extends CardListMenu {
    private float actualX;
    private float actualY;

    public HandMenu(Hand hand, float x, float y, BattleStage battleStage, Player player) {
        super(hand, x, y, 0, 0, false, battleStage, player);
        actualX = x;
        actualY = y;
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
        centralize();
    }

    private void centralize() {
        float offset = TokenType.HAND.getWidth() * getTokens().size() * 0.5f;
        actualX = getX()-offset;
        refreshTokenPositions();
    }

    private void refreshTokenPositions() {
        for (int i = 0; i < getTokens().size(); i++) {
            getTokens().get(i).setPosition(actualX + getOffset()*i, actualY);
        }
    }

    @Override
    public void setupOffset() {
        setOffset(TokenType.FLEET.getWidth());
        if (isNegativeOffset()) { setOffset(getOffset()*-1); }
    }

    @Override
    protected void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            getTokens().add(new HandToken(getCardList().get(i), actualX + getOffset()*i, actualY, getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(Card card) {
        getTokens().add(new HandToken(card, actualX + getOffset()*getTokens().size(), actualY, getBattleStage(), this));
        centralize();
    }

}
