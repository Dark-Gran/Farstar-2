package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPrice extends TokenPart {
    private Texture pad2;
    private SimpleVector2 textWH2;

    public TokenPrice(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public void setPad() {
        setPad(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padE", getToken().getTokenType())+".png"));
        pad2 = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padM", getToken().getTokenType())+".png");
    }

    @Override
    public void setupOffset() {
        setOffsetY(-1f-getPad().getHeight());
        setOffsetX(getPad().getWidth());
    }

    @Override
    public void update() {
        String e = Integer.toString(getToken().getCard().getCardInfo().getEnergy());
        String m = Integer.toString(getToken().getCard().getCardInfo().getMatter());
        setTextWH(TextDrawer.getTextWH(getFont(), e));
        textWH2 = TextDrawer.getTextWH(getFont(), m);
        if (e.equals("1")) {
            getTextWH().setX(getTextWH().getX()+3f);
        }
        if (m.equals("1")) {
            textWH2.setX(textWH2.getX()+3f);
        }
    }

    @Override
    public void adjustTextWH() {}

    @Override
    public void draw(Batch batch) {
        float x = getX()-getPad().getWidth()+getOffsetX();
        if (getToken().getCard().getCardInfo().getEnergy() != 0) {
            String e = Integer.toString(getToken().getCard().getCardInfo().getEnergy());
            batch.draw(getPad(), x, getY()+getOffsetY());
            drawText(getFont(), batch, x+getPad().getWidth()*0.5f-getTextWH().getX()*0.5f, getY()+getOffsetY()+getPad().getHeight()*0.5f+ getTextWH().getY()*0.48f, e);
            x += getPad().getWidth();
        }
        if (getToken().getCard().getCardInfo().getMatter() != 0) {
            String m = Integer.toString(getToken().getCard().getCardInfo().getMatter());
            batch.draw(pad2, x, getY()+getOffsetY());
            drawText(getFont(), batch, x+getPad().getWidth()*0.5f-textWH2.getX()*0.5f, getY()+getOffsetY()+getPad().getHeight()*0.5f+ textWH2.getY()*0.48f, m);
        }
    }
}
