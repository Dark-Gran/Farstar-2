package com.darkgran.farstar.battle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.TableMenu;
import com.darkgran.farstar.battle.gui.GUI;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final Battle battle;
    private final GUI gui; //buttons

    public BattleScreen(final Farstar game, TableMenu tableMenu, Battle battle)
    {
        super(game);
        setTableMenu(tableMenu);
        Box2D.init();
        this.battle = battle;
        gui = battle.createGUI(game, getViewport(), this);
        game.getInputMultiplexer().addProcessor(gui);
        battle.launchBattle(new RoundManager(battle));
    }

    @Override
    public void drawContent(float delta, Batch batch) {
        gui.drawGUI(delta, batch);
        //drawBox2DDebug(); //draws boundaries of world-bodies but disables fonts
        worldManager.worldTimer(delta); //world stepping
    }

    @Override
    public void drawMenus(float delta) {
        if (gui != null) {
            gui.act(delta);
            gui.draw();
        }
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
        getGame().getInputMultiplexer().removeProcessor(gui);
        gui.dispose();
    }

    public GUI getGUI() { return gui; }

    public Battle getBattle() { return battle; }

    public WorldManager getWorldManager() { return worldManager; }

}
