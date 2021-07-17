package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.TechType;

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
        setPad((Texture) Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getTypePad(getToken().getCard().getCardInfo().getOffenseType(), tokenType)));
    }

    @Override
    public void resetContentState() {
        if (getToken().getCard().hasUpgradedWeapons()) {
            setCurrentContentState(ContentState.UPGRADED);
        } else if (getToken().getCard().hasDamagedWeapons()) {
            setCurrentContentState(ContentState.DAMAGED);
        } else {
            setCurrentContentState(ContentState.NORMAL);
        }
    }

    @Override
    public void setupOffset() {
        if (getPad() != null) {
            setOffsetX(getPad().getWidth());
        }
    }

    @Override
    public void adjustTextWH() {
        if (getContent().equals("1")) {
            getTextWH().setX(getTextWH().getX()+3f);
        }
        adjustTextWHByCurrentState();
    }

}
