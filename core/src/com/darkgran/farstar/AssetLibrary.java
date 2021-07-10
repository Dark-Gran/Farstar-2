package com.darkgran.farstar;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.TechType;
import com.darkgran.farstar.battle.players.cards.CardInfo;

public class AssetLibrary {
    private final AssetManager assetManager = new AssetManager();

    public String getPortraitName(CardInfo cardInfo, TokenType tokenType) {
        String path = "images/portraits/";
        String name = addTokenTypeAcronym(Integer.toString(cardInfo.getId()), tokenType, false) + ".png";
        if (assetManager.contains(path+name)) {
            return path+name;
        } else {
            return path+addTokenTypeAcronym("empty", tokenType, false)+".png";
        }
    }

    public String getFrameName(CardInfo cardInfo, TokenType tokenType) {
        String path = "images/tokens/";
        String name = addTokenTypeAcronym(addRarityAcronym("frame", cardInfo), tokenType, false)+".png";
        return path+name;
    }

    public String getTypePad(TechType techType, TokenType tokenType) {
        String name = "images/tokens/pad";
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
        return addTokenTypeAcronym(name, tokenType, true)+".png";
    }

    public String addTokenTypeAcronym(String string, TokenType tokenType, boolean pad) {
        string += "_";
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
        loadCursors();
        loadFonts();
        loadTextures();
        assetManager.finishLoading();
    }

    private void loadCursors() {
        assetManager.load("images/cursor_aim.png", Pixmap.class);
        assetManager.load("images/cursor_default.png", Pixmap.class);
    }

    private void loadFonts() {
        BitmapFontLoader.BitmapFontParameter bmpParams = new BitmapFontLoader.BitmapFontParameter();
        bmpParams.minFilter = Texture.TextureFilter.Linear;
        bmpParams.magFilter = Texture.TextureFilter.Linear;
        assetManager.load("fonts/arial15.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift24.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift30.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift40b.fnt", BitmapFont.class, bmpParams);
        //assetManager.load("fonts/bahnschrift42b.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift44b.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift46b.fnt", BitmapFont.class, bmpParams);
        //assetManager.load("fonts/bahnschrift48b.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift50b.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/bahnschrift58b.fnt", BitmapFont.class, bmpParams);
        assetManager.load("fonts/orbitron36.fnt", BitmapFont.class, bmpParams);
    }

    private void loadTextures() {
        TextureLoader.TextureParameter texParams = new TextureLoader.TextureParameter();
        texParams.minFilter = Texture.TextureFilter.Linear;
        texParams.magFilter = Texture.TextureFilter.Linear;
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
        assetManager.load("images/tokens/abiU_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/abiU_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/abiU_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/abiU_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/card_D.png", Texture.class, texParams);
        assetManager.load("images/tokens/card_U.png", Texture.class, texParams);
        assetManager.load("images/tokens/card_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameB_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameB_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameB_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameB_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameB_Y.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameB_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameI_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameI_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameI_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameI_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameI_Y.png", Texture.class, texParams);
        assetManager.load("images/tokens/frameI_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_D.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_U.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_Y.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowG_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_D.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_U.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_Y.png", Texture.class, texParams);
        assetManager.load("images/tokens/glowY_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padE_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padE_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padE_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padE_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padE_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padI_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padI_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padI_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padI_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padI_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padK_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padK_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padK_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padK_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padK_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padM_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padM_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padM_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padM_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padM_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padT_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padT_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padT_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padT_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padT_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padP_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padP_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padP_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padP_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padP_Z.png", Texture.class, texParams);
        assetManager.load("images/tokens/padS_F.png", Texture.class, texParams);
        assetManager.load("images/tokens/padS_FK.png", Texture.class, texParams);
        assetManager.load("images/tokens/padS_MS.png", Texture.class, texParams);
        assetManager.load("images/tokens/padS_S.png", Texture.class, texParams);
        assetManager.load("images/tokens/padS_Z.png", Texture.class, texParams);
        //Portraits
        assetManager.load("images/portraits/empty_F.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_FK.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_MS.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_S.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_Y.png", Texture.class, texParams);
        assetManager.load("images/portraits/empty_Z.png", Texture.class, texParams);
    }

    public <T> T get(String filename) {
        return assetManager.get(filename);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
