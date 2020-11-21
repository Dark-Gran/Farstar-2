package com.darkgran.farstar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.battle.BattleManager;
import com.darkgran.farstar.battle.WorldManager;
import com.darkgran.farstar.menus.TableMenu;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final BattleManager battleManager;

    public BattleScreen(final Farstar game, TableMenu tableMenu, BattleManager battleManager)
    {
        super(game);
        setTableMenu(tableMenu);
        Box2D.init();
        this.battleManager = battleManager;
    }

    @Override
    public void drawScreen(float delta, Batch batch) {
        battleManager.getGui().drawGUI(batch);
        //drawBox2DDebug(); //draws boundaries of world-bodies but disables fonts
        worldManager.worldTimer(delta); //world stepping
    }

    private void drawBox2DDebug() {
        Matrix4 debugMatrix = new Matrix4(getCamera().combined);
        debugMatrix.scale(1f, 1f, 1f);
        debugRenderer.setDrawBodies(true);
        debugRenderer.render(worldManager.getWorld(), debugMatrix);
    }

    @Override
    public void dispose() {
        worldManager.disposeWorld();
    }
}
