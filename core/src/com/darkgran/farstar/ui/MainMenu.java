package com.darkgran.farstar.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleScreen;

public class MainMenu extends ListeningMenu {
    private final Texture start = new Texture("start.png");
    private final ImageButton startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(start)));

    public MainMenu(final Farstar game, Viewport viewport) {
        super(game, viewport);
        startButton.setPosition((float) (Farstar.STAGE_WIDTH/2-start.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-start.getHeight()/2));
        this.addActor(startButton);
        setupListeners();
    }

    @Override
    public void setupListeners() {
        startButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                final TableMenu tableMenu =  game.getSuperScreen().getTableMenu();
                game.getScreen().dispose();
                game.setScreen(new BattleScreen(game, tableMenu));
                //startButton.removeListener(this);
            }
        });
    }

    @Override
    public void dispose() {
        start.dispose();
        super.dispose();
    }
}
