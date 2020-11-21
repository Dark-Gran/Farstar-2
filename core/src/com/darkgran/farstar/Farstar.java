package com.darkgran.farstar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Farstar extends Game {
	public static final int STAGE_WIDTH = 1280;
	public static final int STAGE_HEIGHT = 640;
	private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

	protected SuperScreen superScreen;
	SpriteBatch batch;

	public InputMultiplexer getInputMultiplexer() { return inputMultiplexer; }

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

	public SuperScreen getSuperScreen () {
		return superScreen;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
