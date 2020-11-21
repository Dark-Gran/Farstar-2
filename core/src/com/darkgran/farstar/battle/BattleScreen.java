package com.darkgran.farstar.battle;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.ui.TableMenu;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final BattleManager battleManager = new BattleManager();

    public BattleScreen(final Farstar game, TableMenu tableMenu)
    {
        super(game);
        setTableMenu(tableMenu);
        Box2D.init();
    }

    @Override
    public void drawScreen(float delta) {
        drawBox2DDebug();
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
