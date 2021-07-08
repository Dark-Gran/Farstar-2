package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.TechType;
import com.darkgran.farstar.battle.players.cards.Card;

public class TokenOffense extends TokenPart {
    public TokenOffense(String fontPath, Card card, TokenType tokenType, boolean noPics) {
        super(fontPath, card, tokenType, noPics);
    }

    @Override
    public boolean isEnabled() {
        return !getContent().equals("0") || !TechType.isInferior(getCard().getCardInfo().getOffenseType());
    }

    @Override
    public String getContent() {
        return Integer.toString(getCard().getCardInfo().getOffense());
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad((Texture) Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getTypePad(getCard().getCardInfo().getOffenseType(), tokenType)));
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
