package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.gui.ActorButton;

public class CombatMenu1V1 extends CombatMenu {
    public final CombatOK duelButton1;
    public final CombatOK duelButton2;

    public CombatMenu1V1(CombatManager combatManager) {
        super(combatManager);
        duelButton1 = new CombatOK(Farstar.ASSET_LIBRARY.get("images/tacticalOK.png"), Farstar.ASSET_LIBRARY.get("images/tacticalOKO.png"), Farstar.ASSET_LIBRARY.get("images/tacticalOKC.png"), Farstar.ASSET_LIBRARY.get("images/tacticalOKCO.png"), combatManager);
        duelButton2 = new CombatOK(Farstar.ASSET_LIBRARY.get("images/tacticalOK.png"), Farstar.ASSET_LIBRARY.get("images/tacticalOKO.png"), Farstar.ASSET_LIBRARY.get("images/tacticalOKC.png"), Farstar.ASSET_LIBRARY.get("images/tacticalOKCO.png"), combatManager);
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
