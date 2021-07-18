package com.darkgran.farstar.battle;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.gui.TableStage;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.battle.players.PossibilityAdvisor;
import com.darkgran.farstar.mainscreen.MainScreen;
import com.darkgran.farstar.util.SimpleBox2;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final WorldManager worldManager = new WorldManager();
    private final Battle battle;
    private final BattleStage battleStage;

    private final InputAdapter battleAdapter = new InputAdapter() {
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (button == 1) { //mouse.right
                if (battle.getRoundManager().isLaunched() && battle.getWhoseTurn() instanceof LocalBattlePlayer) {
                    battle.getRoundManager().tryCancel();
                    battle.closeYards();
                }
            }
            battleStage.getCardZoom().reactivate();
            return super.touchUp(screenX, screenY, pointer, button);
        }
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            battleStage.getCardZoom().deactivate(button==1);
            return super.touchDown(screenX, screenY, pointer, button);
        }
    };

    public BattleScreen(final Farstar game, TableStage tableStage, Battle battle, NotificationManager notificationManager, ScreenSettings screenSettings) {
        super(game, notificationManager, screenSettings);
        setTableMenu(tableStage);
        Box2D.init();
        this.battle = battle;
        battle.startingSetup(this,
                new RoundManager(battle, new PossibilityAdvisor()),
                battle.createCombatManager(),
                new AbilityManager(battle)
        );
        battleStage = battle.createBattleStage(game, getViewport(), this);
        battle.getCombatManager().setBattleStage(battleStage);
        battle.getCombatManager().getCombatMenu().setBattleStage(battleStage);
        game.getInputMultiplexer().addProcessor(battleAdapter);
        game.getInputMultiplexer().addProcessor(battleStage);
        battle.getRoundManager().launch();
    }

    @Override
    public void userEscape() {
        getGame().setScreen(new MainScreen(getGame(), getTableMenu(), getNotificationManager(), getScreenSettings()));
    }

    @Override
    protected void drawContent(float delta, Batch batch) {
        battleStage.drawBattleStage(delta, batch);
        //if (DEBUG_RENDER) { drawBox2DDebug(batch); } //draws boundaries of world-bodies
        //worldManager.worldTimer(delta); //world stepping
    }

    @Override
    protected void drawMenus(float delta, Batch batch) { //Stage-menus use their own Batch
        super.drawMenus(delta, batch);
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(simpleBox2.getX(), simpleBox2.getY(), simpleBox2.getWidth(), simpleBox2.getHeight());
        shapeRenderer.end();
        batch.begin();
    }

    public void drawDebugBox(float x, float y, float width, float height, ShapeRenderer shapeRenderer, Batch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void dispose() {
        battle.dispose();
        worldManager.disposeWorld();
        getGame().getInputMultiplexer().removeProcessor(battleAdapter);
        getGame().getInputMultiplexer().removeProcessor(battleStage);
        battleStage.dispose();
        super.dispose();
    }

    public BattleStage getGUI() { return battleStage; }

    public Battle getBattle() { return battle; }

    public WorldManager getWorldManager() { return worldManager; }

    public BattleStage getBattleStage() { return battleStage; }

}
