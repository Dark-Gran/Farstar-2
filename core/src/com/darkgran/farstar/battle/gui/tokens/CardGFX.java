package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.darkgran.farstar.AssetLibrary;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardCulture;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.gui.TextDrawer;

public interface CardGFX extends TextDrawer {
    float PORTRAIT_OFFSET_Y = -15f;

    void setCardPic(Texture texture);
    Texture getCardPic();
    Card getCard();
    TokenType getTokenType();

    @Override
    default float getWrapWidth() { return getCardPic().getWidth(); }
    @Override
    default boolean getWrap() { return true; }

    default void resetCardGFX(CardCulture culture, TokenType tokenType) {
        if (tokenType != null) {
            setCardPic(Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("images/tokens/card" + culture.getAcronym()+"_", tokenType, false) + ".png"));
        }
    }

    default void drawCardGFX(Batch batch, float x, float y, TokenType tokenType) {
        if (getCardPic() != null) {
            batch.draw(getCardPic(), x, y);
            if (getCard() != null) {
                drawText(getNameFont(tokenType), batch, x, y+getCardPic().getHeight()*0.518f, getCardName(getCard()), Align.center);
                drawText(getTierFont(tokenType), batch, x, y+getCardPic().getHeight()*0.452f, getTierName(getCardTier(getCard()), getCardType(getCard())), Align.center);
                drawText(getDescFont(tokenType), batch, x, y+getCardPic().getHeight()*0.37f, getCardDescription(getCard()), Align.center, ColorPalette.BLACK);
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
                return "mothership ";
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

    default BitmapFont getNameFont(TokenType tokenType) {
        return Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("fonts/orbitron_name", tokenType, false)+".fnt");
    }

    default BitmapFont getTierFont(TokenType tokenType) { //barlow Z>FK>F 26>20>18
        return Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("fonts/barlow_tier", tokenType, false)+".fnt");
    }
    default BitmapFont getDescFont(TokenType tokenType) {
        return Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("fonts/barlow_desc", tokenType, false)+".fnt");
    }

    default String getCardName(Card card) {
        return card.getCardInfo().getName();
    }

    default String getCardDescription(Card card) {
        return card.getCardInfo().getDescription();
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
