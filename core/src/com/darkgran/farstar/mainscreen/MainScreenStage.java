package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.ListeningStage;
import com.darkgran.farstar.battle.Battle1v1;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.PlayerFactory;
import com.darkgran.farstar.util.ActorButton;

public class MainScreenStage extends ListeningStage {
    private final PlayerFactory playerFactory = new PlayerFactory();
    private final Texture solitary = new Texture("images/solitary.png");
    private final Texture skirmish = new Texture("images/skirmish.png");
    private final Texture sim = new Texture("images/sim.png");
    private final Texture web = new Texture("images/web.png");
    private final Texture solitaryO = new Texture("images/solitaryO.png");
    private final Texture skirmishO = new Texture("images/skirmishO.png");
    private final Texture simO = new Texture("images/simO.png");
    private final Texture webO = new Texture("images/webO.png");
    private final Texture FSLogo = new Texture("images/FSLogo.png");
    private final VersionInfo versionInfo = new VersionInfo((float) (Farstar.STAGE_WIDTH*0.85), (float) (Farstar.STAGE_HEIGHT*0.98), new Color(0.31f, 0.498f, 0.706f, 1));
    private final PerfMeter perfMeter = new PerfMeter((float) (Farstar.STAGE_WIDTH*0.085), (float) (Farstar.STAGE_HEIGHT*0.98), new Color(0.31f, 0.498f, 0.706f, 1));

    private final ActorButton startButton = new ActorButton(solitary, solitaryO){
        @Override
        public void clicked() {
            System.out.println("Starting Solitary.");
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("LOCAL", 1, 0), playerFactory.getPlayer("LOCAL", 2, 15))));
        }
    };
    private final ActorButton botButton = new ActorButton(skirmish, skirmishO){
        @Override
        public void clicked() {
            System.out.println("Starting Skirmish.");
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("LOCAL", 1, 0), playerFactory.getPlayer("AUTO", 2, 15))));
        }
    };
    private final ActorButton simButton = new ActorButton(sim, simO){
        @Override
        public void clicked() {
            System.out.println("Starting Simulation.");
            getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("AUTO", 1, 0), playerFactory.getPlayer("AUTO", 2, 15))));
        }
    };
    private final ActorButton webButton = new ActorButton(web, webO){
        @Override
        public void clicked() {
            System.out.println("LinkButton Click"); //TODO
        }
    };

    public MainScreenStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        botButton.setPosition((float) (Farstar.STAGE_WIDTH/2-solitary.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2+solitary.getHeight()/2));
        simButton.setPosition((float) (Farstar.STAGE_WIDTH/2-solitary.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-solitary.getHeight()/2));
        startButton.setPosition((float) (Farstar.STAGE_WIDTH/2-solitary.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-solitary.getHeight()*1.5));
        webButton.setPosition((float) (Farstar.STAGE_WIDTH*0.054), (float) (Farstar.STAGE_HEIGHT*0.005));
        this.addActor(startButton);
        this.addActor(botButton);
        this.addActor(simButton);
        this.addActor(webButton);
        setupListeners();
    }

    @Override
    public void draw() {
        super.draw();
        getBatch().begin();
        getBatch().draw(FSLogo, (float) (Farstar.STAGE_WIDTH/2-FSLogo.getWidth()/2), (float) (Farstar.STAGE_HEIGHT*0.8));
        versionInfo.draw(getBatch());
        perfMeter.draw(getBatch());
        getBatch().end();
    }

    @Override
    protected void setupListeners() { }

    @Override
    public void dispose() {
        solitary.dispose();
        sim.dispose();
        skirmish.dispose();
        web.dispose();
        FSLogo.dispose();
        versionInfo.dispose();
        perfMeter.dispose();
        super.dispose();
    }
}
