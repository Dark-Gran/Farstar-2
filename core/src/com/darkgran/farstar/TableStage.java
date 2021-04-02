package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;


public class TableStage extends ListeningStage {
    private final Texture exit = new Texture("images/exit.png");
    private final ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(exit)));
    private final Texture table = new Texture("images/tableMain_1920.png"); //multiple resolutions will require texture atlas
    private final Texture space = new Texture("images/Space_1920.png");

    public TableStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        exitButton.setBounds(Farstar.STAGE_WIDTH - Farstar.STAGE_WIDTH/24.5f, Farstar.STAGE_HEIGHT/14.5f, (float) (Farstar.STAGE_WIDTH/20), (float) (Farstar.STAGE_HEIGHT/20));
        addActor(exitButton);
        setupListeners();
    }

    protected void drawBackground(SpriteBatch batch) {
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(space, (float) (Farstar.STAGE_WIDTH / 2 - space.getWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - space.getHeight() / 2));
        batch.draw(table, (float) (Farstar.STAGE_WIDTH / 2 - table.getWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - table.getHeight() / 2));
        batch.end();
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
