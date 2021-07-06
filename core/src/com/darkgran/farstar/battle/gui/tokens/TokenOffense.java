package com.darkgran.farstar.battle.gui.tokens;

public class TokenOffense extends TokenPart {
    public TokenOffense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getCardInfo().getOffense());
    }
}
