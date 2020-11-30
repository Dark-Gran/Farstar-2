package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;

public class DuelManager {
    private DuelMenu duelMenu;
    private boolean active = false;

    public void receiveDuelMenu(DuelMenu duelMenu) { this.duelMenu = duelMenu; }

    public DuelMenu getDuelMenu() { return duelMenu; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
