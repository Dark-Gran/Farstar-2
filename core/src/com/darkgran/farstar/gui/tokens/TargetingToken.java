package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

public class TargetingToken extends FakeToken {
    private Dragger dragger;
    private Texture aimPic;

    public TargetingToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, true);
        aimPic = Farstar.ASSET_LIBRARY.get("images/combat_aim.png");
        setWidth(aimPic.getWidth());
        setHeight(aimPic.getHeight());
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        super.draw(batch);
        batch.end();
        drawConnection(shapeRenderer);
        batch.begin();
        batch.draw(aimPic, getX(), getY());
    }

    private void drawConnection(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(ColorPalette.LIGHT);
        Vector2 start = new Vector2(getCard().getToken().getX()+getCard().getToken().getWidth()/2, getCard().getToken().getY()+getCard().getToken().getHeight()/2);
        Vector2 end = new Vector2(getX()+getWidth()/2, getY()+getHeight()/2);
        shapeRenderer.rectLine(start, end, 2f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();
    }

    @Override
    public Dragger getDragger() {
        return dragger;
    }

    @Override
    public void setDragger(Dragger dragger) {
        this.dragger = dragger;
    }
}
