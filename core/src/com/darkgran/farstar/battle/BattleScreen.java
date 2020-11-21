package com.darkgran.farstar.battle;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.ui.TableMenu;

public class BattleScreen extends SuperScreen {

    public BattleScreen(final Farstar game, TableMenu tableMenu)
    {
        super(game);
        setTableMenu(tableMenu);
        System.out.println("OK");
    }

    @Override
    public void drawScreen() {

    }

    @Override
    public void dispose() {

    }
}
