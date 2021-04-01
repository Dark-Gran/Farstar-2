package com.darkgran.farstar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkgran.farstar.battle.CardLibrary;

public class Farstar extends Game {
	public static final int STAGE_WIDTH = 1920;
	public static final int STAGE_HEIGHT = 960;
	public static final CardLibrary CARD_LIBRARY = new CardLibrary();
	private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
	protected SuperScreen superScreen;
	SpriteBatch batch;

	protected void loadLibrary() { CARD_LIBRARY.loadLocal("content/cards.json"); }

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new IntroScreen(this));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		this.superScreen = (SuperScreen) screen;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public SuperScreen getSuperScreen () {
		return superScreen;
	}

	public InputMultiplexer getInputMultiplexer() { return inputMultiplexer; }

}
