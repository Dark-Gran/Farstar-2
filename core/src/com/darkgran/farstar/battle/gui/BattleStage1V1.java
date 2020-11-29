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
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.Player;

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

    public BattleStage1V1(Farstar game, Viewport viewport, BattleScreen battleScreen, Player player1, Player player2) {
        super(game, viewport, battleScreen);
        //Resources
        resourceMeter1 = new ResourceMeter(player1, true, Farstar.STAGE_WIDTH, 0f);
        resourceMeter2 = new ResourceMeter(player2, false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        //Motherships
        mothershipToken1 = new MothershipToken(player1.getMs(), Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT*1/4, this, null);
        mothershipToken2 = new MothershipToken(player2.getMs(), Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT*3/4, this, null);
        //Buttons
        turnButton.setBounds(Farstar.STAGE_WIDTH*6/7, Farstar.STAGE_HEIGHT/2, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(turnButton);
        //Shipyards
        yardMenu1 = new YardMenu(player1.getShipyard(), false, Farstar.STAGE_WIDTH*1/9, Farstar.STAGE_HEIGHT*1/5, this);
        yardButton1.setBounds(Farstar.STAGE_WIDTH*1/9, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(yardButton1);
        yardMenu2 = new YardMenu(player2.getShipyard(), true, Farstar.STAGE_WIDTH*1/9, Farstar.STAGE_HEIGHT*4/5, this);
        yardButton2.setBounds(Farstar.STAGE_WIDTH*1/9, Farstar.STAGE_HEIGHT*4/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        this.addActor(yardButton2);
        //Hands
        handMenu1 = new HandMenu(player1.getHand(),Farstar.STAGE_WIDTH*1/12, Farstar.STAGE_HEIGHT*1/8, this);
        handMenu2 = new HandMenu(player2.getHand(),Farstar.STAGE_WIDTH*1/12, Farstar.STAGE_HEIGHT*7/8, this);
        //Fleets
        fleetMenu1 = new FleetMenu(player1.getFleet(), Farstar.STAGE_WIDTH*1/3, Farstar.STAGE_HEIGHT*1/3, Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT/5, this);
        fleetMenu2 = new FleetMenu(player2.getFleet(), Farstar.STAGE_WIDTH*1/3, Farstar.STAGE_HEIGHT*2/3, Farstar.STAGE_WIDTH/2, Farstar.STAGE_HEIGHT/5, this);
        //Finish
        setupListeners();
    }

    @Override
    public void processDrop(float x, float y, Token token) {
        if (isInBox(fleetMenu1, x, y)) {
            getBattleScreen().getBattle().getRoundManager().fleetDeployment(token, fleetMenu1.getFleet(), getDropPosition(x, y, fleetMenu1));
        } else if (isInBox(fleetMenu2, x, y)) {
            getBattleScreen().getBattle().getRoundManager().fleetDeployment(token, fleetMenu2.getFleet(), getDropPosition(x, y, fleetMenu2));
        } else  if (token instanceof HandToken) {
            ((HandToken) token).resetPosition();
        }
        if (token instanceof FakeToken) {
            token.destroy();
        }
    }

    @Override
    public void drawBattleStage(float delta, Batch batch) {
        super.drawBattleStage(delta, batch);
        resourceMeter2.draw(batch);
        resourceMeter1.draw(batch);
        mothershipToken1.draw(batch);
        mothershipToken2.draw(batch);
        if (yardMenu1.isVisible()) { drawTokenMenu(yardMenu1, batch); }
        if (yardMenu2.isVisible()) { drawTokenMenu(yardMenu2, batch); }
        drawTokenMenu(handMenu1, batch);
        drawTokenMenu(handMenu2, batch);
        drawFleet(fleetMenu1, batch);
        drawFleet(fleetMenu2, batch);
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
        yardButton2.removeListener(yardButton2.getClickListener());
        super.dispose();
    }
}
