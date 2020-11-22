package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;

public class GUI1v1 implements GUI {
    private final ResourceMeter resourceMeter1;
    private final ResourceMeter resourceMeter2;
    private final MothershipSlot mothershipSlot1;
    //private final MothershipSlot mothershipSlot2;

    public GUI1v1(Farstar game, Viewport viewport, Player player1, Player player2) {
        resourceMeter1 = new ResourceMeter(player1, true, Farstar.STAGE_WIDTH, 0f);
        resourceMeter2 = new ResourceMeter(player2, false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        mothershipSlot1 = new MothershipSlot(player1.getMs(), Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT/2);
        //mothershipSlot2 = new MothershipSlot(player2.getMs(), 0, 0);
    }

    @Override
    public void drawGUI(float delta, Batch batch) {
        resourceMeter2.draw(batch);
        resourceMeter1.draw(batch);
        mothershipSlot1.draw(batch);
        //mothershipSlot2.draw(batch);
    }

}
