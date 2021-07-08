package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPrice extends TokenPart {
    private Texture pad2;
    private SimpleVector2 textWH2;
    private Texture abiMark;

    public TokenPrice(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad((Texture) Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padE", tokenType, true)+".png"));
        pad2 = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padM", tokenType, true)+".png");
        if (tokenType != TokenType.PRINT) { abiMark = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/abiU", tokenType, true)+".png"); }
    }

    @Override
    public void setupOffset() {
        setOffsetY(-1f-getPad().getHeight());
        setOffsetX(1f+getPad().getWidth());
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
        if (isEnabled()) {
            float x = getX() - getPad().getWidth() + getOffsetX();
            boolean hasAbility = hasValidAbility(getToken());
            if (hasAbility && !isMouseOver()) {
                batch.draw(getPad(), x, getY() + getOffsetY());
                if (abiMark != null) { batch.draw(abiMark, x, getY() + getOffsetY()); }
            } else {
                int E = getResource(true);
                if (E != 0 || hasAbility) {
                    String e = Integer.toString(E);
                    batch.draw(getPad(), x, getY() + getOffsetY());
                    drawText(getFont(), batch, x + getPad().getWidth() * 0.5f - getTextWH().getX() * 0.5f, getY() + getOffsetY() + getPad().getHeight() * 0.5f + getTextWH().getY() * 0.48f, e);
                    x += getPad().getWidth();
                }
                int M = getResource(false);
                if (M != 0) {
                    String m = Integer.toString(M);
                    batch.draw(pad2, x, getY() + getOffsetY());
                    drawText(getFont(), batch, x + getPad().getWidth() * 0.5f - textWH2.getX() * 0.5f, getY() + getOffsetY() + getPad().getHeight() * 0.5f + textWH2.getY() * 0.48f, m);
                }
            }
        }
    }

    private int getResource(boolean energy) {
        switch (getToken().getTokenType()) {
            case YARD:
            case JUNK:
            case FAKE:
            case HAND:
                return energy ? getToken().getCard().getCardInfo().getEnergy() : getToken().getCard().getCardInfo().getMatter();
            default:
            case PRINT:
            case MS:
            case SUPPORT:
            case FLEET:
                for (AbilityInfo abilityInfo : getToken().getCard().getCardInfo().getAbilities()) {
                    if (abilityInfo.getStarter() == AbilityStarter.USE) {
                        return energy ? abilityInfo.getResourcePrice().getEnergy() : abilityInfo.getResourcePrice().getMatter();
                    }
                }
                return 0;
        }
    }

    private boolean hasValidAbility(Token token) {
        if (TokenType.isDeployed(token.getTokenType())) {
            for (AbilityInfo abilityInfo : token.getCard().getCardInfo().getAbilities()) {
                if (abilityInfo.getStarter() == AbilityStarter.USE) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMouseOver() {
        if (getToken() instanceof ClickToken) {
            return ((ClickToken) getToken()).getClickListener().isOver();
        }
        return false;
    }

}
