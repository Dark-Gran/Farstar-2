package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.cards.TechType;

public class TokenOffense extends TokenPart {
    public TokenOffense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public boolean isEnabled() {
        if (getToken().getTokenType() != TokenType.JUNK) {
            return !getContent().equals("0") || !TechType.isInferior(getToken().getCard().getCardInfo().getOffenseType());
        } else {
            return false;
        }
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getCardInfo().getOffense());
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad(Farstar.ASSET_LIBRARY.getAtlasRegion(Farstar.ASSET_LIBRARY.getTypePad(getToken().getCard().getCardInfo().getOffenseType(), tokenType)));
    }

    @Override
    public void resetContentState() {
        if (getToken().getCard().hasDamagedWeapons()) { //getToken().getCard().getCardInfo().getOffense() < getToken().getCard().getOriginalInfo().getOffense()
            setCurrentContentState(ContentState.DAMAGED);
        } else if (getToken().getCard().hasUpgradedWeapons()) { //getToken().getCard().getCardInfo().getOffense() > getToken().getCard().getOriginalInfo().getOffense()
            setCurrentContentState(ContentState.UPGRADED);
        } else {
            setCurrentContentState(ContentState.NORMAL);
        }
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        if (getPad() != null) {
            setOffsetX(getPad().getRegionWidth());
        }
    }

    @Override
    public void adjustTextWH() {
        if (getContent().equals("1")) {
            getTextWH().x = getTextWH().x+3f;
        }
        adjustTextWHByCurrentState();
    }

}
