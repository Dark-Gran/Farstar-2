package com.darkgran.farstar;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkgran.farstar.cards.CardLibrary;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.NotificationManager;

public class Farstar extends Game {
	public static final String APP_VERSION_NAME = "v0.3 (Alpha)";
	public static final int STAGE_WIDTH = 1920;
	public static final int STAGE_HEIGHT = 960;
	public static final int DEFAULT_FPS = 60;
	public static final CardLibrary CARD_LIBRARY = new CardLibrary();
	public static final AssetLibrary ASSET_LIBRARY = new AssetLibrary();
	private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
	private final KeyboardProcessor keyboardProcessor = new KeyboardProcessor(this);
	protected SuperScreen superScreen;
	SpriteBatch batch;
	public final MouseWindowQuery MWQ;
	protected int currentFPSCap = DEFAULT_FPS;
	public static boolean firstMatchThisLaunch = true; //in-future: bind to user settings (file-write or user-acc)

	public Farstar(MouseWindowQuery MWQ) {
		this.MWQ = MWQ;
	}

	protected void setForegroundFPS(int value) { } //see DesktopLauncher
	protected void setBackgroundFPS(int value) { }

	public void loadLibrary() { CARD_LIBRARY.loadLocal("content/cards.json"); }

	@Override
	public void create () {
		setForegroundFPS(currentFPSCap);
		setBackgroundFPS(currentFPSCap);
		ASSET_LIBRARY.loadAssets();
		batch = new SpriteBatch();
		this.setScreen(new IntroScreen(this));
		inputMultiplexer.addProcessor(keyboardProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void setScreen(Screen screen) {
		if (superScreen != null) { superScreen.dispose(); }
		this.superScreen = (SuperScreen) screen;
		super.setScreen(screen);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		ASSET_LIBRARY.dispose();
	}

	public SuperScreen getSuperScreen () {
		return superScreen;
	}

	public InputMultiplexer getInputMultiplexer() { return inputMultiplexer; }

}
