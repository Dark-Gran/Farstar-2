package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.PB2Drawer;

/**
 * "Deck"
 */
public class CardSource extends PB2Drawer {
    private final Texture pic = Farstar.ASSET_LIBRARY.getAssetManager().get("images/deck.png");

    public CardSource(float x, float y, BattleStage battleStage, Player player) {
        super(x, y, battleStage, player);
        setupBox(x, y, pic.getWidth(), pic.getHeight());
    }

    @Override
    public void draw(Batch batch) {
        //super.draw(batch);
        batch.draw(pic, getX(), getY());
    }
}
