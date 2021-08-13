package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.gui.AssetLibrary;

import static com.darkgran.farstar.Farstar.STAGE_HEIGHT;
import static com.darkgran.farstar.Farstar.STAGE_WIDTH;

public class CombatMenu1V1 extends CombatMenu {
    public final CombatOK duelButton1;
    public final CombatOK duelButton2;

    public CombatMenu1V1(CombatManager combatManager) {
        super(combatManager);
        duelButton1 = new CombatOK(AssetLibrary.getInstance().getAtlasRegion("tacticalOK"), AssetLibrary.getInstance().getAtlasRegion("tacticalOKO"), AssetLibrary.getInstance().getAtlasRegion("tacticalOKC"), AssetLibrary.getInstance().getAtlasRegion("tacticalOKCO"), combatManager);
        duelButton2 = new CombatOK(AssetLibrary.getInstance().getAtlasRegion("tacticalOK"), AssetLibrary.getInstance().getAtlasRegion("tacticalOKO"), AssetLibrary.getInstance().getAtlasRegion("tacticalOKC"), AssetLibrary.getInstance().getAtlasRegion("tacticalOKCO"), combatManager);
        duelButton1.setPosition(STAGE_WIDTH*0.6f, STAGE_HEIGHT*0.28f);
        duelButton2.setPosition(STAGE_WIDTH*0.6f, STAGE_HEIGHT*0.69f);
    }

    @Override
    public void removeAllOKs() {
        removeOK(duelButton1);
        removeOK(duelButton2);
    }

    public CombatOK getDuelButton1() { return duelButton1; }

    public CombatOK getDuelButton2() { return duelButton2; }

    @Override
    public void dispose() {
        super.dispose();
        removeAllOKs();
        duelButton1.dispose();
        duelButton2.dispose();
    }
}
