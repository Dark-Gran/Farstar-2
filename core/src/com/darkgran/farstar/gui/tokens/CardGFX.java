package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.darkgran.farstar.cards.Card;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.cards.CardCulture;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.gui.TextDrawer;

public interface CardGFX extends TextDrawer {
    float PORTRAIT_OFFSET_Y = -15f;

    void setCardPic(TextureRegion texture);
    TextureRegion getCardPic();
    Card getCard();
    TokenType getTokenType();
    boolean isBackside();
    BitmapFont getNameFont(TokenType tokenType);
    BitmapFont getTierFont(TokenType tokenType);
    BitmapFont getDescFont(TokenType tokenType, boolean bold);

    @Override
    default float getWrapWidth() { return getCardPic().getRegionWidth(); }
    @Override
    default boolean getWrap() { return true; }

    default void resetCardGFX(CardCulture culture, TokenType tokenType) {
        if (tokenType != null) {
            if (isBackside()) {
                setCardPic(AssetLibrary.getInstance().getAtlasRegion(AssetLibrary.getInstance().addTokenTypeAcronym("cardB-", tokenType, false)));
            } else {
                setCardPic(AssetLibrary.getInstance().getAtlasRegion(AssetLibrary.getInstance().addTokenTypeAcronym("card" + culture.getAcronym() + "-", tokenType, false)));
            }
        }
    }

    default void drawCardGFX(Batch batch, float x, float y, TokenType tokenType) { //in-future: optimize
        if (getCardPic() != null) {
            batch.draw(getCardPic(), x, y);
            if (getCard() != null && !isBackside()) {
                //Name
                drawText(getNameFont(tokenType), batch, x, y+getCardPic().getRegionHeight()*0.516f, getCardName(getCard()), Align.center);
                //Tier
                drawText(getTierFont(tokenType), batch, x, y+getCardPic().getRegionHeight() * (getCardType(getCard()) == CardType.MS ? 0.458f : 0.453f), getTierName(getCardTier(getCard()), getCardType(getCard())), Align.center);
                //Description
                BitmapFont boldFont = getDescFont(tokenType, true);
                BitmapFont normalFont = getDescFont(tokenType, false);
                float pos = y+getCardPic().getRegionHeight()*0.37f;
                String head = getCardDescriptionHead(getCard());
                if (!head.equals("")) {
                    drawText(
                            boldFont,
                            batch,
                            x,
                            pos,
                            head,
                            Align.center
                    );
                    pos -= TextDrawer.getTextWH(boldFont, getCardDescriptionHead(getCard())).y + (boldFont.getLineHeight() * 0.3f);
                }
                drawText(
                        normalFont,
                        batch,
                        x,
                        pos,
                        getCardDescriptionMain(getCard()),
                        Align.center
                );
                pos -= TextDrawer.getTextWH(normalFont, getCardDescriptionMain(getCard())).y+(boldFont.getLineHeight()*0.3f);
                drawText(
                        boldFont,
                        batch,
                        x,
                        pos,
                        getCardDescriptionFoot(getCard()),
                        Align.center
                );
            }
        }
    }

    static String getTierName(int tier, CardType cardType) {
        switch (cardType) {
            case ACTION:
                return "action";
            case TACTIC:
                return "tactic";
            case MS:
                return "mothership";
            case SUPPORT:
                return "support";
            case BLUEPRINT:
            case YARDPRINT:
                return "tier " + DecimalToRoman(tier) + ".";
        }
        return "n/a";
    }

    static String DecimalToRoman(int i) {
        i = MathUtils.clamp(i, 1, 5);
        String r = "n/a";
        switch (i) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }
        return r;
    }

    default String getCardName(Card card) {
        return card.getCardInfo().getName();
    }

    default String getCardDescriptionHead(Card card) {
        return card.getCardInfo().getDescriptionHead();
    }

    default String getCardDescriptionMain(Card card) {
        return card.getCardInfo().getDescription();
    }

    default String getCardDescriptionFoot(Card card) {
        return card.getCardInfo().getDescriptionFoot();
    }

    default int getCardTier(Card card) {
        return card.getCardInfo().getTier();
    }

    default CardType getCardType(Card card) {
        return card.getCardInfo().getCardType();
    }

    //Wrap unchangeable unless overridden
    @Override
    default void setWrap(boolean wrap) {}

    @Override
    default void setWrapWidth(float width) {}

}
