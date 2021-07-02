package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.DuelManager;

public class DuelMenu1v1 extends DuelMenu {
    public final DuelOK duelButton1;
    public final DuelOK duelButton2;

    public DuelMenu1v1(DuelManager duelManager) {
        super(duelManager);
        duelButton1 = new DuelOK(Farstar.ASSET_LIBRARY.getAssetManager().get("images/duel.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/duelO.png"), duelManager);
        duelButton2 = new DuelOK(Farstar.ASSET_LIBRARY.getAssetManager().get("images/duel.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/duelO.png"), duelManager);
        duelButton1.setPosition(Farstar.STAGE_WIDTH*0.6f, Farstar.STAGE_HEIGHT*0.28f);
        duelButton2.setPosition(Farstar.STAGE_WIDTH*0.6f, Farstar.STAGE_HEIGHT*0.69f);
        getCancelButton().setPosition(Farstar.STAGE_WIDTH*0.75f, Farstar.STAGE_HEIGHT*0.28f);
    }

    @Override
    public void removeAllOKs() {
        removeOK(duelButton1);
        removeOK(duelButton2);
    }

    public DuelOK getDuelButton1() { return duelButton1; }

    public DuelOK getDuelButton2() { return duelButton2; }

}
