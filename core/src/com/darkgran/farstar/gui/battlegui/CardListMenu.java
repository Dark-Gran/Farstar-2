package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.battle.players.CardList;

import java.util.ArrayList;

//is connected to a CardList and uses ArrayList (unlike FleetMenu)
public abstract class CardListMenu extends BaseMenu {
    private ArrayList<Token> tokens = new ArrayList<>();
    private final CardList cardList;
    private final float tokensX;
    private final float tokensY;

    public CardListMenu(CardList cardList, float x, float y, float tokensX, float tokensY, boolean negativeOffset, BattleStage battleStage, BattlePlayer battlePlayer) {
        super(x, y, negativeOffset, battleStage, battlePlayer);
        this.cardList = cardList;
        this.tokensX = tokensX;
        this.tokensY = tokensY;
        cardList.receiveCardListMenu(this);
        generateTokens();
    }

    protected void generateTokens() {
        tokens.clear();
        for (int i = 0; i < cardList.size(); i++) {
            tokens.add(new Token(cardList.get(i), tokensX + x + getOffset()*i, tokensY + y, getBattleStage(), this, TokenType.FLEET, false, true));
        }
    }

    public void generateNewToken(BattleCard battleCard) {
        tokens.add(new Token(battleCard, tokensX + x + getOffset()*tokens.size()-1, tokensY + y, getBattleStage(), this, TokenType.FLEET, false, true));
    }

    public void drawTokens(Batch batch) {
        if (getTokens().size() > 0) {
            for (int i = 0; i < getTokens().size(); i++) {
                getTokens().get(i).draw(batch);
            }
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
