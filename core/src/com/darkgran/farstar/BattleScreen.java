package com.darkgran.farstar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.battle.WorldManager;
import com.darkgran.farstar.battle.gui.GUI;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final Battle battle;
    private GUI gui;

    public BattleScreen(final Farstar game, TableMenu tableMenu, Battle battle)
    {
        super(game);
        setTableMenu(tableMenu);
        Box2D.init();
        this.battle = battle;
        this.battle.launchBattle(new RoundManager(this.battle));
    }

    @Override
    public void drawContent(float delta, Batch batch) {
        //battle.getGUI().drawGUI(delta, batch);
        //drawBox2DDebug(); //draws boundaries of world-bodies but disables fonts
        worldManager.worldTimer(delta); //world stepping
    }

    @Override
    public void drawMenus(float delta) {
        //battle.getGUI().
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
