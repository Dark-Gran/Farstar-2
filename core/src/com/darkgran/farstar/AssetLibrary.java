package com.darkgran.farstar;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
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
        assetManager.load("fonts/arial15.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift24.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift30.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift40b.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift44b.fnt", BitmapFont.class);
        assetManager.load("fonts/orbitron36.fnt", BitmapFont.class);
    }

    private void loadTextures() {
        TextureLoader.TextureParameter texParams = new TextureLoader.TextureParameter();
        texParams.minFilter = Texture.TextureFilter.Linear;
        texParams.magFilter = Texture.TextureFilter.Nearest;
        //All-Screens
        assetManager.load("images/x.png", Texture.class, texParams);
        assetManager.load("images/xO.png", Texture.class, texParams);
        assetManager.load("images/y.png", Texture.class, texParams);
        assetManager.load("images/yO.png", Texture.class, texParams);
        //Intro
        assetManager.load("images/logo.jpg", Texture.class, texParams);
        //MainScreenStage
        assetManager.load("images/FSlogo.png", Texture.class, texParams);
        assetManager.load("images/skirmish.png", Texture.class, texParams);
        assetManager.load("images/skirmishO.png", Texture.class, texParams);
        assetManager.load("images/sim.png", Texture.class, texParams);
        assetManager.load("images/simO.png", Texture.class, texParams);
        assetManager.load("images/solitary.png", Texture.class, texParams);
        assetManager.load("images/solitaryO.png", Texture.class, texParams);
        assetManager.load("images/web.png", Texture.class, texParams);
        assetManager.load("images/webO.png", Texture.class, texParams);
        //TableStage
        assetManager.load("images/empty.png", Texture.class, texParams);
        assetManager.load("images/exit.png", Texture.class, texParams);
        assetManager.load("images/friends.png", Texture.class, texParams);
        assetManager.load("images/fs.png", Texture.class, texParams);
        assetManager.load("images/logout.png", Texture.class, texParams);
        assetManager.load("images/quality.png", Texture.class, texParams);
        assetManager.load("images/sound.png", Texture.class, texParams);
        assetManager.load("images/Space_1920.png", Texture.class, texParams);
        assetManager.load("images/tableMain_1920.png", Texture.class, texParams);
        //Battle
        assetManager.load("images/combat_end.png", Texture.class, texParams);
        assetManager.load("images/combat_endO.png", Texture.class, texParams);
        assetManager.load("images/deck.png", Texture.class, texParams);
        assetManager.load("images/duel.png", Texture.class, texParams);
        assetManager.load("images/duelO.png", Texture.class, texParams);
        assetManager.load("images/duel_cancel.png", Texture.class, texParams);
        assetManager.load("images/duel_cancelO.png", Texture.class, texParams);
        assetManager.load("images/matter.png", Texture.class, texParams);
        assetManager.load("images/energy.png", Texture.class, texParams);
        assetManager.load("images/rounds.png", Texture.class, texParams);
        assetManager.load("images/turn.png", Texture.class, texParams);
        assetManager.load("images/turnO.png", Texture.class, texParams);
        assetManager.load("images/turnOP.png", Texture.class, texParams);
        assetManager.load("images/turnP.png", Texture.class, texParams);
        assetManager.load("images/yard.png", Texture.class, texParams);
        assetManager.load("images/yardO.png", Texture.class, texParams);
        assetManager.load("images/yardOP.png", Texture.class, texParams);
        assetManager.load("images/yardP.png", Texture.class, texParams);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
