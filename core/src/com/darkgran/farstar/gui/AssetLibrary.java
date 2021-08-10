package com.darkgran.farstar.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.cards.TechType;
import com.darkgran.farstar.cards.CardInfo;

public class AssetLibrary {
    private static AssetLibrary assetLibrary = null;
    private final AssetManager assetManager = new AssetManager();

    private AssetLibrary() {}
    public static AssetLibrary getInstance() {
        if (assetLibrary == null) {
            assetLibrary = new AssetLibrary();
        }
        return assetLibrary;
    }

    public TextureRegion getPortrait(CardInfo cardInfo, TokenType tokenType) {
        return getAtlasRegion(getPortraitName(cardInfo, tokenType));
    }

    public String getPortraitName(CardInfo cardInfo, TokenType tokenType) {
        return getPortraitName(cardInfo.getId(), tokenType);
    }

    public String getPortraitName(byte id, TokenType tokenType) { //in-future: make hand use a "different F tokenType/portrait" instead of giving F portrait "worse" filtering (does not matter until cards with ships in hand (ie. blueprints) - atm none are present)
        String name = addTokenTypeAcronym(Integer.toString(id)+"-", tokenType, false);
        TextureRegion portrait = ((TextureAtlas) assetManager.get("images/fs2nearest.atlas")).findRegion(name);
        if (portrait == null) {
            portrait = ((TextureAtlas) assetManager.get("images/fs2linear.atlas")).findRegion(name);
        }
        if (portrait != null) {
            return name;
        } else {
            return addTokenTypeAcronym("empty-", tokenType, false);
        }
    }

    public String getFrameName(CardInfo cardInfo, TokenType tokenType) {
        return addTokenTypeAcronym("frame"+cardInfo.getCardRarity().getAcronym()+"-", tokenType, false);
    }

    public String getTypePad(TechType techType, TokenType tokenType) {
        String name = "pad";
        switch (techType) {
            default:
            case NONE:
            case INFERIOR:
                name += "I";
                break;
            case KINETIC:
                name += "K";
                break;
            case THERMAL:
                name += "T";
                break;
            case PARTICLE:
                name += "P";
                break;
            case SUPERIOR:
                name += "S";
                break;
        }
        return addTokenTypeAcronym(name+"-", tokenType, true);
    }

    public String addTokenTypeAcronym(String string, TokenType tokenType, boolean pad) {
        switch (tokenType) {
            default:
            case JUNK:
            case SUPPORT:
                return string + "S";
            case FLEET:
            case HAND:
                return string + "F";
            case MS:
                return string + "MS";
            case FAKE:
                return string + "FK";
            case YARD:
                return pad ? string + "F" : string + "Y";
            case PRINT:
                return string + "Z";
        }
    }

    public BitmapFont getFont(String fontSize, String fontName) {
        return this.get(getFontPath(fontSize, fontName));
    }

    public String getFontPath(String fontSize, String fontName) {
        return "fonts/"+fontName+fontSize+".fnt";
    }

    public void loadAssets() {
        loadFonts();
        loadTextures();
        assetManager.finishLoading();
    }

    //For some reason, when using Atlas for the font dependencies, BitmapFontLoader/AssetManager throws IndexOutOfBoundException when the font uses more than 1 png file.
    //In-future: use Atlas anyway ie. either "fix" BitmapFontLoader/AssetManager or make all fonts use only 1 png file.
    private void loadFonts() {
        BitmapFontLoader.BitmapFontParameter bmpParams = new BitmapFontLoader.BitmapFontParameter();
        bmpParams.minFilter = Texture.TextureFilter.Linear;
        bmpParams.magFilter = Texture.TextureFilter.Linear;
        assetManager.load("fonts/arial15.fnt", BitmapFont.class, bmpParams);
        //Bahnschrift
        assetManager.load("fonts/bahnschrift24.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift26.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift30.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift38.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift40.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift40b.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift44.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift48.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift58.fnt", BitmapFont.class, bmpParams);
        //Orbitron
        assetManager.load("fonts/orbitron36.fnt", BitmapFont.class, bmpParams);
        //Bahnschrift - BMFont (instead of Hiero)
        assetManager.load("fonts/resMeter.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padNormal_F.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padNormal_MS.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padNormal_S.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padNormal_Z.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padOutlined_F.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padOutlined_MS.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padOutlined_S.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/padOutlined_Z.fnt", BitmapFont.class, bmpParams);
        //Orbitron - BMFont
        assetManager.load("fonts/orbitron_nameF.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/orbitron_nameFK.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/orbitron_nameZ.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/barlow_descF.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/barlow_descFK.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/barlow_descZ.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/barlow_tierF.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/barlow_tierFK.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/barlow_tierZ.fnt", BitmapFont.class, bmpParams);
    }

    private void loadTextures() {
        TextureLoader.TextureParameter texParams = new TextureLoader.TextureParameter();
        texParams.minFilter = Texture.TextureFilter.Linear;
        texParams.magFilter = Texture.TextureFilter.Linear;
        assetManager.load("images/tableMain-1920.png", Texture.class, texParams); //in-future: cut table and net to avoid rendering huge areas with 0 alpha
        //Atlases
        assetManager.load("images/fs2linear.atlas", TextureAtlas.class);
        assetManager.load("images/fs2nearest.atlas", TextureAtlas.class);
    }

    public TextureAtlas.AtlasRegion getAtlasRegion(String filename) {
        if (((TextureAtlas) assetManager.get("images/fs2linear.atlas")).findRegion(filename) != null) {
            return ((TextureAtlas) assetManager.get("images/fs2linear.atlas")).findRegion(filename);
        } else {
            return ((TextureAtlas) assetManager.get("images/fs2nearest.atlas")).findRegion(filename);
        }
    }

    public <T> T get(String filename) {
        return assetManager.get(filename);
    }

    public <T> T get(String fileName, Class<T> type) {
        return assetManager.get(fileName, type);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
