package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.battle.players.BattleCard;

public class TokenZoom1v1 extends TokenZoom {

    public TokenZoom1v1(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, int counterCap) {
        super(battleCard, x, y, battleStage, cardListMenu, counterCap);
    }

    @Override
    public void shiftPosition() {
        if (getTargetXY() != null) {
            float newX = getTargetXY().getX()+getTargetType().getWidth()+5f;
            float newY = getTargetXY().getY();
            switch (getTargetType()) {
                case SUPPORT:
                case MS:
                    newY += (getCard().getPlayer().getBattleID() == 1) ? getTargetType().getHeight()*0.25f : -getCardPic().getHeight()*0.8f;
                    break;
                case FLEET:
                    newY += getTargetType().getHeight()/2-getCardPic().getHeight() * ((getCard().getPlayer().getBattleID() == 1) ? 0.5f : 0.6f);
                    break;
                case YARD:
                    newX += 5f;
                    newY = (getCard().getPlayer().getBattleID() == 1) ? Farstar.STAGE_HEIGHT*0.15f : Farstar.STAGE_HEIGHT*0.4f;
                    break;
                case HAND:
                    newX -= getTargetType().getWidth()/2f+getCardPic().getWidth()/2f;
                    newY = (getCard().getPlayer().getBattleID() == 1) ? 0f : Farstar.STAGE_HEIGHT-getCardPic().getHeight();
                    break;
                case JUNK:
                    newX = getTargetXY().getX()-getTargetType().getWidth()*1.5f-5f;
                    newY -= (getCard().getPlayer().getBattleID() == 1) ? 0f : getCardPic().getHeight()-TokenType.JUNK.getWidth()/2f;
                    break;
            }
            setPosition(newX, newY);
        }
    }

}
