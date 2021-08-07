package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.battle.players.BattleCard;

public class TokenZoom1v1 extends TokenZoom {

    public TokenZoom1v1(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, float counterCap) {
        super(battleCard, x, y, battleStage, cardListMenu, counterCap);
    }

    @Override
    public void shiftPosition() {
        if (getTargetXY() != null) {
            float newX = getTargetXY().x+getTargetType().getWidth()+5f;
            float newY = getTargetXY().y;
            switch (getTargetType()) {
                case SUPPORT:
                case MS:
                    newY += (getCard().getBattlePlayer().getBattleID() == 1) ? getTargetType().getHeight()*0.25f : -getCardPic().getRegionHeight()*0.75f;
                    break;
                case FLEET:
                    newY += getTargetType().getHeight()/2-getCardPic().getRegionHeight() * ((getCard().getBattlePlayer().getBattleID() == 1) ? 0.5f : 0.6f);
                    break;
                case YARD:
                    newX += 5f;
                    newY = (getCard().getBattlePlayer().getBattleID() == 1) ? Farstar.STAGE_HEIGHT*0.15f : Farstar.STAGE_HEIGHT*0.4f;
                    break;
                case HAND:
                    newX -= getTargetType().getWidth()/2f+getCardPic().getRegionWidth()/2f;
                    newY = (getCard().getBattlePlayer().getBattleID() == 1) ? 0f : Farstar.STAGE_HEIGHT-getCardPic().getRegionHeight();
                    break;
                case JUNK:
                    newY -= (getCard().getBattlePlayer().getBattleID() == 1) ? 0f : getCardPic().getRegionHeight()-TokenType.JUNK.getHeight()*3/4f;
                    break;
            }
            setPosition(newX, newY);
        }
    }

}
