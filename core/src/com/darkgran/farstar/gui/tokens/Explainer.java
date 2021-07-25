package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.cards.Effect;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextInTheBox;
import com.darkgran.farstar.util.SimpleVector2;

public class Explainer extends TextInTheBox {

    public Explainer(Color fontColor, Color boxColor, String fontPath, String text, boolean noBox) {
        super(fontColor, boxColor, fontPath, text, noBox);
    }

    public Explainer() {
        super(ColorPalette.LIGHT, ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f), "fonts/bahnschrift24.fnt", "", false);
        setWrapWidth(400f);
        setWrap(true);
    }

    public void setShiftedPosition(float x, float y) {
        this.x = (x+TokenType.PRINT.getWidth()*1.1f);
        this.y = (y+TokenType.PRINT.getHeight()*0.95f);
        if (this.x+this.getSimpleBox().getWidth() > Farstar.STAGE_WIDTH-130f) {
            this.x = x - this.getSimpleBox().getWidth();
        }
        setupBox();
    }

    private void setupBox() {
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), getText(), getWrapWidth(), getWrap());
        if (textWH.x < getWrapWidth()) {
            x = (x + (getWrapWidth() - textWH.x)/2);
        }
        setupBox(x, y, getWrapWidth()+30f, textWH.y+40f);
        centralizeBox();
        //getSimpleBox().setY(getSimpleBox().y-1f);
    }

    public void refreshText(BattleCard battleCard) {
        setText(getExplanation(battleCard));
        setupBox();
    }

    protected String getExplanation(BattleCard battleCard) {
        String str = "";
        boolean first = true;
        switch (battleCard.getCardInfo().getCardType()) {
            case TACTIC:
                str += "Tactic:\nTactics may be played both in Your Turn and in the Tactical Phase.\n";
                first = false;
                break;
        }
        boolean FSpresent = false;
        for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
            for (Effect effect : abilityInfo.getEffects()) {
                switch (effect.getEffectType()) {
                    case GUARD:
                        if (!first) { str += "\n"; }
                        else { first = false; }
                        str += "Guard:\nAlways must be targeted first by attackers without Reach.\n";
                        break;
                    case REACH:
                        if (!first) { str += "\n"; }
                        else { first = false; }
                        str += "Reach:\nAllows targeting over Guards, but only if the Reach is same as (or greater than) number of Guards.\n";
                        break;
                    case FIRST_STRIKE:
                        if (!first) { str += "\n"; }
                        else { first = false; }
                        FSpresent = true;
                        break;
                }
            }
            switch (abilityInfo.getStarter()) {
                case USE:
                    if (!first) { str += "\n"; }
                    else { first = false; }
                    str += "Usable Ability:\nMay be used only once per Turn.\n";
                    break;
                /*case DEPLOY:
                    if (!first) { str += "\n"; }
                    else { first = false; }
                    str += "Does something when played.";
                    break;*/
            }
        }
        if (!FSpresent) { FSpresent = AbilityManager.upgradesFirstStrike(battleCard); }
        if (FSpresent) {
            if (!first) { str += "\n"; }
            else { first = false; }
            str += "First-Strike:\nShips with First-Strike always shoot first: if the opposing ship is destroyed by First-Strike, it will not retaliate.\n";
        }
        return str;
    }


}
