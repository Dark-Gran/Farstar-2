package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;

public class BattleMenu1v1 extends BattleMenu {

    public BattleMenu1v1(Farstar game, Viewport viewport) {
        super(game, viewport);
        turnButton.setBounds(Farstar.STAGE_WIDTH*6/7, Farstar.STAGE_HEIGHT/2, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(turnButton);
        yardButton.setBounds(Farstar.STAGE_WIDTH*1/9, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(yardButton);
        setupListeners();
    }
}
