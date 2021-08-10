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
    private final PlayerFactory playerFactory = PlayerFactory.getInstance();
    private final SimpleImage2 FSLogo;
    private final VersionInfo versionInfo;
    private final ActorButton solitaryButton = new ActorButton(AssetLibrary.getInstance().getAtlasRegion("solitary"), AssetLibrary.getInstance().getAtlasRegion("solitaryO")){
        @Override
        public void clicked() { launchBattleScreen(BattleType.SOLITARY); }
    };
    private final ActorButton skirmishButton = new ActorButton(AssetLibrary.getInstance().getAtlasRegion("skirmish"), AssetLibrary.getInstance().getAtlasRegion("skirmishO")){
        @Override
        public void clicked() { launchBattleScreen(BattleType.SKIRMISH); }
    };
    private final ActorButton simulationButton = new ActorButton(AssetLibrary.getInstance().getAtlasRegion("sim"), AssetLibrary.getInstance().getAtlasRegion("simO")){
        @Override
        public void clicked() { launchBattleScreen(BattleType.SIMULATION); }
    };
    private final ActorButton webButton = new ActorButton(AssetLibrary.getInstance().getAtlasRegion("web"), AssetLibrary.getInstance().getAtlasRegion("webO")){
        @Override
        public void clicked() {
            System.out.println("Opening Web-Browser.");
            Gdx.net.openURI("https://github.com/Dark-Gran/Farstar-2");
        }
    };
    private final SimpleImage2 otherModesPic;


    public MainScreenStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        versionInfo = new VersionInfo((float) (Farstar.STAGE_WIDTH*0.85), (float) (Farstar.STAGE_HEIGHT*0.98), ColorPalette.MAIN, Farstar.APP_VERSION_NAME);
        TextureRegion tr = AssetLibrary.getInstance().getAtlasRegion("FSLogo");
        FSLogo = new SimpleImage2((float) (Farstar.STAGE_WIDTH/2-tr.getRegionWidth()/2), (float) (Farstar.STAGE_HEIGHT*0.8), tr);
        TextureRegion measureTexture = AssetLibrary.getInstance().getAtlasRegion("solitary");
        skirmishButton.setPosition(Farstar.STAGE_WIDTH/2f - skirmishButton.getWidth()/2, Math.round((float) (Farstar.STAGE_HEIGHT/2 - measureTexture.getRegionHeight()*0.5)));
        tr = AssetLibrary.getInstance().getAtlasRegion("otherModes");
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

    private void launchBattleScreen(BattleType battleType) { //in-future: rework for "custom players"
        System.out.println("Starting "+battleType.getName()+".");
        Battle battle = null;
        switch (battleType) {
            case SKIRMISH:
                NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Use F1 for Instructions.", 5, true);
                battle = new Battle1v1(
                        playerFactory.getPlayer("LOCAL", 1, 0),
                        playerFactory.getPlayer("AUTO", 2, 15),
                        battleType
                );
                break;
            case SIMULATION:
                battle = new Battle1v1(
                        playerFactory.getPlayer("AUTO", 1, 0),
                        playerFactory.getPlayer("AUTO", 2, 15),
                        battleType
                );
                NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Game Mode: AI vs AI.", 5);
                break;
            case SOLITARY:

                battle = new Battle1v1(
                        playerFactory.getPlayer("LOCAL", 1, 0),
                        playerFactory.getPlayer("LOCAL", 2, 15),
                        battleType
                );
                NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Game Mode: Local Player vs Local Player.", 5);
                break;
        }
        if (battle != null) {
            getGame().setScreen(new BattleScreen(getGame(), Farstar.getSuperScreen().getTableMenu(), battle, battleType));
        }
    }

    @Override
    public void draw() { //uses Stage batch
        super.draw();
        getBatch().begin();
        if (!Farstar.getSuperScreen().isConcederActive()) {
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
