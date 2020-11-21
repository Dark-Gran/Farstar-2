package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darkgran.farstar.match.BattleScreen;

public class MenuScreen extends SuperScreen {
    private final Stage menu;
    private final Texture start;
    private final ImageButton startButton;

    public MenuScreen(final Farstar game, final Texture exit) {
        super(game);
        this.exit = exit;
        start = new Texture("start.png");
        startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(start)));
        menu = new Stage(viewport);
        startButton.setPosition((float) (Farstar.STAGE_WIDTH/2-start.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-start.getHeight()/2));
        menu.addActor(startButton);
        setupListeners();
        Gdx.input.setInputProcessor(menu);
        Gdx.input.setCursorCatched(false);
    }

    private void setupListeners() {
        startButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.getScreen().dispose();
                game.setScreen(new BattleScreen(game, exit));
                //startButton.removeListener(this);
            }
        });
    }

    @Override
    public void drawScreen() {
        menu.act(Gdx.graphics.getDeltaTime());
        menu.draw();
    }

    @Override
    public void dispose() {
        start.dispose();
        menu.dispose();
    }
}
