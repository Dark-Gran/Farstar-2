package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.Farstar;

public class TokenDefense extends TokenPart {
    public TokenDefense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public boolean isEnabled() { //todo
        return super.isEnabled();
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getHealth());
    }

    @Override
    public void setPad() {
        setPad(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getTypePad(getToken().getCard().getCardInfo().getDefenseType(), getToken().getTokenType())));
    }

    @Override
    public void setupOffset() {
        setOffsetY(1f+getPad().getHeight()*0.02f);
    }

}
