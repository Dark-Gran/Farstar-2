package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.TechType;

public abstract class TokenTech extends TokenPart {

    public TokenTech(String fontPath, Token token) {
        super(fontPath, token);
    }

    public Texture getTypeTexture(TechType techType) {
        switch (techType) {
            default:
            case NONE:
            case INFERIOR:
                return Farstar.ASSET_LIBRARY.getAssetManager().get("images/portraits/pad_I.png");
            case KINETIC:
                return Farstar.ASSET_LIBRARY.getAssetManager().get("images/portraits/pad_K.png");
            case THERMAL:
                return Farstar.ASSET_LIBRARY.getAssetManager().get("images/portraits/pad_T.png");
            case PARTICLE:
                return Farstar.ASSET_LIBRARY.getAssetManager().get("images/portraits/pad_P.png");
            case SUPERIOR:
                return Farstar.ASSET_LIBRARY.getAssetManager().get("images/portraits/pad_S.png");
        }
    }

}
