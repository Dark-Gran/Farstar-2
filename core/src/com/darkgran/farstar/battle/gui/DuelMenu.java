package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.DuelManager;

public abstract class DuelMenu {
    private final DuelManager duelManager;

    public DuelMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        duelManager.receiveDuelMenu(this);
    }

    public void draw(Batch batch) {
        //TODO
    }
}
