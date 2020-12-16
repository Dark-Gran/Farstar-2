package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.battle.Battle1v1;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.players.cards.Mothership;

public class MainScreenStage extends ListeningStage {
    private final Texture start = new Texture("images/start.png");
    private final ImageButton startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(start)));
    private final ImageButton botButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(start)));

    public MainScreenStage(final Farstar game, Viewport viewport) {
        super(game, viewport);
        startButton.setPosition((float) (Farstar.STAGE_WIDTH/2-start.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-start.getHeight()/2));
        botButton.setPosition((float) (Farstar.STAGE_WIDTH/2-start.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-start.getHeight()*3/2));
        this.addActor(startButton);
        this.addActor(botButton);
        setupListeners();
    }

    @Override
    public void setupListeners() {
        startButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Starting Solitary.");
                getGame().getScreen().dispose();
                getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(createLocalPlayer(1, 0), createLocalPlayer(2, 15))));
            }
        });
        botButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                System.out.println("Starting Skirmish.");
                getGame().getScreen().dispose();
                getGame().setScreen(new BattleScreen(getGame(), getGame().getSuperScreen().getTableMenu(), new Battle1v1(createLocalPlayer(1, 0), createAutomaton(2, 15))));
            }
        });
    }

    private Player createLocalPlayer(int playerID, int mothershipId) {
        return new LocalPlayer((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard());
    }

    private Player createAutomaton(int playerID, int mothershipId) {
        return new Automaton((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard(), BotTier.AUTOMATON);
    }

    @Override
    public void dispose() {
        startButton.removeListener(startButton.getClickListener());
        start.dispose();
        super.dispose();
    }
}
