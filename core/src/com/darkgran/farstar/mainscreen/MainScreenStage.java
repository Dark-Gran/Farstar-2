package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleType;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.Battle1v1;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.PlayerFactory;

public class MainScreenStage extends ListeningStage {
    private final PlayerFactory playerFactory = new PlayerFactory();
    private final SimpleImage2 FSLogo;
    private final VersionInfo versionInfo = new VersionInfo((float) (Farstar.STAGE_WIDTH*0.85), (float) (Farstar.STAGE_HEIGHT*0.98), ColorPalette.MAIN);
    private final ActorButton solitaryButton = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("solitary"), Farstar.ASSET_LIBRARY.getAtlasRegion("solitaryO")){
        @Override
        public void clicked() { launchBattleScreen(BattleType.SOLITARY); }
    };
    private final ActorButton skirmishButton = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("skirmish"), Farstar.ASSET_LIBRARY.getAtlasRegion("skirmishO")){
        @Override
        public void clicked() { launchBattleScreen(BattleType.SKIRMISH); }
    };
    private final ActorButton simulationButton = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("sim"), Farstar.ASSET_LIBRARY.getAtlasRegion("simO")){
        @Override
        public void clicked() { launchBattleScreen(BattleType.SIMULATION); }
    };
    private final ActorButton webButton = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("web"), Farstar.ASSET_LIBRARY.getAtlasRegion("webO")){
        @Override
        public void clicked() {
            System.out.println("Opening Web-Browser.");
            Gdx.net.openURI("https://github.com/Dark-Gran/Farstar-2");
        }
    };
    private final SimpleImage2 otherModesPic;


    public MainScreenStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        TextureRegion tr = Farstar.ASSET_LIBRARY.getAtlasRegion("FSLogo");
        FSLogo = new SimpleImage2((float) (Farstar.STAGE_WIDTH/2-tr.getRegionWidth()/2), (float) (Farstar.STAGE_HEIGHT*0.8), tr);
        TextureRegion measureTexture = Farstar.ASSET_LIBRARY.getAtlasRegion("solitary");
        skirmishButton.setPosition(Farstar.STAGE_WIDTH/2f - skirmishButton.getWidth()/2, (float) (Farstar.STAGE_HEIGHT/2 - measureTexture.getRegionHeight()*0.5));
        tr = Farstar.ASSET_LIBRARY.getAtlasRegion("otherModes");
        float otherY = (float) (Farstar.STAGE_HEIGHT * 0.18);
        otherModesPic = new SimpleImage2((float) (Farstar.STAGE_WIDTH / 2 - tr.getRegionWidth() / 2), (float) (otherY - simulationButton.getHeight()*0.05), tr);
        solitaryButton.setPosition((float) (Farstar.STAGE_WIDTH/2 - measureTexture.getRegionWidth()/2), (float) (otherY - simulationButton.getHeight()*0.8));
        simulationButton.setPosition(Farstar.STAGE_WIDTH/2f - simulationButton.getWidth()/2, (float) (otherY - simulationButton.getHeight()*1.6));
        webButton.setPosition((float) (Farstar.STAGE_WIDTH*0.0725), (float) (Farstar.STAGE_HEIGHT*0.012));
        addActor(skirmishButton);
        addActor(solitaryButton);
        addActor(simulationButton);
        addActor(webButton);
    }

    public void enableMainButtons(boolean disable) {
        solitaryButton.setDisabled(disable);
        skirmishButton.setDisabled(disable);
        simulationButton.setDisabled(disable);
        if (disable) {
            solitaryButton.remove();
            skirmishButton.remove();
            simulationButton.remove();
        } else {
            addActor(solitaryButton);
            addActor(skirmishButton);
            addActor(simulationButton);
        }
    }

    private void launchBattleScreen(BattleType battleType) {
        System.out.println("Starting "+battleType.getName()+".");
        Battle battle = null;
        switch (battleType) {
            case SKIRMISH:
                battle = new Battle1v1(
                        playerFactory.getPlayer("LOCAL", 1, 0),
                        playerFactory.getPlayer("AUTO", 2, 15)
                );
                break;
            case SIMULATION:
                battle = new Battle1v1(
                        playerFactory.getPlayer("AUTO", 1, 0),
                        playerFactory.getPlayer("AUTO", 2, 15)
                );
                getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Game Mode: AI vs AI.", 5);
                break;
            case SOLITARY:

                battle = new Battle1v1(
                        playerFactory.getPlayer("LOCAL", 1, 0),
                        playerFactory.getPlayer("LOCAL", 2, 15)
                );
                getGame().getSuperScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Game Mode: Local Player vs Local Player.", 5);
                break;
        }
        if (battle != null) {
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), battle, battleType, getGame().getSuperScreen().getNotificationManager(), getGame().getSuperScreen().getScreenSettings()));
        }
    }

    @Override
    public void draw() { //uses Stage batch
        super.draw();
        getBatch().begin();
        if (!getGame().getSuperScreen().isConcederActive()) {
            otherModesPic.draw(getBatch());
        }
        FSLogo.draw(getBatch());
        versionInfo.drawText(getBatch());
        getBatch().end();
    }

    @Override
    public void dispose() {
        solitaryButton.remove();
        solitaryButton.dispose();
        skirmishButton.remove();
        skirmishButton.dispose();
        simulationButton.remove();
        simulationButton.dispose();
        webButton.remove();
        webButton.dispose();
        super.dispose();
    }
}
