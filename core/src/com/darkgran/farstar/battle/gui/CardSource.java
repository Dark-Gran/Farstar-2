package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.PB2Drawer;
import com.darkgran.farstar.gui.TextInTheBox;

/**
 * "Deck"
 */
public class CardSource extends PB2Drawer {
    private final Texture pic = Farstar.ASSET_LIBRARY.getAssetManager().get("images/deck.png");
    private final TextInTheBox info;

    public CardSource(float x, float y, BattleStage battleStage, Player player) {
        super(x, y, battleStage, player);
        setupBox(x, y, pic.getWidth(), pic.getHeight());
        info = new TextInTheBox(ColorPalette.MAIN, ColorPalette.DARK, "fonts/bahnschrift30.fnt", "n/a", x, y, 10, 10);
        update();
    }

    public void update() {
        info.setText(getPlayer().getDeck().size()+" Cards remaining.");
    }

    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        draw(batch);
        info.draw(batch, shapeRenderer);
    }

    @Override
    public void draw(Batch batch) {
        //super.draw(batch);
        batch.draw(pic, getX(), getY());
    }

}
