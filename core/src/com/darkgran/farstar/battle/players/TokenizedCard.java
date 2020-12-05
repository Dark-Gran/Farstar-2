package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.Token;

public abstract class TokenizedCard extends Card {
    private Token token;

    public TokenizedCard(CardInfo cardInfo) { super(cardInfo); }

    public TokenizedCard() { super(); }

    public TokenizedCard(int id) { super(id); }

    public Token getToken() { return token; }

    public void setToken(Token token) { this.token = token; }
}
