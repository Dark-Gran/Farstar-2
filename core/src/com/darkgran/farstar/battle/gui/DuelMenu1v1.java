package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.DuelManager;

public class DuelMenu1v1 extends DuelMenu {
    public final DuelOK duelButton1 = new DuelOK(new TextureRegionDrawable(new TextureRegion(getDuel())));
    public final DuelOK duelButton2 = new DuelOK(new TextureRegionDrawable(new TextureRegion(getDuel())));

    public DuelMenu1v1(DuelManager duelManager) {
        super(duelManager);
        duelButton1.setBounds(Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        duelButton2.setBounds(Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT*4/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        getCancelButton().setBounds(Farstar.STAGE_WIDTH*2/3, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
    }

    @Override
    public void removeAllOKs() {
        removeOK(duelButton1);
        removeOK(duelButton2);
    }

    public DuelOK getDuelButton1() { return duelButton1; }

    public DuelOK getDuelButton2() { return duelButton2; }

}
