package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.TechType;

public class TokenOffense extends TokenPart {
    public TokenOffense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public boolean isEnabled() {
        return !getContent().equals("0") || !TechType.isInferior(getToken().getCard().getCardInfo().getOffenseType());
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getCardInfo().getOffense());
    }

    @Override
    public void setPad() {
        setPad(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getTypePad(getToken().getCard().getCardInfo().getOffenseType(), getToken().getTokenType())));
    }

    @Override
    public void setupOffset() {
        setOffsetY(1f+getPad().getHeight()*0.02f);
        setOffsetX(getPad().getWidth());
    }

    @Override
    public void adjustTextWH() {
        if (getContent().equals("1")) {
            getTextWH().setX(getTextWH().getX()+3f);
        }
    }

}
