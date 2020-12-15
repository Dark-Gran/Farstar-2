package com.darkgran.farstar.battle;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.TableStage;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.util.SimpleBox2;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final Battle battle;
    private final BattleStage battleStage;
    public final static boolean DEBUG_RENDER = true;

    private final InputAdapter generalInputProcessor = new InputAdapter() {
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (button == 1) { //mouse.right
                if (battle.getRoundManager().isLaunched() && battle.getWhoseTurn() instanceof LocalPlayer) {
                    battle.getRoundManager().tryCancel();
                }
            }
            return false;
        }
    };

    public BattleScreen(final Farstar game, TableStage tableStage, Battle battle)
    {
        super(game);
        setTableMenu(tableStage);
        Box2D.init();
        this.battle = battle;
        battle.startingSetup(new RoundManager(battle), new CombatManager(battle, battle.createDuelManager()), new AbilityManager(battle));
        battleStage = battle.createBattleStage(game, getViewport(), this);
        battle.getCombatManager().setBattleStage(battleStage);
        battle.getCombatManager().getDuelManager().getDuelMenu().setBattleStage(battleStage);
        game.getInputMultiplexer().addProcessor(generalInputProcessor);
        game.getInputMultiplexer().addProcessor(battleStage);
        battle.getRoundManager().launch();
    }

    @Override
    public void drawContent(float delta, Batch batch) {
        battleStage.drawBattleStage(delta, batch);
        //if (DEBUG_RENDER) { drawBox2DDebug(batch); } //draws boundaries of world-bodies
        worldManager.worldTimer(delta); //world stepping
    }

    @Override
    public void drawMenus(float delta) { //Stage-menus use their own Batch
        if (battleStage != null) {
            battleStage.act(delta);
            battleStage.draw();
        }
    }

    private void drawBox2DDebug(Batch batch) {
        batch.end();
        Matrix4 debugMatrix = new Matrix4(getCamera().combined);
        debugMatrix.scale(1f, 1f, 1f);
        debugRenderer.setDrawBodies(true);
        debugRenderer.render(worldManager.getWorld(), debugMatrix);
        batch.begin();
    }

    public void drawDebugSimpleBox2(SimpleBox2 simpleBox2, ShapeRenderer shapeRenderer, Batch batch) {
        batch.end();
        shapeRenderer.setProjectionMatrix(new Matrix4(getCamera().combined));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(simpleBox2.getX(), simpleBox2.getY(), simpleBox2.getWidth(), simpleBox2.getHeight());
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void dispose() {
        worldManager.disposeWorld();
        getGame().getInputMultiplexer().removeProcessor(generalInputProcessor);
        getGame().getInputMultiplexer().removeProcessor(battleStage);
        battleStage.dispose();
    }

    public BattleStage getGUI() { return battleStage; }

    public Battle getBattle() { return battle; }

    public WorldManager getWorldManager() { return worldManager; }

    public BattleStage getBattleStage() { return battleStage; }

}
