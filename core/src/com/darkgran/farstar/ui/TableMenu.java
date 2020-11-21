package com.darkgran.farstar.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.MenuScreen;


public class TableMenu extends ListeningMenu {
    private final Texture exit = new Texture("exit.png");
    private final ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(exit)));

    public TableMenu(final Farstar game, Viewport viewport) {
        super(game, viewport);
        exitButton.setBounds(1, 1, (float) Farstar.STAGE_WIDTH/40,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(exitButton);
        setupListeners();
    }

    @Override
    public void setupListeners() {
        exitButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

                if (game.getScreen().getClass() == MenuScreen.class) {
                    System.exit(0); //asd
                } else  {
                    final TableMenu tableMenu =  game.getSuperScreen().getTableMenu();
                    game.getScreen().dispose();
                    game.setScreen(new MenuScreen(game, tableMenu));
                }

            }
        });
    }
}
