package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.util.ActorButton;

public class TableStage extends ListeningStage {
    private final Texture empty = Farstar.ASSET_LIBRARY.getAssetManager().get("images/empty.png");
    private final Texture exit = Farstar.ASSET_LIBRARY.getAssetManager().get("images/exit.png");
    private final Texture quality = Farstar.ASSET_LIBRARY.getAssetManager().get("images/quality.png");
    private final Texture fs = Farstar.ASSET_LIBRARY.getAssetManager().get("images/fs.png");
    private final Texture sound = Farstar.ASSET_LIBRARY.getAssetManager().get("images/sound.png");
    private final Texture logout = Farstar.ASSET_LIBRARY.getAssetManager().get("images/logout.png");
    private final Texture friends = Farstar.ASSET_LIBRARY.getAssetManager().get("images/friends.png");
    private final Texture table = Farstar.ASSET_LIBRARY.getAssetManager().get("images/tableMain_1920.png");
    private final Texture space = Farstar.ASSET_LIBRARY.getAssetManager().get("images/Space_1920.png");

    private final TextureRegionDrawable emptyTRD = new TextureRegionDrawable(new TextureRegion(empty));

    private final ActorButton exitButton = new ActorButton(empty, empty, exit){
        @Override
        public void clicked() {
            getGame().getSuperScreen().userEscape();
        }
    };

    //private final ImageButton exitButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(exit)));
    private final ImageButton qualityButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(quality)));
    private final ImageButton fsButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(fs)));
    private final ImageButton soundButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(sound)));
    private final ImageButton logoutButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(logout)));
    private final ImageButton friendsButton = new ImageButton(emptyTRD, new TextureRegionDrawable(new TextureRegion(friends)));


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

    @Override
    protected void setupListeners() {
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
                getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Settings Not Available.", 3);
            }
        });
        soundButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Sound Not Available.", 3);
            }
        });
        logoutButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Server Not Available.", 3);
            }
        });
        friendsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Contacts Not Available.", 3);
            }
        });
    }

    protected void drawBackground(SpriteBatch batch) {
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(space, (float) (Farstar.STAGE_WIDTH / 2 - space.getWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - space.getHeight() / 2));
        batch.draw(table, (float) (Farstar.STAGE_WIDTH / 2 - table.getWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - table.getHeight() / 2));
        batch.end();
    }

    @Override
    public void dispose() {
        exitButton.dispose();
        super.dispose();
    }

}
