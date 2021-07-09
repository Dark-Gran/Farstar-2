package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.SupportToken;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.CardList;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.cards.Support;
import com.darkgran.farstar.util.SimpleBox2;

public class SupportMenu extends CardListMenu implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();

    public SupportMenu(CardList cardList, float x, float y, float tokensX, float tokensY, float width, float height, boolean negativeOffset, BattleStage battleStage, Player player) {
        super(cardList, x, y, tokensX, tokensY, negativeOffset, battleStage, player);
        setWidth(width);
        setHeight(height);
        setupSimpleBox2(x, y, width, height);
    }

    @Override
    public void setupOffset() {
        setOffset(TokenType.SUPPORT.getWidth()*1.02f);
    }

    @Override
    protected void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            int pos = translatePosition(i);
            getTokens().add(new SupportToken(getCardList().get(i), getTokensX() + getX() + getOffset()*pos + ((pos >= 3) ? 0 : 0), getTokensY() + getY(), getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(Card card) {
        if (card instanceof Support) {
            Support support = (Support) card;
            int pos = translatePosition(getTokens().size());
            SupportToken supportToken = new SupportToken(support, getTokensX() + getX() + getOffset()*pos + ((pos >= 3) ? TokenType.MS.getWidth()+13f : 0), getTokensY() + getY(), getBattleStage(), this);
            getTokens().add(supportToken);
            card.setToken(supportToken);
        }
    }

    public static int translatePosition(int position) {
        switch (position) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 1;
            case 3:
                return 4;
            case 4:
                return 0;
            case 5:
                return 5;
            default:
                return position;
        }
    }

    public static int unTranslatePosition(int position) {
        switch (position) {
            case 0:
                return 4;
            case 1:
                return 2;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 3;
            case 5:
                return 5;
            default:
                return position;
        }
    }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }
}
