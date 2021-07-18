package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;

public class TableStage extends ListeningStage {
    private final Texture empty = Farstar.ASSET_LIBRARY.get("images/empty.png");
    private final Texture exit = Farstar.ASSET_LIBRARY.get("images/exit.png");
    private final Texture quality = Farstar.ASSET_LIBRARY.get("images/quality.png");
    private final Texture fs = Farstar.ASSET_LIBRARY.get("images/fs.png");
    private final Texture sound = Farstar.ASSET_LIBRARY.get("images/sound.png");
    private final Texture logout = Farstar.ASSET_LIBRARY.get("images/logout.png");
    private final Texture friends = Farstar.ASSET_LIBRARY.get("images/friends.png");
    private final Texture table = Farstar.ASSET_LIBRARY.get("images/tableMain_1920.png");
    private final Texture space = Farstar.ASSET_LIBRARY.get("images/Space_1920.png");

    private final ActorButton exitButton = new ActorButton(empty, empty, exit){
        @Override
        public void clicked() {
            getGame().getSuperScreen().userEscape();
        }
    };
    private final ActorButton fsButton = new ActorButton(empty, empty, fs){
        @Override
        public void clicked() {
            SuperScreen.switchFullscreen();
        }
    };
    private final ActorButton soundButton = new ActorButton(empty, empty, sound){
        @Override
        public void clicked() {
            getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Sound Not Available.", 3);
        }
    };
    private final ActorButton logoutButton = new ActorButton(empty, empty, logout){
        @Override
        public void clicked() {
            getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Server Not Available.", 3);
        }
    };
    private final ActorButton qualityButton = new ActorButton(empty, empty, quality){
        @Override
        public void clicked() {
            getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Settings Not Available.", 3);
        }
    };
    private final ActorButton friendsButton = new ActorButton(empty, empty, friends){
        @Override
        public void clicked() {
            getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Contacts Not Available.", 3);
        }
    };

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
    }

    public void drawBackground(SpriteBatch batch, boolean tableEnabled) {
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(space, (float) (Farstar.STAGE_WIDTH / 2 - space.getWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - space.getHeight() / 2));
        if (tableEnabled) { batch.draw(table, (float) (Farstar.STAGE_WIDTH / 2 - table.getWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - table.getHeight() / 2)); }
        batch.end();
    }

    public void enableButtons(boolean enable) {
        exitButton.setDisabled(!enable);
        qualityButton.setDisabled(!enable);
        fsButton.setDisabled(!enable);
        soundButton.setDisabled(!enable);
        logoutButton.setDisabled(!enable);
        friendsButton.setDisabled(!enable);
    }

    @Override
    public void dispose() {
        exitButton.dispose();
        qualityButton.dispose();
        fsButton.dispose();
        soundButton.dispose();
        logoutButton.dispose();
        friendsButton.dispose();
        super.dispose();
    }

}
