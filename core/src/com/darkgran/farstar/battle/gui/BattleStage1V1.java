package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.gui.tokens.MothershipToken;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class BattleStage1V1 extends BattleStage {
    private final ResourceMeter resourceMeter1;
    private final ResourceMeter resourceMeter2;
    private final MothershipToken mothershipToken1;
    private final MothershipToken mothershipToken2;
    public final ImageButton yardButton1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(getYardPic())));
    private final YardMenu yardMenu1;
    public final ImageButton yardButton2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(getYardPic())));
    private final YardMenu yardMenu2;
    private final HandMenu handMenu1;
    private final HandMenu handMenu2;
    private final FleetMenu fleetMenu1;
    private final FleetMenu fleetMenu2;
    private final JunkButton junkButton1;
    private final JunkButton junkButton2;
    private final SupportMenu supportMenu1;
    private final SupportMenu supportMenu2;

    public BattleStage1V1(Farstar game, Viewport viewport, BattleScreen battleScreen, DuelMenu duelMenu, Player player1, Player player2) {
        super(game, viewport, battleScreen, duelMenu);
        //Resources
        resourceMeter1 = new ResourceMeter(player1, true, Farstar.STAGE_WIDTH, 0f);
        resourceMeter2 = new ResourceMeter(player2, false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        //Motherships
        mothershipToken1 = new MothershipToken(player1.getMs(), (Farstar.STAGE_WIDTH - TokenType.MS.getWidth()) * 0.5f, Farstar.STAGE_HEIGHT * 0.1f, this, null);
        mothershipToken2 = new MothershipToken(player2.getMs(), (Farstar.STAGE_WIDTH - TokenType.MS.getWidth()) * 0.5f, (Farstar.STAGE_HEIGHT * 0.91f) - TokenType.MS.getHeight(), this, null);
        addDropTarget(mothershipToken1);
        addDropTarget(mothershipToken2);
        //Buttons
        turnButton.setBounds(Farstar.STAGE_WIDTH*0.95f, Farstar.STAGE_HEIGHT * 0.5f, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(turnButton);
        //Shipyards
        yardMenu1 = new YardMenu(player1.getShipyard(), false, Farstar.STAGE_WIDTH*0.12f, Farstar.STAGE_HEIGHT*0.12f, this, player1);
        yardButton1.setBounds(Farstar.STAGE_WIDTH*0.12f, Farstar.STAGE_HEIGHT*0.12f, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        //this.addActor(yardButton1);
        yardMenu2 = new YardMenu(player2.getShipyard(), true, Farstar.STAGE_WIDTH*0.12f, Farstar.STAGE_HEIGHT*0.78f, this, player2);
        yardButton2.setBounds(Farstar.STAGE_WIDTH*0.12f, Farstar.STAGE_HEIGHT*0.78f, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        //this.addActor(yardButton2);
        //Hands
        handMenu1 = new HandMenu(player1.getHand(),Farstar.STAGE_WIDTH*0.3f, Farstar.STAGE_HEIGHT*0.01f, this, player1);
        handMenu2 = new HandMenu(player2.getHand(),Farstar.STAGE_WIDTH*0.3f, Farstar.STAGE_HEIGHT*0.95f, this, player2);
        //Fleets
        fleetMenu1 = new FleetMenu(player1.getFleet(), Farstar.STAGE_WIDTH*1/3, Farstar.STAGE_HEIGHT*1/3, Farstar.STAGE_WIDTH*3/7, Farstar.STAGE_HEIGHT/7, this, player1, false);
        fleetMenu2 = new FleetMenu(player2.getFleet(), Farstar.STAGE_WIDTH*1/3, Farstar.STAGE_HEIGHT/2, Farstar.STAGE_WIDTH*3/7, Farstar.STAGE_HEIGHT/7, this, player2, true);
        addDropTarget(fleetMenu1);
        addDropTarget(fleetMenu2);
        //Discards / Junkpiles
        junkButton1 = new JunkButton(Farstar.STAGE_WIDTH*6/7, Farstar.STAGE_HEIGHT*1/3, this, player1);
        junkButton2 = new JunkButton(Farstar.STAGE_WIDTH*6/7, Farstar.STAGE_HEIGHT*2/3, this, player2);
        addDropTarget(junkButton1);
        addDropTarget(junkButton2);
        //Supports
        supportMenu1 = new SupportMenu(player1.getSupports(), Farstar.STAGE_WIDTH*0.082f, Farstar.STAGE_HEIGHT*0.11f, Farstar.STAGE_WIDTH * 0.838f, Farstar.STAGE_HEIGHT*0.14f, false, this, player1);
        supportMenu2 = new SupportMenu(player2.getSupports(), Farstar.STAGE_WIDTH*0.082f, Farstar.STAGE_HEIGHT*0.76f, Farstar.STAGE_WIDTH * 0.838f, Farstar.STAGE_HEIGHT*0.14f, true, this, player2);
        addDropTarget(supportMenu1);
        addDropTarget(supportMenu2);
        //Finish
        setupListeners();
    }

    @Override
    public void drawBattleStage(float delta, Batch batch) {
        super.drawBattleStage(delta, batch);
        //resourceMeter2.draw(batch);
        //resourceMeter1.draw(batch);
        mothershipToken1.draw(batch);
        mothershipToken2.draw(batch);
        //junkButton1.draw(batch);
        //junkButton2.draw(batch);
        //drawFleet(fleetMenu1, batch);
        //drawFleet(fleetMenu2, batch);
        drawTokenMenu(handMenu1, batch);
        drawTokenMenu(handMenu2, batch);
        //if (yardMenu1.isVisible()) { drawTokenMenu(yardMenu1, batch); }
        //if (yardMenu2.isVisible()) { drawTokenMenu(yardMenu2, batch); }
        drawTokenMenu(supportMenu1, batch);
        drawTokenMenu(supportMenu2, batch);
        if (DEBUG_RENDER) { getBattleScreen().drawDebugSimpleBox2(supportMenu1.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch); }
        if (DEBUG_RENDER) { getBattleScreen().drawDebugSimpleBox2(supportMenu2.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch); }
    }

    @Override
    public void setupListeners() {
        yardButton1.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                yardMenu1.switchVisibility();
            }
        });
        yardButton2.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                yardMenu2.switchVisibility();
            }
        });
        super.setupListeners();
    }

    @Override
    public void dispose() {
        yardButton1.removeListener(yardButton1.getClickListener());
        yardButton1.remove();
        yardButton2.removeListener(yardButton2.getClickListener());
        yardButton2.remove();
        super.dispose();
    }
}
