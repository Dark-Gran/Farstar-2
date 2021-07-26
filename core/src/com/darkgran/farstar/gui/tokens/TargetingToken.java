package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.Dragger;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.SimpleVector2;

public class TargetingToken extends FakeToken {
    private Dragger dragger;
    private TextureRegion aimPic;
    private TextureRegion aimPicRegion;

    public TargetingToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, true);
        aimPic = Farstar.ASSET_LIBRARY.getAtlasRegion("combat-aim");
        aimPicRegion = new TextureRegion(aimPic);
        setWidth(aimPic.getRegionWidth());
        setHeight(aimPic.getRegionHeight());
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        setPosition(coords.x-getWidth()/2f, Farstar.STAGE_HEIGHT-(coords.y+getHeight()/2f));
        battleCard.getToken().setPicked(true);
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        super.draw(batch);
        batch.end();
        Vector2 start = new Vector2(getCard().getToken().getX()+getCard().getToken().getWidth()/2, getCard().getToken().getY()+getCard().getToken().getHeight()/2);
        Vector2 end = new Vector2(getX()+getWidth()/2, getY()+getHeight()/2);
        drawConnection(shapeRenderer, start, end, ColorPalette.changeAlpha(ColorPalette.LIGHT, 0.3f));
        batch.begin();
        batch.draw(aimPicRegion, getX(), getY(), getWidth()/2f, getHeight()/2f, getWidth(), getHeight(), 1f, 1f, (float) Math.toDegrees(Math.atan2(start.x - end.x, end.y - start.y)));
    }

    public static void drawConnection(ShapeRenderer shapeRenderer, Vector2 start, Vector2 end, Color color) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rectLine(start, end, 5f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void destroy() {
        super.destroy();
        getCard().getToken().setPicked(false);
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
