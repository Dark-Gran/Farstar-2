package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.gui.battlegui.AbilityPickerOption;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.cards.AbilityStarter;
import com.darkgran.farstar.cards.Effect;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.SimpleVector2;

/**
 * Actually a group of pads: Handles the entire top-left corner (ie. ability-pads too)
 */
public class TokenPrice extends TokenPart {
    private TextureRegion pad2;
    private SimpleVector2 textWH2;
    private TextureRegion useMark;
    private TextureRegion guardMark;
    private TextureRegion reachMark;
    private TextureRegion fsMark;
    private boolean showUseMark;
    private boolean showGuardMark;
    private boolean showReachMark;
    private boolean showFSMark;

    public TokenPrice(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public boolean isEnabled() {
        return getToken().getTokenType() != TokenType.JUNK;
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad(AssetLibrary.getInstance().getAtlasRegion(AssetLibrary.getInstance().addTokenTypeAcronym("padE-", tokenType, true)));
        pad2 = AssetLibrary.getInstance().getAtlasRegion(AssetLibrary.getInstance().addTokenTypeAcronym("padM-", tokenType, true));
        if (tokenType != TokenType.PRINT) { useMark = AssetLibrary.getInstance().getAtlasRegion(AssetLibrary.getInstance().addTokenTypeAcronym("abiU-", tokenType, true)); }
        if (tokenType == TokenType.FLEET) {
            fsMark = AssetLibrary.getInstance().getAtlasRegion("abiFS-F");
            guardMark = AssetLibrary.getInstance().getAtlasRegion("abiG-F");
            reachMark = AssetLibrary.getInstance().getAtlasRegion("abiR-F");
        }
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        setOffsetY(-getPad().getRegionHeight());
        setOffsetX(getPad().getRegionWidth());
        if (getToken().getTokenType() == TokenType.SUPPORT) {
            int res = getResource(true, TokenType.SUPPORT); //temp solution that works with the one Support in-game; in-future: needs to handle all parts of price (and atm does not use GetContent() - see draw()); also atm it does not support price >9 at all (text not centered)
            if (res == 0) {
                setTextOffsetX(-2.5f);
            } else if (res != 1) {
                setTextOffsetX(-1f);
            }
        }
    }

    @Override
    public void update() {
        String e = Integer.toString(getToken().getCard().getCardInfo().getEnergy());
        String m = Integer.toString(getToken().getCard().getCardInfo().getMatter());
        setTextWH(TextDrawer.getTextWH(getFont(), e));
        textWH2 = TextDrawer.getTextWH(getFont(), m);
        if (e.equals("1")) {
            getTextWH().x = getTextWH().x+3f;
        }
        if (m.equals("1")) {
            textWH2.x = textWH2.x+3f;
        }
        if (!(getToken() instanceof CardGFX)) {
            showGuardMark = false;
            showFSMark = false;
            showReachMark = false;
            showUseMark = AbilityManager.hasStarter(getToken().getCard(), AbilityStarter.USE);
            for (AbilityInfo abilityInfo : getToken().getCard().getCardInfo().getAbilities()) {
                if (abilityInfo.getStarter() == AbilityStarter.NONE) {
                    for (Effect effect : abilityInfo.getEffects()) {
                        switch (effect.getEffectType()) {
                            case GUARD:
                                showGuardMark = true;
                                break;
                            case FIRST_STRIKE:
                                showFSMark = true;
                                break;
                            case REACH:
                                showReachMark = true;
                                break;
                        }
                    }
                }
            }
        } else {
            showFSMark = false;
            showReachMark = false;
            showUseMark = false;
            showGuardMark = false;
        }
    }

    @Override
    public void adjustTextWH() {}

    @Override
    public void draw(Batch batch) {
        if (isEnabled()) {
            float x = this.x - getPad().getRegionWidth() + getOffsetX();
            if ((showUseMark && !isMouseOver()) || (showGuardMark && guardMark != null) || (showReachMark && reachMark != null) || (showFSMark && fsMark != null)) {
                if (showUseMark && useMark != null) {
                    batch.draw(useMark, x, this.y + getOffsetY());
                    x += getPad().getRegionWidth();
                }
                if (showGuardMark && guardMark != null) {
                    batch.draw(guardMark, x, this.y + getOffsetY());
                    x += getPad().getRegionWidth();
                }
                if (showReachMark && reachMark != null) {
                    batch.draw(reachMark, x, this.y + getOffsetY());
                    x += getPad().getRegionWidth();
                }
                if (showFSMark && fsMark != null) { batch.draw(fsMark, x, this.y + getOffsetY()); }
            } else {
                int E = getResource(true, getToken() instanceof AbilityPickerOption ? TokenType.SUPPORT : getToken().getTokenType());
                if (E != 0 || showUseMark) {
                    String e = Integer.toString(E);
                    batch.draw(getPad(), x, this.y + getOffsetY());
                    drawText(getFont(), batch, x + getPad().getRegionWidth() * 0.5f - getTextWH().x * 0.5f + getTextOffsetX(), this.y + getOffsetY() + getPad().getRegionHeight() * 0.5f + getTextWH().y * 0.5f + getTextOffsetY(), e, ColorPalette.BLACKISH);
                    x += getPad().getRegionWidth();
                }
                int M = getResource(false, getToken() instanceof AbilityPickerOption ? TokenType.SUPPORT : getToken().getTokenType());
                if (M != 0) {
                    String m = Integer.toString(M);
                    batch.draw(pad2, x, this.y + getOffsetY());
                    drawText(getFont(), batch, x + getPad().getRegionWidth() * 0.5f - textWH2.x * 0.5f + getTextOffsetX(), this.y + getOffsetY() + getPad().getRegionHeight() * 0.5f + textWH2.y * 0.5f + getTextOffsetY(), m, ColorPalette.BLACKISH);
                }
            }
        }
    }

    private int getResource(boolean energy, TokenType tokenType) {
        if (getToken().getCard().isMS() && tokenType == TokenType.PRINT) {
            tokenType = TokenType.MS;
        }
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
