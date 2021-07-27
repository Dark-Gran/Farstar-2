package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.CombatManager;

public class CombatMenu1V1 extends CombatMenu {
    public final CombatOK duelButton1;
    public final CombatOK duelButton2;

    public CombatMenu1V1(CombatManager combatManager) {
        super(combatManager);
        duelButton1 = new CombatOK(Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOK"), Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOKO"), Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOKC"), Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOKCO"), combatManager);
        duelButton2 = new CombatOK(Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOK"), Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOKO"), Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOKC"), Farstar.ASSET_LIBRARY.getAtlasRegion("tacticalOKCO"), combatManager);
        duelButton1.setPosition(Farstar.STAGE_WIDTH*0.6f, Farstar.STAGE_HEIGHT*0.28f);
        duelButton2.setPosition(Farstar.STAGE_WIDTH*0.6f, Farstar.STAGE_HEIGHT*0.69f);
    }

    @Override
    public void removeAllOKs() {
        removeOK(duelButton1);
        removeOK(duelButton2);
    }

    public CombatOK getDuelButton1() { return duelButton1; }

    public CombatOK getDuelButton2() { return duelButton2; }

}
