package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.DuelManager;

public class DuelMenu1v1 extends DuelMenu {
    public final ImageButton duelButton1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(getDuel())));
    public final ImageButton duelButton2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(getDuel())));

    public DuelMenu1v1(DuelManager duelManager) {
        super(duelManager);
        duelButton1.setBounds(Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        duelButton2.setBounds(Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT*4/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
    }

    @Override
    public void addActors() {
        getBattleStage().addActor(duelButton1);
        getBattleStage().addActor(duelButton2);
    }

    @Override
    public void removeActors() {
        duelButton1.remove();
        duelButton2.remove();
    }


}
