package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.Effect;
import com.darkgran.farstar.battle.players.abilities.EffectType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextInTheBox;
import com.darkgran.farstar.util.SimpleVector2;

public class Explainer extends TextInTheBox {

    public Explainer(Color fontColor, Color boxColor, String fontPath, String text, boolean noBox) {
        super(fontColor, boxColor, fontPath, text, noBox);
    }

    public Explainer() {
        super(ColorPalette.LIGHT, ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f), "fonts/bahnschrift30.fnt", "", false);
        setWrapWidth(200f);
        setWrap(true);
    }

    public void setShiftedPosition(float x, float y) {
        setX(x+TokenType.PRINT.getWidth()*1.1f);
        setY(y+TokenType.PRINT.getHeight()*0.95f);
    }

    private void setupBox() {
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), getText(), getWrapWidth(), getWrap());
        if (textWH.getX() < getWrapWidth()) {
            setX(getX() + (getWrapWidth() - textWH.getX())/2);
        }
        setupBox(getX(), getY(), getWrapWidth()+40f, textWH.getY()+40f);
        centralizeBox();
        //getSimpleBox().setY(getSimpleBox().getY()-1f);
    }

    public void refreshText(Card card) {
        setText(getExplanation(card));
        setupBox();
    }

    protected String getExplanation(Card card) {
        String str = "";
        switch (card.getCardInfo().getCardType()) {
            case TACTIC:
                //str += "Tactic.\n";
                break;
        }
        for (AbilityInfo abilityInfo : card.getCardInfo().getAbilities()) {
            for (Effect effect : abilityInfo.getEffects()) {
                switch (effect.getEffectType()) {
                    case GUARD:
                        //str += "Guard.\n";
                        break;
                    case REACH:
                        //str += "Reach.\n";
                        break;
                    case FIRST_STRIKE:
                        //str += "FirstStrike.\n";
                        break;
                }
            }
        }
        return str;
    }


}
