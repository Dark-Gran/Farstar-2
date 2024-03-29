package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.gui.TextInTheBox;

/**
 * "Deck"
 */
public class CardSource extends Actor {
    private final TextureRegion pic = AssetLibrary.getInstance().getAtlasRegion("deck");
    private final BPB2Drawer drawer;
    private final TextInTheBox info;
    private final ClickListener clickListener = new ClickListener(){};

    public CardSource(float x, float y, BattleStage battleStage, BattlePlayer battlePlayer, boolean onBottom) {
        drawer = new BPB2Drawer(x, y, battleStage, battlePlayer){
            @Override
            public void draw(Batch batch) {
                //super.draw(batch);
                batch.draw(pic, getX(), getY());
            }
        };
        drawer.setupBox(x, y, pic.getRegionWidth(), pic.getRegionHeight());
        setX(x);
        setY(y);
        setWidth(pic.getRegionWidth());
        setHeight(pic.getRegionHeight());
        info = new TextInTheBox(
                ColorPalette.LIGHT,
                ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f),
                "fonts/bahnschrift30.fnt",
                "00 Cards remaining.",
                x-300f,
                y + ((onBottom) ? -40f : 100f),
                280,
                50
        );
        update();
        addListener(clickListener);
    }

    public void update() {
        info.setText(drawer.getBattlePlayer().getDeck().size()+" Cards remaining.");
    }

    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        drawer.draw(batch);
        if (clickListener.isOver()) {
            info.draw(batch, shapeRenderer);
        }
    }

}
