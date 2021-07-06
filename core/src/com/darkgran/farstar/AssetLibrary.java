package com.darkgran.farstar;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.CardInfo;

public class AssetLibrary {
    private final AssetManager assetManager = new AssetManager();

    public String getPortraitName(CardInfo cardInfo, TokenType tokenType) {
        String path = "images/portraits/";
        String name = addTokenTypeAcronym(Integer.toString(cardInfo.getId()), tokenType) + ".png";
        if (assetManager.contains(path+name)) {
            return path+name;
        } else {
            return path+addTokenTypeAcronym("empty", tokenType)+".png";
        }
    }

    public String getFrameName(CardInfo cardInfo, TokenType tokenType) {
        String path = "images/portraits/";
        String name = addTokenTypeAcronym(addRarityAcronym("frame", cardInfo), tokenType)+".png";
        return path+name;
    }

    public String addTokenTypeAcronym(String string, TokenType tokenType) {
        string += "_";
        switch (tokenType) {
            default:
            case SUPPORT:
                return string + "S";
            case FLEET:
                return string + "F";
            case MS:
                return string + "MS";
            case FAKE:
                return string + "FK";
            case YARD:
                return string + "Y";
        }
    }

    public String addRarityAcronym(String string, CardInfo cardInfo) {
        switch (cardInfo.getCardRarity()) {
            default:
            case IRON:
                return string + "I";
            case BRONZE:
                return string + "B";
        }
    }

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
        //assetManager.load("fonts/bahnschrift42b.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift44b.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift46b.fnt", BitmapFont.class);
        //assetManager.load("fonts/bahnschrift48b.fnt", BitmapFont.class);
        assetManager.load("fonts/bahnschrift50b.fnt", BitmapFont.class);
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
        //Token Parts
        assetManager.load("images/portraits/frameB_F.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameB_FK.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameB_MS.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameB_S.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameB_Y.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameI_F.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameI_FK.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameI_MS.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameI_S.png", Texture.class, texParams);
        assetManager.load("images/portraits/frameI_Y.png", Texture.class, texParams);
        assetManager.load("images/portraits/pad_I.png", Texture.class, texParams);
        assetManager.load("images/portraits/pad_K.png", Texture.class, texParams);
        assetManager.load("images/portraits/pad_P.png", Texture.class, texParams);
        assetManager.load("images/portraits/pad_S.png", Texture.class, texParams);
        assetManager.load("images/portraits/pad_T.png", Texture.class, texParams);
        //Portraits
        assetManager.load("images/portraits/empty_F.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_FK.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_MS.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_S.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_Y.png", Texture.class, texParams);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
