package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.battle.BattleScreen;

public class TableStage extends ListeningStage {
    private final Texture table = Farstar.ASSET_LIBRARY.get("images/tableMain-1920.png");
    private final TextureRegion empty = Farstar.ASSET_LIBRARY.getAtlasRegion("empty");
    private final TextureRegion exit = Farstar.ASSET_LIBRARY.getAtlasRegion("exit");
    private final TextureRegion quality = Farstar.ASSET_LIBRARY.getAtlasRegion("quality");
    private final TextureRegion fs = Farstar.ASSET_LIBRARY.getAtlasRegion("fs");
    private final TextureRegion sound = Farstar.ASSET_LIBRARY.getAtlasRegion("sound");
    private final TextureRegion logout = Farstar.ASSET_LIBRARY.getAtlasRegion("logout");
    private final TextureRegion friends = Farstar.ASSET_LIBRARY.getAtlasRegion("friends");
    private final TextureRegion space = Farstar.ASSET_LIBRARY.getAtlasRegion("Space-1920");
    private final TextureRegion net = Farstar.ASSET_LIBRARY.getAtlasRegion("net-1920"); //atm only for 1v1

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
            NotificationManager.newNotification(Notification.NotificationType.BOT_LEFT, "Sound Not Available.", 3);
        }
    };
    private final ActorButton logoutButton = new ActorButton(empty, empty, logout){
        @Override
        public void clicked() {
            NotificationManager.newNotification(Notification.NotificationType.BOT_LEFT, "Server Not Available.", 3);
        }
    };
    private final ActorButton qualityButton = new ActorButton(empty, empty, quality){
        @Override
        public void clicked() {
            NotificationManager.newNotification(Notification.NotificationType.BOT_LEFT, "Settings Not Available.", 3);
        }
    };
    private final ActorButton friendsButton = new ActorButton(empty, empty, friends){
        @Override
        public void clicked() {
            NotificationManager.newNotification(Notification.NotificationType.BOT_LEFT, "Contacts Not Available.", 3);
        }
    };

    private final SimpleVector2 tableCoords;

    public TableStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        float rightSideX = Farstar.STAGE_WIDTH - Farstar.STAGE_WIDTH/33.5f;
        exitButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/15f);
        qualityButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/7.98f);
        fsButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/5.41f);
        soundButton.setPosition(rightSideX, Farstar.STAGE_HEIGHT/4.11f);
        float leftSideX = empty.getRegionWidth()/30f;
        logoutButton.setPosition(leftSideX, Farstar.STAGE_HEIGHT/15f);
        friendsButton.setPosition(leftSideX, Farstar.STAGE_HEIGHT/7.98f);
        addActor(exitButton);
        addActor(qualityButton);
        addActor(fsButton);
        addActor(soundButton);
        addActor(logoutButton);
        addActor(friendsButton);
        tableCoords = new SimpleVector2(Farstar.STAGE_WIDTH / 2f - table.getWidth() / 2f, Farstar.STAGE_HEIGHT / 2f - table.getHeight() / 2f);
    }

    public void drawBackground(SpriteBatch batch, boolean tableEnabled, boolean netEnabled) {
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(space, 0, 0);
        if (netEnabled && getGame().getSuperScreen() instanceof BattleScreen) { batch.draw(net, 0, 0); }
        if (tableEnabled) { batch.draw(table, tableCoords.x, tableCoords.y); }
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
