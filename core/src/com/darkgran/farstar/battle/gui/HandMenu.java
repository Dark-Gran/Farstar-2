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
        float covering = 1f;
        if (getTokens().size() > 4) {
            switch (getTokens().size()) {
                case 5:
                    covering = 0.89f;
                    break;
                case 6:
                    covering = 0.72f;
                    break;
                case 7:
                    covering = 0.61f;
                    break;
                case 8:
                    covering = 0.53f;
                    break;
                default:
                    covering = 1f - (getTokens().size()-4)*0.2f;
                    break;
            }
        }
        float offset = ((TokenType.HAND.getWidth() * getTokens().size())-TokenType.HAND.getWidth()*(1f-covering)*(getTokens().size()-1)) * 0.5f;
        actualX = getX()-offset;
        refreshTokenPositions(covering);
    }

    private void refreshTokenPositions(float covering) {
        for (int i = 0; i < getTokens().size(); i++) {
            getTokens().get(i).setPosition(actualX + getOffset()*i*covering, actualY);
            ((AnchoredToken) getTokens().get(i)).setNewAnchor(actualX + getOffset()*i*covering, actualY);
        }
    }

    @Override
    public void setupOffset() {
        setOffset(TokenType.HAND.getWidth());
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
