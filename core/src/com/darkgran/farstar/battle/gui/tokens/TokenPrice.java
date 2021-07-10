package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.gui.AbilityPickerOption;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.abilities.EffectType;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPrice extends TokenPart {
    private Texture pad2;
    private SimpleVector2 textWH2;
    private Texture useMark;
    private Texture guardMark;
    private boolean showUseMark;
    private boolean showGuardMark;

    public TokenPrice(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public boolean isEnabled() {
        return getToken().getTokenType() != TokenType.JUNK;
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad((Texture) Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padE", tokenType, true)+".png"));
        pad2 = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padM", tokenType, true)+".png");
        if (tokenType != TokenType.PRINT) { useMark = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/abiU", tokenType, true)+".png"); }
        if (tokenType == TokenType.FLEET) { guardMark = Farstar.ASSET_LIBRARY.get("images/tokens/abiG_F.png"); }
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
        showUseMark = AbilityManager.hasStarter(getToken().getCard(), AbilityStarter.USE);
        showGuardMark = AbilityManager.hasAttribute(getToken().getCard(), EffectType.GUARD);
    }

    @Override
    public void adjustTextWH() {}

    @Override
    public void draw(Batch batch) {
        if (isEnabled()) {
            float x = getX() - getPad().getWidth() + getOffsetX();
            if ((showUseMark && !isMouseOver()) || (showGuardMark && guardMark != null)) {
                if (showUseMark && useMark != null) {
                    batch.draw(useMark, x, getY() + getOffsetY());
                    x += getPad().getWidth();
                }
                if (showGuardMark && guardMark != null) { batch.draw(guardMark, x, getY() + getOffsetY()); }
            } else {
                int E = getResource(true, getToken() instanceof AbilityPickerOption ? TokenType.SUPPORT : getToken().getTokenType());
                if (E != 0 || showUseMark) {
                    String e = Integer.toString(E);
                    batch.draw(getPad(), x, getY() + getOffsetY());
                    drawText(getFont(), batch, x + getPad().getWidth() * 0.5f - getTextWH().getX() * 0.5f, getY() + getOffsetY() + getPad().getHeight() * 0.5f + getTextWH().getY() * 0.48f, e);
                    x += getPad().getWidth();
                }
                int M = getResource(false, getToken() instanceof AbilityPickerOption ? TokenType.SUPPORT : getToken().getTokenType());
                if (M != 0) {
                    String m = Integer.toString(M);
                    batch.draw(pad2, x, getY() + getOffsetY());
                    drawText(getFont(), batch, x + getPad().getWidth() * 0.5f - textWH2.getX() * 0.5f, getY() + getOffsetY() + getPad().getHeight() * 0.5f + textWH2.getY() * 0.48f, m);
                }
            }
        }
    }

    private int getResource(boolean energy, TokenType tokenType) {
        switch (tokenType) {
            case YARD:
            case JUNK:
            case FAKE:
            case HAND:
            case PRINT:
                return energy ? getToken().getCard().getCardInfo().getEnergy() : getToken().getCard().getCardInfo().getMatter();
            default:
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

    private boolean isMouseOver() {
        if (getToken() instanceof ClickToken) {
            return ((ClickToken) getToken()).getClickListener().isOver();
        }
        return false;
    }

}
