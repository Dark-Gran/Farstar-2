package com.darkgran.farstar.battle.gui.tokens;

public class TokenDefense extends TokenTech {
    public TokenDefense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getHealth());
    }

    @Override
    public void setPad() {
        setPad(getTypeTexture(getToken().getCard().getCardInfo().getDefenseType()));
    }

}
