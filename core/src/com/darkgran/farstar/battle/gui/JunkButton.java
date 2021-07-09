package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;

public class JunkButton extends Token implements DropTarget {
    private final float angle = -90f;
    private final Player player;
    private Matrix4 mx = new Matrix4();
    private Matrix4 oldMX;

    public JunkButton(float x, float y, BattleStage battleStage, Player player) {
        super(null, x, y, battleStage, null, TokenType.JUNK, false, false);
        this.player = player;
        setupSimpleBox2(x, y, TokenType.JUNK.getWidth(), TokenType.JUNK.getHeight());
        mx.rotate(new Vector3(0, 0, 1), angle);
        SimpleVector2 newXY = HandToken.getNewXYFromAngle(0, 0, getX()+getWidth()/2, getY()+getHeight()/2, Math.toRadians(angle));
        mx.trn(newXY.getX(), newXY.getY(), 0);
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
        setRotation(angle);
    }

    @Override
    public void draw(Batch batch) {
        //Rotate
        oldMX = batch.getTransformMatrix().cpy();
        batch.setTransformMatrix(mx);
        //Draw
        super.draw(batch);
        batch.setTransformMatrix(oldMX);
    }

    @Override
    protected void debugRender(Batch batch) {
        batch.end();
        getBattleStage().getBattleScreen().getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        getBattleStage().getBattleScreen().getShapeRenderer().rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f, getRotation());
        getBattleStage().getBattleScreen().getShapeRenderer().end();
        batch.begin();
    }

    @Override
    public SimpleBox2 getSimpleBox2() {
        return new SimpleBox2(getX(), getY(), getWidth(), getHeight());
    }

    public Player getPlayer() {
        return player;
    }

}
