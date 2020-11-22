package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.ListeningMenu;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.battle.BattleScreen;

public class BattleMenu extends ListeningMenu {
    private final Texture turn = new Texture("images/turn.png");
    public final ImageButton turnButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(turn)));
    private final Texture yard = new Texture("images/yard.png");
    public final ImageButton yardButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(yard)));

    public BattleMenu(final Farstar game, Viewport viewport) {
        super(game, viewport);
    }

    @Override
    public void setupListeners() {
        turnButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SuperScreen screen = getGame().getSuperScreen();
                if (screen instanceof BattleScreen) {
                    ((BattleScreen) screen).getBattle().getRoundManager().endTurn();
                }
            }
        });
        yardButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //TODO
            }
        });
    }

    @Override
    public void dispose() {
        turnButton.removeListener(turnButton.getClickListener());
        turn.dispose();
        yardButton.removeListener(yardButton.getClickListener());
        yard.dispose();
        super.dispose();
    }

}
