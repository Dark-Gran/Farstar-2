package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.gui.tokens.Herald;
import com.darkgran.farstar.gui.tokens.MothershipToken;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.gui.tokens.TokenZoom1v1;
import com.darkgran.farstar.gui.ButtonWithExtraState;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class BattleStage1V1 extends BattleStage {
    private final ResourceMeter resourceMeter1;
    private final ResourceMeter resourceMeter2;
    private final MothershipToken mothershipToken1;
    private final MothershipToken mothershipToken2;
    private final YardMenu yardMenu1;
    private final YardMenu yardMenu2;
    private final HandMenu handMenu1;
    private final HandMenu handMenu2;
    private final FleetMenu fleetMenu1;
    private final FleetMenu fleetMenu2;
    private final JunkButton junkButton1;
    private final JunkButton junkButton2;
    private final SupportMenu supportMenu1;
    private final SupportMenu supportMenu2;
    private final CardSource deck1;
    private final CardSource deck2;
    private final TierCounter tier1;
    private final TierCounter tier2;
    private final ButtonWithExtraState yardButton1 = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/yard.png"), Farstar.ASSET_LIBRARY.get("images/yardO.png"), Farstar.ASSET_LIBRARY.get("images/yardP.png"), Farstar.ASSET_LIBRARY.get("images/yardOP.png")){
        @Override
        public void clicked() {
            yardMenu1.switchVisibility();
        }
    };
    private final ButtonWithExtraState yardButton2 = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/yard.png"), Farstar.ASSET_LIBRARY.get("images/yardO.png"), Farstar.ASSET_LIBRARY.get("images/yardP.png"), Farstar.ASSET_LIBRARY.get("images/yardOP.png")){
        @Override
        public void clicked() {
            yardMenu2.switchVisibility();
        }
    };

    public BattleStage1V1(Farstar game, Viewport viewport, BattleScreen battleScreen, CombatMenu combatMenu, BattlePlayer battlePlayer1, BattlePlayer battlePlayer2) {
        super(game, viewport, battleScreen, combatMenu);
        //Resources
        resourceMeter1 = new ResourceMeter(getBattleScreen().getBattle(), battlePlayer1, true, Farstar.STAGE_WIDTH*0.758f, Farstar.STAGE_HEIGHT*0.04f);
        resourceMeter2 = new ResourceMeter(getBattleScreen().getBattle(), battlePlayer2, false, Farstar.STAGE_WIDTH*0.758f, Farstar.STAGE_HEIGHT*0.97f);
        //Buttons
        turnButton.setPosition(1828f, 478f - turnButton.getHeight() * 0.5f);
        addActor(turnButton);
        //Tiers
        tier1 = new TierCounter(getBattleScreen().getBattle(), Farstar.STAGE_WIDTH*0.079f, Farstar.STAGE_HEIGHT*0.064f);
        tier2 = new TierCounter(getBattleScreen().getBattle(), Farstar.STAGE_WIDTH*0.079f, Farstar.STAGE_HEIGHT*0.948f);
        //Discards / Junkpiles
        junkButton1 = new JunkButton(Farstar.STAGE_WIDTH*0.871f, Farstar.STAGE_HEIGHT*0.15f, this, battlePlayer1);
        junkButton2 = new JunkButton(Farstar.STAGE_WIDTH*0.871f, Farstar.STAGE_HEIGHT*0.85f - TokenType.JUNK.getHeight(), this, battlePlayer2);
        battlePlayer1.getJunkpile().setJunkButton(junkButton1);
        battlePlayer2.getJunkpile().setJunkButton(junkButton2);
        addDropTarget(junkButton1);
        addDropTarget(junkButton2);
        //Fleets
        fleetMenu1 = new FleetMenu(battlePlayer1.getFleet(), Farstar.STAGE_WIDTH*0.066f, Farstar.STAGE_HEIGHT*0.252f, Farstar.STAGE_WIDTH*0.87f, Farstar.STAGE_HEIGHT*0.25f, this, battlePlayer1, false);
        fleetMenu2 = new FleetMenu(battlePlayer2.getFleet(), Farstar.STAGE_WIDTH*0.066f, Farstar.STAGE_HEIGHT*0.506f, Farstar.STAGE_WIDTH*0.87f, Farstar.STAGE_HEIGHT*0.25f, this, battlePlayer2, true);
        addActor(fleetMenu1);
        addActor(fleetMenu2);
        //Supports+Supports
        supportMenu1 = new SupportMenu(battlePlayer1.getSupports(), Farstar.STAGE_WIDTH*0.082f, Farstar.STAGE_HEIGHT*0.11f, 63f, 10f, Farstar.STAGE_WIDTH * 0.838f, Farstar.STAGE_HEIGHT*0.14f, false, this, battlePlayer1);
        supportMenu2 = new SupportMenu(battlePlayer2.getSupports(), Farstar.STAGE_WIDTH*0.082f, Farstar.STAGE_HEIGHT*0.75f, 63f, 30f, Farstar.STAGE_WIDTH * 0.838f, Farstar.STAGE_HEIGHT*0.14f, true, this, battlePlayer2);
        mothershipToken1 = new MothershipToken(battlePlayer1.getMs(), (Farstar.STAGE_WIDTH - TokenType.MS.getWidth()) * 0.5f, Farstar.STAGE_HEIGHT * 0.1f, this, null, supportMenu1);
        mothershipToken2 = new MothershipToken(battlePlayer2.getMs(), (Farstar.STAGE_WIDTH - TokenType.MS.getWidth()) * 0.5f, (Farstar.STAGE_HEIGHT * 0.91f) - TokenType.MS.getHeight(), this, null, supportMenu2);
        addDropTarget(mothershipToken2);
        addDropTarget(fleetMenu1);
        addDropTarget(fleetMenu2);
        addDropTarget(mothershipToken1);
        addDropTarget(supportMenu1);
        addDropTarget(supportMenu2);
        //Decks
        deck1 = new CardSource(Farstar.STAGE_WIDTH*0.96f, Farstar.STAGE_HEIGHT*0.4f,this, battlePlayer1, true);
        deck2 = new CardSource(Farstar.STAGE_WIDTH*0.96f, Farstar.STAGE_HEIGHT*0.559f, this, battlePlayer2, false);
        addActor(deck1);
        addActor(deck2);
        //Shipyards
        yardMenu1 = new YardMenu(battlePlayer1.getShipyard(), false, 194f, Farstar.STAGE_HEIGHT*0.078f, this, battlePlayer1);
        yardButton1.setPosition(Farstar.STAGE_WIDTH*0.094f, Farstar.STAGE_HEIGHT*0.029f);
        battlePlayer1.getYard().setYardButton(yardButton1);
        addActor(yardButton1);
        yardMenu2 = new YardMenu(battlePlayer2.getShipyard(), true, 194f, Farstar.STAGE_HEIGHT*0.76f, this, battlePlayer2);
        yardButton2.setPosition(Farstar.STAGE_WIDTH*0.094f, Farstar.STAGE_HEIGHT*0.91f);
        battlePlayer2.getYard().setYardButton(yardButton2);
        addActor(yardButton2);
        //Hands
        handMenu1 = new HandMenu(battlePlayer1.getHand(),Farstar.STAGE_WIDTH*0.5f, -Farstar.STAGE_HEIGHT*0.35f, this, battlePlayer1, true);
        handMenu2 = new HandMenu(battlePlayer2.getHand(),Farstar.STAGE_WIDTH*0.5f, Farstar.STAGE_HEIGHT*0.95f, this, battlePlayer2, false);
        addDropTarget(handMenu1);
        addDropTarget(handMenu2);
        //TokenZoom
        createTopActors();
    }

    @Override
    protected void createTopActors() {
        setCardZoom(new TokenZoom1v1(null, 0, 0, this, null, 30));
        setHerald(new Herald(null, Farstar.STAGE_WIDTH*0.09f, Farstar.STAGE_HEIGHT*0.38f, this, null, 210));
        addActor(getHerald());
    }

    @Override
    public void updateDeckInfos() {
        deck1.update();
        deck2.update();
    }

    @Override
    public void drawBattleStage(float delta, Batch batch) {
        drawBottomActors(batch);
        resourceMeter1.draw(batch);
        resourceMeter2.draw(batch);
        deck1.draw(batch, getBattleScreen().getShapeRenderer());
        deck2.draw(batch, getBattleScreen().getShapeRenderer());
        tier1.drawText(batch);
        tier2.drawText(batch);
        mothershipToken1.draw(batch);
        mothershipToken2.draw(batch);
        junkButton1.draw(batch);
        junkButton2.draw(batch);
        fleetMenu1.drawTokens(batch);
        fleetMenu2.drawTokens(batch);
        supportMenu1.drawTokens(batch);
        supportMenu2.drawTokens(batch);
        if (yardMenu1.isOpen()) { yardMenu1.drawTokens(batch); }
        if (yardMenu2.isOpen()) { yardMenu2.drawTokens(batch); }
        handMenu1.drawTokens(batch);
        handMenu2.drawTokens(batch);
        super.drawBattleStage(delta, batch);
        if (DEBUG_RENDER) {
            getBattleScreen().drawDebugSimpleBox2(fleetMenu1.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugSimpleBox2(fleetMenu2.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugSimpleBox2(supportMenu1.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugSimpleBox2(supportMenu2.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugBox(handMenu1.getClickListenerActor().getX(), handMenu1.getClickListenerActor().getY(), handMenu1.getClickListenerActor().getWidth(), handMenu1.getClickListenerActor().getHeight(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugBox(handMenu2.getClickListenerActor().getX(), handMenu2.getClickListenerActor().getY(), handMenu2.getClickListenerActor().getWidth(), handMenu2.getClickListenerActor().getHeight(), getBattleScreen().getShapeRenderer(), batch);
        }
    }

    @Override
    public void dispose() {
        yardButton1.remove();
        yardButton2.remove();
        yardButton1.dispose();
        yardButton2.dispose();
        deck1.remove();
        deck2.remove();
        super.dispose();
    }
}
