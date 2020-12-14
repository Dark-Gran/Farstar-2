package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.Player;

public abstract class TokenizedCard extends Card {
    private Token token;

    public TokenizedCard(CardInfo cardInfo, Player player) { super(cardInfo, player); }

    public TokenizedCard() { super(); }

    public TokenizedCard(int id) { super(id); }

    public Token getToken() { return token; }

    public void setToken(Token token) { this.token = token; }
}
