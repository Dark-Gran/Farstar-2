package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.DuelManager;

public class DuelMenu {
    private final DuelManager duelManager;

    public DuelMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        duelManager.receiveDuelMenu(this);
    }
}
