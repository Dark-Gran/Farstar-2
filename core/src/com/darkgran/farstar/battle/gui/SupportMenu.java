package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.CardList;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

public class SupportMenu extends CardListMenu implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();

    public SupportMenu(CardList cardList, float x, float y, float width, float height, boolean negativeOffset, BattleStage battleStage, Player player) {
        super(cardList, x, y, negativeOffset, battleStage, player);
        setWidth(width);
        setHeight(height);
        setupSimpleBox2(x, y, height, width);
    }

    @Override
    public void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().getCards().size(); i++) {
            getTokens().add(new Token(getCardList().getCards().get(i), getX() + getOffset()*translatePosition(i), getY(), getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(Card card) {
        getTokens().add(new Token(card, getX() + getOffset()*translatePosition(getTokens().size()), getY(), getBattleStage(), this));
    }

    public static int translatePosition(int position) {
        switch (position) {
            case 0:
                return 2;
            case 1:
                return 4;
            case 2:
                return 1;
            case 3:
                return 5;
            case 4:
                return 0;
            case 5:
                return 6;
            default:
                return position+1;
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
            case 4:
                return 1;
            case 5:
                return 3;
            case 6:
                return 5;
            default:
                return position-1;
        }
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        if (isNegativeOffset()) { setOffset(getOffset()*-1); } //switching back, SupportMenu handles offset differently
    }

    @Override
    public void setupSimpleBox2(float x, float y, float height, float width) {
        simpleBox2.setX(x);
        simpleBox2.setY(y);
        simpleBox2.setHeight(height);
        simpleBox2.setWidth(width);
    }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }
}
