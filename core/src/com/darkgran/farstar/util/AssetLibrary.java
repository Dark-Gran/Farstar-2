package com.darkgran.farstar.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetLibrary {
    private final AssetManager assetManager = new AssetManager();

    public void loadAssets() {
        loadFonts();
        loadTextures();
        assetManager.finishLoading();
    }

    private void loadFonts() {
        assetManager.load("fonts/barlow24.fnt", BitmapFont.class);
        assetManager.load("fonts/arial15.fnt", BitmapFont.class);
    }

    private void loadTextures() {
        //Intro
        assetManager.load("images/logo.jpg", Texture.class);
        //MainScreenStage
        assetManager.load("images/FSlogo.png", Texture.class);
        assetManager.load("images/solitary.png", Texture.class);
        assetManager.load("images/solitaryO.png", Texture.class);
        assetManager.load("images/skirmish.png", Texture.class);
        assetManager.load("images/skirmishO.png", Texture.class);
        assetManager.load("images/sim.png", Texture.class);
        assetManager.load("images/simO.png", Texture.class);
        assetManager.load("images/web.png", Texture.class);
        assetManager.load("images/webO.png", Texture.class);
        //TableStage
        assetManager.load("images/tableMain_1920.png", Texture.class);
        assetManager.load("images/Space_1920.png", Texture.class);
        assetManager.load("images/empty.png", Texture.class);
        assetManager.load("images/exit.png", Texture.class);
        assetManager.load("images/quality.png", Texture.class);
        assetManager.load("images/fs.png", Texture.class);
        assetManager.load("images/sound.png", Texture.class);
        assetManager.load("images/logout.png", Texture.class);
        assetManager.load("images/friends.png", Texture.class);
        //Battle
        assetManager.load("images/duel.png", Texture.class);
        assetManager.load("images/duel_cancel.png", Texture.class);
        assetManager.load("images/turn.png", Texture.class);
        assetManager.load("images/yard.png", Texture.class);
        assetManager.load("images/combat_end.png", Texture.class);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
