package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;


public class TableStage extends ListeningStage {
    private final Texture exit = new Texture("images/exit.png");
    private final ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(exit)));

    public TableStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        exitButton.setBounds(1, 1, (float) Farstar.STAGE_WIDTH/40,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(exitButton);
        setupListeners();
    }

    @Override
    protected void setupListeners() {
        exitButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

                if (getGame().getScreen().getClass() == MainScreen.class) {
                    System.exit(0);
                } else  {
                    final TableStage tableMenu =  getGame().getSuperScreen().getTableMenu();
                    getGame().getScreen().dispose();
                    getGame().setScreen(new MainScreen(getGame(), tableMenu));
                }

            }
        });
    }

    @Override
    public void dispose() {
        exitButton.removeListener(exitButton.getClickListener());
        exit.dispose();
        super.dispose();
    }
}
