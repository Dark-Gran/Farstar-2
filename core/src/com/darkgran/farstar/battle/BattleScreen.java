package com.darkgran.farstar.battle;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.ui.TableMenu;

public class BattleScreen extends SuperScreen {

    public BattleScreen(final Farstar game, TableMenu tableMenu)
    {
        super(game);
        this.tableMenu = tableMenu;
        System.out.println("OK");
    }

    @Override
    public void drawScreen() {
        /*game.batch.begin();
        game.batch.setColor(1, 1, 1, 1);

        //game.batch.draw(start, (float) (Farstar.STAGE_WIDTH/2-start.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-start.getHeight()/2));

        game.batch.end();*/
    }

    @Override
    public void dispose() {

    }
}
