package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.mainscreen.MainScreen;


public class TableStage extends ListeningStage {
    private final Texture empty = new Texture("images/empty.png");
    private final Texture exit = new Texture("images/exit.png");
    private final Texture quality = new Texture("images/quality.png");
    private final Texture fs = new Texture("images/fs.png");
    private final Texture sound = new Texture("images/sound.png");
    private final Texture logout = new Texture("images/logout.png");
    private final Texture friends = new Texture("images/friends.png");
    private final TextureRegionDrawable emptyTRD = new TextureRegionDrawable(new TextureRegion(empty));
    private final ImageButton exitButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(exit)));
    private final ImageButton qualityButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(quality)));
    private final ImageButton fsButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(fs)));
    private final ImageButton soundButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(sound)));
    private final ImageButton logoutButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(logout)));
    private final ImageButton friendsButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(friends)));
    private final Texture table = new Texture("images/tableMain_1920.png"); //multiple resolutions will require texture atlas
    private final Texture space = new Texture("images/Space_1920.png");

    public TableStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        float rightSideX = Farstar.STAGE_WIDTH - Farstar.STAGE_WIDTH/33.5f;
        exitButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/15f);
        qualityButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/7.98f);
        fsButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/5.41f);
        soundButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/4.11f);
        float leftSideX = empty.getWidth()/30f;
        logoutButton.setPosition(leftSideX, Farstar.STAGE_HEIGHT/15f);
        friendsButton.setPosition(leftSideX, Farstar.STAGE_HEIGHT/7.98f);
        addActor(exitButton);
        addActor(qualityButton);
        addActor(fsButton);
        addActor(soundButton);
        addActor(logoutButton);
        addActor(friendsButton);
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
        fsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SuperScreen.switchFullscreen();
            }
        });
        qualityButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Quality-Button Clicked.");
            }
        });
        soundButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Sound-Button Clicked.");
            }
        });
        logoutButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("LogOut-Button Clicked.");
            }
        });
        friendsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Friends-Button Clicked.");
            }
        });
    }

    @Override
    public void dispose() {
        exitButton.removeListener(exitButton.getClickListener());
        empty.dispose();
        quality.dispose();
        exit.dispose();
        friends.dispose();
        logout.dispose();
        sound.dispose();
        fs.dispose();
        super.dispose();
    }

}
