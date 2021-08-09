package com.darkgran.farstar.battle;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.battle.players.PossibilityAdvisor;
import com.darkgran.farstar.mainscreen.MainScreen;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    //private final WorldManager worldManager = new WorldManager();
    private final Battle battle;
    private final BattleStage battleStage;
    private final BattleType battleType;

    private final InputAdapter battleAdapter = new InputAdapter() {
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            battleStage.getCardZoom().reactivate();
            return super.touchUp(screenX, screenY, pointer, button);
        }
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == 1) { //mouse.right
                if (battle.getRoundManager().isLaunched() && battle.getWhoseTurn() instanceof LocalBattlePlayer) {
                    battle.getRoundManager().tryCancel();
                    battle.closeYards();
                    if (battleStage.getBattleHelp() != null) { battleStage.getBattleHelp().setEnabled(false); }
                }
            }
            battleStage.getCardZoom().deactivate(button==1);
            return super.touchDown(screenX, screenY, pointer, button);
        }
    };

    public BattleScreen(final Farstar game, TableStage tableStage, Battle battle, BattleType battleType) {
        super(game);
        setTableMenu(tableStage);
        Box2D.init();
        this.battle = battle;
        this.battleType = battleType;
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
    }

    @Override
    public void show() {
        super.show();
        if (!battle.getRoundManager().isLaunched()) {
            battle.getRoundManager().launch();
        }
    }

    @Override
    public void userEscape() {
        NotificationManager.clear(Notification.NotificationType.MIDDLE);
        if (!isConcederActive()) {
            if ((getBattleStage().getBattleHelp() != null && getBattleStage().getBattleHelp().isEnabled()) || getBattle().areYardsOpen() || getBattle().getRoundManager().isTargetingActive() || getBattleStage().getAbilityPicker().isActive()) {
                if (getBattle().getRoundManager().isTargetingActive() || getBattleStage().getAbilityPicker().isActive()) { getBattle().getRoundManager().tryCancel(); }
                if (getBattle().areYardsOpen()) { getBattle().closeYards(); }
                if ((getBattleStage().getBattleHelp() != null && getBattleStage().getBattleHelp().isEnabled())) { getBattleStage().getBattleHelp().setEnabled(false); }
            } else {
                String txt = getBattle().isEverythingDisabled() || getBattleType() == BattleType.SIMULATION ? "LEAVE?" : "CONCEDE?";
                String fontPath = "fonts/orbitron36.fnt";
                SimpleVector2 textWH = TextDrawer.getTextWH(Farstar.ASSET_LIBRARY.getAssetManager().get(fontPath, BitmapFont.class), txt);
                setScreenConceder(new YXQuestionBox(
                        ColorPalette.LIGHT,
                        ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f),
                        fontPath,
                        txt,
                        Farstar.STAGE_WIDTH / 2f - textWH.x / 2,
                        Farstar.STAGE_HEIGHT / 2f + textWH.y / 2 + textWH.y * 2,
                        textWH.x * 1.5f,
                        textWH.y * 5.75f,
                        false,
                        battleStage,
                        this::userEscape,
                        () -> getGame().setScreen(new MainScreen(getGame(), getTableMenu()))
                ));
            }
        } else {
            hideScreenConceder();
        }
    }

    @Override
    protected void drawContent(float delta, Batch batch) {
        super.drawContent(delta, batch);
        battleStage.drawBattleStage(delta, batch);
        //if (DEBUG_RENDER) { drawBox2DDebug(batch); } //draws boundaries of world-bodies
        //worldManager.worldTimer(delta); //world stepping
    }

    @Override
    protected void drawMenus(float delta, Batch batch) { //Stage-menus use their own Batch
        super.drawMenus(delta, batch);
        battleStage.act(delta);
        battleStage.draw();
    }

    @Override
    protected void drawTopContent(float delta, Batch batch) {
        super.drawTopContent(delta, batch);
        battleStage.drawTopBattleStage(delta, batch);
        battleStage.drawZoomed(batch);
        battleStage.drawBattleHelp(batch);
    }

    private void drawBox2DDebug(Batch batch) {
        batch.end();
        Matrix4 debugMatrix = new Matrix4(getCamera().combined);
        debugMatrix.scale(1f, 1f, 1f);
        debugRenderer.setDrawBodies(true);
        //debugRenderer.render(worldManager.getWorld(), debugMatrix);
        batch.begin();
    }

    public static void drawDebugSimpleBox2(SimpleBox2 simpleBox2, ShapeRenderer shapeRenderer, Batch batch) {
        drawDebugBox(simpleBox2.x, simpleBox2.y, simpleBox2.getWidth(), simpleBox2.getHeight(), shapeRenderer, batch);
    }

    public static void drawDebugBox(float x, float y, float width, float height, ShapeRenderer shapeRenderer, Batch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void dispose() {
        battle.dispose();
        //worldManager.disposeWorld();
        getGame().getInputMultiplexer().removeProcessor(battleAdapter);
        getGame().getInputMultiplexer().removeProcessor(battleStage);
        battleStage.dispose();
        super.dispose();
    }

    public BattleStage getGUI() { return battleStage; }

    public Battle getBattle() { return battle; }

    //public WorldManager getWorldManager() { return worldManager; }

    public BattleStage getBattleStage() { return battleStage; }

    public BattleType getBattleType() {
        return battleType;
    }
}
