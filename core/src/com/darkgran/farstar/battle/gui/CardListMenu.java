package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardList;
import com.darkgran.farstar.battle.players.Player;

import java.util.ArrayList;

//is connected to a CardList and uses ArrayList (unlike FleetMenu)
public abstract class CardListMenu extends BaseMenu {
    private ArrayList<Token> tokens = new ArrayList<>();
    private final CardList cardList;

    public CardListMenu(CardList cardList, float x, float y, boolean negativeOffset, BattleStage battleStage, Player player) {
        super(x, y, negativeOffset, battleStage, player);
        this.cardList = cardList;
        cardList.receiveCardListMenu(this);
        generateTokens();
    }

    public void generateTokens() {
        tokens.clear();
        for (int i = 0; i < cardList.getCards().size(); i++) {
            tokens.add(new Token(cardList.getCards().get(i), getX() + getOffset()*i, getY(), getBattleStage(), this));
        }
    }

    public void generateNewToken(Card card) {
        tokens.add(new Token(card, getX() + getOffset()*tokens.size()-1, getY(), getBattleStage(), this));
    }

    public void removeToken(Token token) {
        tokens.remove(token);
    }

    public ArrayList<Token> getTokens() { return tokens; }

    public void setTokens(ArrayList<Token> tokens) { this.tokens = tokens; }

    public CardList getCardList() { return cardList; }

}
