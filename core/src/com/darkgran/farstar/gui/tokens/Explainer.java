package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.cards.AbilityStarter;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.cards.Effect;
import com.darkgran.farstar.battle.players.BattleCard;

public class Explainer extends TextInTheBox {
    private final String italicPath = "fonts/explainerI.fnt";
    private String explanation = "";
    private String flavour = "";
    private float flavourOffsetY = 0f;

    public Explainer() {
        super(ColorPalette.LIGHT, ColorPalette.changeAlpha(ColorPalette.DARK, 0.6f), "fonts/explainerN.fnt", "", false);
        setWrapWidth(400f);
        setWrap(true);
    }

    public void setShiftedPosition(float x, float y) {
        this.x = (x+TokenType.PRINT.getWidth()*1.1f);
        this.y = (y+TokenType.PRINT.getHeight()*0.95f);
        if (this.x+this.getSimpleBox().getWidth() > Farstar.STAGE_WIDTH-130f) {
            this.x = x - this.getSimpleBox().getWidth();
        }
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        setupBox();
    }

    private void setupBox() {
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), explanation, getWrapWidth(), getWrap());
        if (textWH.x < getWrapWidth()) {
            x = (x + (getWrapWidth() - textWH.x)/2);
        }
        BitmapFont font = AssetLibrary.getInstance().getAssetManager().get(italicPath, BitmapFont.class);
        flavourOffsetY = textWH.y - ((flavour.equals("") ? 0 : font.getLineHeight()));
        setupBox(x, y, getWrapWidth()+40f, textWH.y+40f+((flavour.equals("") ? 0 : font.getLineHeight()*2)));
        centralizeBox();
        //getSimpleBox().setY(getSimpleBox().y-1f);
    }

    public void refreshText(BattleCard battleCard) {
        explanation = getMainContent(battleCard);
        flavour = getFlavourContent(battleCard);
        setText(explanation+flavour);
        setupBox();
    }

    @Override
    public void drawText(Batch batch) {
        if (!getFontPath().equals("")) {
            drawText(getFont(), batch, x, y, explanation, getFontColor());
            drawText(AssetLibrary.getInstance().getAssetManager().get(italicPath, BitmapFont.class), batch, x, y-flavourOffsetY, flavour, getFontColor());
        }
    }

    protected String getMainContent(BattleCard battleCard) {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        if (CardType.needsDefense(battleCard.getCardInfo().getCardType()) && battleCard.getDamage() > 0) {
            str.append("Current/Max. Shields: ").append(battleCard.getHealth()).append("/").append(battleCard.getCardInfo().getDefense()).append("                      \n");
            first = false;
        }
        String shipOrSupport = battleCard.getCardInfo().getCardType() == CardType.SUPPORT ? "support" : "ship";
        if (battleCard.isUsed()) {
            if (!first) { str.append("\n"); }
            else { first = false; }
            str.append("Disabled:\nThis ").append(shipOrSupport).append(" cannot attack or use abilities until next Turn.\n");
        }
        if (battleCard.getCardInfo().getCardType() == CardType.TACTIC || battleCard.getCardInfo().getCardType() == CardType.SUPPORT) {
            if (!first) { str.append("\n"); }
            else { first = false; }
            switch (battleCard.getCardInfo().getCardType()) {
                case TACTIC:
                    str.append("Tactic:\nTactics may be played both in Your Turn and in the Tactical Phase.\nUsually the best choice is to keep them for the Tactical Phase.\n");
                    break;
                case SUPPORT:
                    str.append("Support:\nYou can Deploy Supports next to Your Mothership.\n");
                    break;
            }
        }
        boolean FSpresent = false;
        boolean USEpresent = false;
        for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
            for (Effect effect : abilityInfo.getEffects()) {
                switch (effect.getEffectType()) {
                    case GUARD:
                        if (!first) { str.append("\n"); }
                        else { first = false; }
                        str.append("Guard:\nAttackers without Reach must always target Guards first.\n");
                        break;
                    case REACH:
                        if (!first) { str.append("\n"); }
                        else { first = false; }
                        str.append("Reach:\nAllows targeting over Guards, but only if the Reach is same as (or greater than) number of Guards.\n");
                        break;
                    case ARMOR:
                        if (!first) { str.append("\n"); }
                        else { first = false; }
                        str.append("Armor:\nReduces all incoming damage.\n");
                        break;
                    case FIRST_STRIKE:
                        FSpresent = true;
                        break;
                }
            }
            USEpresent = abilityInfo.getStarter() == AbilityStarter.USE;
            /*switch (abilityInfo.getStarter()) {
                case USE:
                    USEpresent = true;
                    break;
                case DEPLOY:
                    if (!first) { str.append("\n"); }
                    else { first = false; }
                    str.append("Does something when played.");
                    break;
            }*/
        }
        if (USEpresent) {
            if (!first) { str.append("\n"); }
            else { first = false; }
            str.append("Usable Ability:\nMay be used only once per Turn and only in Your Turn.\n");
        }
        if (!FSpresent) { FSpresent = AbilityManager.upgradesFirstStrike(battleCard); }
        if (FSpresent) {
            if (!first) { str.append("\n"); }
            //else { first = false; }
            str.append("First-Strike:\nShips with First-Strike always shoot first: if a ship is destroyed by First-Strike, it will not retaliate.\n");
        }
        return str.toString();
    }

    protected String getFlavourContent(BattleCard battleCard) {
        StringBuilder str = new StringBuilder();
        String flavour = battleCard.getCardInfo().getFlavour();
        if (!flavour.equals("")) {
            str.append("\n\"").append(flavour).append("\"");
        }
        return str.toString();
    }

}
