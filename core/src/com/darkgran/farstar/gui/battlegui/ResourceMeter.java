package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.battle.Battle;

public class ResourceMeter extends Actor implements JustFont {
    private final Battle battle;
    private final BattlePlayer battlePlayer;
    private final boolean onBottom;
    private String fontPath = "";
    private TextureRegion enePic = AssetLibrary.getInstance().getAtlasRegion("energy");
    private TextureRegion matPic = AssetLibrary.getInstance().getAtlasRegion("matter");
    private SimpleVector2 eneWH;
    private SimpleVector2 matWH;

    public ResourceMeter(Battle battle, BattlePlayer battlePlayer, boolean onBottom, float x, float y) {
        setFont("fonts/resMeter.fnt"); //bahnschrift semi-bold 60
        eneWH = TextDrawer.getTextWH(getFont(), "0 ");
        matWH = TextDrawer.getTextWH(getFont(), "0 ");
        this.battle = battle;
        this.battlePlayer = battlePlayer;
        this.onBottom = onBottom;
        String res = "E 123 M 456";
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), res);
        setWidth(textWH.x);
        setHeight(textWH.y);
        setX(x);
        setY(y);
        update();
        this.battlePlayer.setResourceMeter(this);
    }

    public void update() {
        String eneSub = "0";
        if (battlePlayer.getEnergy() > 9) {
            if (battlePlayer.getEnergy() > 99) {
                eneSub = "000";
            } else {
                eneSub = "00";
            }
        }
        String matSub = "0";
        if (battlePlayer.getMatter() > 9) {
            if (battlePlayer.getMatter() > 99) {
                matSub = "000";
            } else {
                matSub = "00";
            }
        }
        eneWH = TextDrawer.getTextWH(getFont(), eneSub+" ");
        matWH = TextDrawer.getTextWH(getFont(), matSub+" ");
    }

    public void draw(Batch batch) {
        getFont().setColor(ColorPalette.ENERGY);
        batch.draw(enePic, getX(), getY() - (onBottom ? 0f : eneWH.y) - getHeight()*0.49f);
        getFont().draw(batch, Integer.toString(battlePlayer.getEnergy()), getX()+enePic.getRegionWidth()*1.5f, onBottom ? getY()+getHeight() : getY());
        getFont().setColor(ColorPalette.MATTER);
        batch.draw(matPic, getX()+enePic.getRegionWidth()*1.5f+eneWH.x+10f, getY()-(onBottom ? 0f : eneWH.y) + getHeight()*0.02f);
        getFont().draw(batch, battlePlayer.getMatter()+" ", getX()+enePic.getRegionWidth()*1.5f+eneWH.x+10f+matPic.getRegionWidth()*1.3f, onBottom ? getY()+getHeight() : getY());
        getFont().setColor((ColorPalette.MAIN));
        getFont().draw(batch, " +"+getIncome(), getX()+enePic.getRegionWidth()*1.5f+eneWH.x+10f+matPic.getRegionWidth()*1.3f+matWH.x, onBottom ? getY()+getHeight() : getY());
        getFont().setColor(Color.WHITE);
    }

    private String getIncome() { //might need update for other mods (ie. ResourceMeter x ResourceMeter1v1?)
        int income = battle.getRoundManager().getIncome();
        if (battlePlayer == battle.getRoundManager().getStartingPlayer() || (battlePlayer != battle.getRoundManager().getStartingPlayer() && battlePlayer == battle.getWhoseTurn())) {
            income += 1;
        }
        income = battle.getRoundManager().capIncome(income);
        return Integer.toString(income);
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }
}
