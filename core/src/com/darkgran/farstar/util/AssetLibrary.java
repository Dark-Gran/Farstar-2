package com.darkgran.farstar.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetLibrary {
    private final AssetManager assetManager = new AssetManager();

    public void loadAssets() {
        assetManager.load("fonts/barlow24.fnt", BitmapFont.class);
        assetManager.load("fonts/arial15.fnt", BitmapFont.class);
        assetManager.finishLoading();
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
