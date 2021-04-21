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
        assetManager.load("images/FSlogo.png", Texture.class);
        assetManager.load("images/solitary.png", Texture.class);
        assetManager.load("images/solitaryO.png", Texture.class);
        assetManager.load("images/skirmish.png", Texture.class);
        assetManager.load("images/skirmishO.png", Texture.class);
        assetManager.load("images/sim.png", Texture.class);
        assetManager.load("images/simO.png", Texture.class);
        assetManager.load("images/web.png", Texture.class);
        assetManager.load("images/webO.png", Texture.class);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
