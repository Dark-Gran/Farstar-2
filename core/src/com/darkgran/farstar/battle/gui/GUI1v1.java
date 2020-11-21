package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.Player;

public class GUI1v1 implements GUI {
    private final ResourceMeter resourceMeter1;
    private final ResourceMeter resourceMeter2;

    public GUI1v1(Player player1, Player player2) {
        resourceMeter1 = new ResourceMeter(player1, true, Farstar.STAGE_WIDTH, 0f);
        resourceMeter2 = new ResourceMeter(player2, false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
    }

    @Override
    public void drawGUI(Batch batch) {
        resourceMeter1.draw(batch);
        resourceMeter2.draw(batch);
    }

}
