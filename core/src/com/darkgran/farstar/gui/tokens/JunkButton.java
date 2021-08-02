package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.DropTarget;
import com.darkgran.farstar.gui.SimpleBox2;
import com.darkgran.farstar.gui.SimpleVector2;

public class JunkButton extends ClickToken implements DropTarget {
    private final BattlePlayer battlePlayer;
    private TextureRegion netSpot = Farstar.ASSET_LIBRARY.getAtlasRegion("netspot-S");
    private final Matrix4 mx = new Matrix4();
    private Matrix4 oldMX;

    public JunkButton(float x, float y, BattleStage battleStage, BattlePlayer battlePlayer) {
        super(null, x, y, battleStage, null, TokenType.JUNK, false, false);
        this.battlePlayer = battlePlayer;
        float angle = -90f;
        mx.rotate(new Vector3(0, 0, 1), angle);
        SimpleVector2 newXY = HandToken.getNewXYFromAngle(0, 0, getX(), getY(), Math.toRadians(angle));
        mx.trn(newXY.x, newXY.y+getHeight(), 0);
    }

    @Override
    public void draw(Batch batch) {
        //Rotate
        oldMX = batch.getTransformMatrix().cpy();
        batch.setTransformMatrix(mx);
        //Draw
        if (getCard() == null) { batch.draw(netSpot, getX(), getY()); }
        super.draw(batch);
        batch.setTransformMatrix(oldMX);
    }

    @Override
    protected void debugRender(Batch batch) {
        batch.setTransformMatrix(oldMX);
        BattleScreen.drawDebugBox(getSimpleBox2().x, getSimpleBox2().y, getSimpleBox2().getWidth(), getSimpleBox2().getHeight(), getBattleStage().getBattleScreen().getShapeRenderer(), batch);
    }

    @Override
    public SimpleBox2 getSimpleBox2() {
        return new SimpleBox2(getX(), getY(), getWidth(), getHeight());
    }

    public BattlePlayer getBattlePlayer() {
        return battlePlayer;
    }

}
