package com.darkgran.farstar;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkgran.farstar.cards.CardLibrary;
import com.darkgran.farstar.gui.AssetLibrary;

public class Farstar extends Game {
	public static final String APP_VERSION_NAME = "v0.3 (Alpha)";
	public static final int STAGE_WIDTH = 1920; //move to SuperScreen?
	public static final int STAGE_HEIGHT = 960; //move to SuperScreen?
	public static final int DEFAULT_FPS = 60;
	private SuperScreen superScreen;
	private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
	public final MouseWindowQuery MWQ;
	protected SpriteBatch batch;
	protected int currentFPSCap = DEFAULT_FPS;
	public static boolean firstMatchThisLaunch = true; //in-future: bind to user settings (file-write or user-acc)

	public Farstar(MouseWindowQuery MWQ) {
		this.MWQ = MWQ;
	}

	protected void setForegroundFPS(int value) { } //see DesktopLauncher
	protected void setBackgroundFPS(int value) { }

	@Override
	public void create () {
		setForegroundFPS(currentFPSCap);
		setBackgroundFPS(currentFPSCap);
		AssetLibrary.getInstance().loadAssets();
		CardLibrary.getInstance().loadLocal("content/cards.json");
		batch = new SpriteBatch();
		this.setScreen(new IntroScreen(this, new ScreenSettings()));
		inputMultiplexer.addProcessor(new KeyboardProcessor(this));
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void setScreen(Screen screen) {
		if (superScreen != null) { superScreen.dispose(); }
		superScreen = (SuperScreen) screen;
		super.setScreen(screen);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		AssetLibrary.getInstance().dispose();
		super.dispose();
	}

	public SuperScreen getSuperScreen() {
		return superScreen;
	}

	public InputMultiplexer getInputMultiplexer() { return inputMultiplexer; }

}
