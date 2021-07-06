package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.Farstar;

public class TokenOffense extends TokenPart {
    public TokenOffense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getCardInfo().getOffense());
    }

    @Override
    public void setPad() {
        setPad(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getTypeTexture(getToken().getCard().getCardInfo().getOffenseType(), getToken().getTokenType())));
    }

}
