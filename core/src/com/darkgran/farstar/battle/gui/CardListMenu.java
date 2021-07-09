package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.CardList;
import com.darkgran.farstar.battle.players.Player;

import java.util.ArrayList;

//is connected to a CardList and uses ArrayList (unlike FleetMenu)
public abstract class CardListMenu extends BaseMenu {
    private ArrayList<Token> tokens = new ArrayList<>();
    private final CardList cardList;
    private final float tokensX;
    private final float tokensY;

    public CardListMenu(CardList cardList, float x, float y, float tokensX, float tokensY, boolean negativeOffset, BattleStage battleStage, Player player) {
        super(x, y, negativeOffset, battleStage, player);
        this.cardList = cardList;
        this.tokensX = tokensX;
        this.tokensY = tokensY;
        cardList.receiveCardListMenu(this);
        generateTokens();
    }

    protected void generateTokens() {
        tokens.clear();
        for (int i = 0; i < cardList.size(); i++) {
            tokens.add(new Token(cardList.get(i), tokensX + getX() + getOffset()*i, tokensY + getY(), getBattleStage(), this, TokenType.FLEET, false, true));
        }
    }

    public void generateNewToken(Card card) {
        tokens.add(new Token(card, tokensX + getX() + getOffset()*tokens.size()-1, tokensY + getY(), getBattleStage(), this, TokenType.FLEET, false, true));
    }

    public void drawTokens(Batch batch) {
        for (int i = 0; i < getTokens().size(); i++) {
            getTokens().get(i).draw(batch);
        }
    }

    @Override
    public boolean isEmpty() { return cardList.size() <= 0; }

    public void removeToken(Token token) {
        tokens.remove(token);
    }

    public ArrayList<Token> getTokens() { return tokens; }

    public void setTokens(ArrayList<Token> tokens) { this.tokens = tokens; }

    public CardList getCardList() { return cardList; }

    public float getTokensX() {
        return tokensX;
    }

    public float getTokensY() {
        return tokensY;
    }
}
