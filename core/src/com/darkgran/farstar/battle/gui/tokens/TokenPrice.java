package com.darkgran.farstar.battle.gui.tokens;

public class TokenPrice extends TokenPart {
    public TokenPrice(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public String getContent() {
        return getToken().getCard().getCardInfo().getEnergy() + ":" + getToken().getCard().getCardInfo().getMatter();
    }
}
