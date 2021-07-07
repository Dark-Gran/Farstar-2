package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.Farstar;

public class TokenPrice extends TokenPart {
    private Texture pad2;

    public TokenPrice(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public String getContent() {
        return getToken().getCard().getCardInfo().getEnergy() + ":" + getToken().getCard().getCardInfo().getMatter();
    }

    @Override
    public void setPad() {
        setPad(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padE", getToken().getTokenType())+".png"));
        pad2 = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padM", getToken().getTokenType())+".png");
    }

    @Override
    public void setupOffset() {
        setOffsetY(-getPad().getHeight());
        setOffsetX(getPad().getWidth());
    }

}
