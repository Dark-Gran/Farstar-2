package com.darkgran.farstar.battle.gui.tokens;

public class TokenOffense extends TokenTech {
    public TokenOffense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getCardInfo().getOffense());
    }

    @Override
    public void setPad() {
        setPad(getTypeTexture(getToken().getCard().getCardInfo().getOffenseType()));
    }

}
