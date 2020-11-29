package com.darkgran.farstar.battle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.TableStage;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.CombatManager;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final Battle battle;
    private final BattleStage battleStage;

    public BattleScreen(final Farstar game, TableStage tableStage, Battle battle)
    {
        super(game);
        setTableMenu(tableStage);
        Box2D.init();
        this.battle = battle;
        battleStage = battle.createBattleStage(game, getViewport(), this);
        game.getInputMultiplexer().addProcessor(battleStage);
        battle.launchBattle(new RoundManager(battle), new CombatManager(battle));
    }

    @Override
    public void drawContent(float delta, Batch batch) {
        battleStage.drawBattleStage(delta, batch);
        //drawBox2DDebug(); //draws boundaries of world-bodies but disables fonts
        worldManager.worldTimer(delta); //world stepping
    }

    @Override
    public void drawMenus(float delta) { //Stage-menus use their own Batch
        if (battleStage != null) {
            battleStage.act(delta);
            battleStage.draw();
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
        getGame().getInputMultiplexer().removeProcessor(battleStage);
        battleStage.dispose();
    }

    public BattleStage getGUI() { return battleStage; }

    public Battle getBattle() { return battle; }

    public WorldManager getWorldManager() { return worldManager; }

}
