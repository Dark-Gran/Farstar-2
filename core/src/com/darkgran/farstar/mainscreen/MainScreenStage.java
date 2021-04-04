package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.ListeningStage;
import com.darkgran.farstar.battle.Battle1v1;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.PlayerFactory;

public class MainScreenStage extends ListeningStage {
    private final Texture solitary = new Texture("images/solitary.png");
    private final Texture skirmish = new Texture("images/skirmish.png");
    private final Texture sim = new Texture("images/sim.png");
    private final ImageButton startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(solitary)));
    private final ImageButton botButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(skirmish)));
    private final ImageButton simButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(sim)));
    private final PlayerFactory playerFactory = new PlayerFactory();

    public MainScreenStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        botButton.setPosition((float) (Farstar.STAGE_WIDTH/2-solitary.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2+solitary.getHeight()/2));
        simButton.setPosition((float) (Farstar.STAGE_WIDTH/2-solitary.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-solitary.getHeight()/2));
        startButton.setPosition((float) (Farstar.STAGE_WIDTH/2-solitary.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-solitary.getHeight()*1.5));
        this.addActor(startButton);
        this.addActor(botButton);
        this.addActor(simButton);
        setupListeners();
    }

    @Override
    protected void setupListeners() { //in-future: separate method for "battle start", as there will be many more buttons
        startButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Starting Solitary.");
                getGame().getScreen().dispose();
                getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("LOCAL", 1, 0), playerFactory.getPlayer("LOCAL", 2, 15))));
            }
        });
        botButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Starting Skirmish.");
                getGame().getScreen().dispose();
                getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("LOCAL", 1, 0), playerFactory.getPlayer("AUTO", 2, 15))));
            }
        });
        simButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Starting Simulation.");
                getGame().getScreen().dispose();
                getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(playerFactory.getPlayer("AUTO", 1, 0), playerFactory.getPlayer("AUTO", 2, 15))));
            }
        });
    }

    @Override
    public void dispose() {
        startButton.removeListener(startButton.getClickListener());
        solitary.dispose();
        sim.dispose();
        skirmish.dispose();
        super.dispose();
    }
}
