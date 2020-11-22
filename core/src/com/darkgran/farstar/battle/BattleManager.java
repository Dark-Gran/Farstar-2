package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.GUI;

public interface BattleManager {
    final CardLibrary CARD_LIBRARY = new CardLibrary();

    void loadLibrary();
    GUI getGui();

}
