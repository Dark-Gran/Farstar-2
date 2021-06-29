package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.ListeningStage;
import com.darkgran.farstar.battle.Battle1v1;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.PlayerFactory;
import com.darkgran.farstar.gui.ActorButton;

public class MainScreenStage extends ListeningStage {
    private final PlayerFactory playerFactory = new PlayerFactory();
    private final Texture measureTexture = Farstar.ASSET_LIBRARY.getAssetManager().get("images/solitary.png");
    private final Texture FSLogo = Farstar.ASSET_LIBRARY.getAssetManager().get("images/FSlogo.png");
    private final VersionInfo versionInfo = new VersionInfo((float) (Farstar.STAGE_WIDTH*0.85), (float) (Farstar.STAGE_HEIGHT*0.98), ColorPalette.MAIN);
    private final PerfMeter perfMeter = new PerfMeter((float) (Farstar.STAGE_WIDTH*0.0885), (float) (Farstar.STAGE_HEIGHT*0.98), ColorPalette.MAIN);

    private final ActorButton startButton = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/solitary.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/solitaryO.png")){
        @Override
        public void clicked() {
            System.out.println("Starting Solitary.");
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("LOCAL", 1, 0), playerFactory.getPlayer("LOCAL", 2, 15)), getGame().getSuperScreen().getNotificationManager()));
        }
    };
    private final ActorButton botButton = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/skirmish.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/skirmishO.png")){
        @Override
        public void clicked() {
            System.out.println("Starting Skirmish.");
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("LOCAL", 1, 0), playerFactory.getPlayer("AUTO", 2, 15)), getGame().getSuperScreen().getNotificationManager()));
        }
    };
    private final ActorButton simButton = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/sim.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/simO.png")){
        @Override
        public void clicked() {
            System.out.println("Starting Simulation.");
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("AUTO", 1, 0), playerFactory.getPlayer("AUTO", 2, 15)), getGame().getSuperScreen().getNotificationManager()));
        }
    };
    private final ActorButton webButton = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/web.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/webO.png")){
        @Override
        public void clicked() {
            System.out.println("Opening Web-Browser.");
            Gdx.net.openURI("https://github.com/Dark-Gran/Farstar-2");
        }
    };

    public MainScreenStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        botButton.setPosition((float) (Farstar.STAGE_WIDTH/2- measureTexture.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2+ measureTexture.getHeight()/2));
        simButton.setPosition((float) (Farstar.STAGE_WIDTH/2- measureTexture.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2- measureTexture.getHeight()/2));
        startButton.setPosition((float) (Farstar.STAGE_WIDTH/2- measureTexture.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2- measureTexture.getHeight()*1.499));
        webButton.setPosition((float) (Farstar.STAGE_WIDTH*0.0725), (float) (Farstar.STAGE_HEIGHT*0.012));
        addActor(startButton);
        addActor(botButton);
        addActor(simButton);
        addActor(webButton);
    }

    public void enableMainButtons(boolean disable) {
        startButton.setDisabled(disable);
        botButton.setDisabled(disable);
        simButton.setDisabled(disable);
        if (disable) {
            startButton.remove();
            botButton.remove();
            simButton.remove();
        } else {
            addActor(startButton);
            addActor(botButton);
            addActor(simButton);
        }
    }

    @Override
    public void draw() {
        super.draw();
        getBatch().begin();
        getBatch().draw(FSLogo, (float) (Farstar.STAGE_WIDTH/2-FSLogo.getWidth()/2), (float) (Farstar.STAGE_HEIGHT*0.8));
        versionInfo.drawText(getBatch());
        perfMeter.drawText(getBatch());
        getBatch().end();
    }

    @Override
    public void dispose() {
        startButton.remove();
        startButton.dispose();
        botButton.remove();
        botButton.dispose();
        simButton.remove();
        simButton.dispose();
        webButton.remove();
        webButton.dispose();
        super.dispose();
    }
}
