package com.darkgran.farstar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Farstar extends Game {
	public static final int STAGE_WIDTH = 1280;
	public static final int STAGE_HEIGHT = 640;
	SpriteBatch batch;
	//Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new IntroScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
