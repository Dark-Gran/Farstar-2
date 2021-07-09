package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.gui.tokens.Herald;
import com.darkgran.farstar.battle.gui.tokens.MothershipToken;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.gui.tokens.TokenZoom1v1;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.ActorButton;
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
    private final ActorButton yardButton1 = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/yard.png"), Farstar.ASSET_LIBRARY.get("images/yardO.png"), Farstar.ASSET_LIBRARY.get("images/yardP.png"), Farstar.ASSET_LIBRARY.get("images/yardOP.png")){
        @Override
        public void clicked() {
            yardMenu1.switchVisibility();
        }
    };
    private final ActorButton yardButton2 = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/yard.png"), Farstar.ASSET_LIBRARY.get("images/yardO.png"), Farstar.ASSET_LIBRARY.get("images/yardP.png"), Farstar.ASSET_LIBRARY.get("images/yardOP.png")){
        @Override
        public void clicked() {
            yardMenu2.switchVisibility();
        }
    };

    public BattleStage1V1(Farstar game, Viewport viewport, BattleScreen battleScreen, DuelMenu duelMenu, Player player1, Player player2) {
        super(game, viewport, battleScreen, duelMenu);
        //Resources
        resourceMeter1 = new ResourceMeter(getBattleScreen().getBattle(), player1, true, Farstar.STAGE_WIDTH*0.779f, Farstar.STAGE_HEIGHT*0.04f);
        resourceMeter2 = new ResourceMeter(getBattleScreen().getBattle(), player2, false, Farstar.STAGE_WIDTH*0.779f, Farstar.STAGE_HEIGHT*0.97f);
        //Buttons
        turnButton.setPosition(1828f, 478f - turnButton.getHeight() * 0.5f);
        addActor(turnButton);
        //Tiers
        tier1 = new TierCounter(getBattleScreen().getBattle(), Farstar.STAGE_WIDTH*0.083f, Farstar.STAGE_HEIGHT*0.064f);
        tier2 = new TierCounter(getBattleScreen().getBattle(), Farstar.STAGE_WIDTH*0.083f, Farstar.STAGE_HEIGHT*0.95f);
        //Discards / Junkpiles
        junkButton1 = new JunkButton(Farstar.STAGE_WIDTH*0.871f, Farstar.STAGE_HEIGHT*0.13f, this, player1);
        junkButton2 = new JunkButton(Farstar.STAGE_WIDTH*0.871f, Farstar.STAGE_HEIGHT*0.88f - TokenType.JUNK.getHeight(), this, player2);
        player1.getJunkpile().setJunkButton(junkButton1);
        player2.getJunkpile().setJunkButton(junkButton2);
        addDropTarget(junkButton1);
        addDropTarget(junkButton2);
        //Fleets
        fleetMenu1 = new FleetMenu(player1.getFleet(), Farstar.STAGE_WIDTH*0.066f, Farstar.STAGE_HEIGHT*0.255f, Farstar.STAGE_WIDTH*0.87f, Farstar.STAGE_HEIGHT*0.25f, this, player1, false);
        fleetMenu2 = new FleetMenu(player2.getFleet(), Farstar.STAGE_WIDTH*0.066f, Farstar.STAGE_HEIGHT*0.505f, Farstar.STAGE_WIDTH*0.87f, Farstar.STAGE_HEIGHT*0.25f, this, player2, true);
        addDropTarget(fleetMenu1);
        addDropTarget(fleetMenu2);
        addActor(fleetMenu1);
        addActor(fleetMenu2);
        //Supports
        supportMenu1 = new SupportMenu(player1.getSupports(), Farstar.STAGE_WIDTH*0.082f, Farstar.STAGE_HEIGHT*0.11f, 63f, 10f, Farstar.STAGE_WIDTH * 0.838f, Farstar.STAGE_HEIGHT*0.14f, false, this, player1);
        supportMenu2 = new SupportMenu(player2.getSupports(), Farstar.STAGE_WIDTH*0.082f, Farstar.STAGE_HEIGHT*0.76f, 63f, 30f, Farstar.STAGE_WIDTH * 0.838f, Farstar.STAGE_HEIGHT*0.14f, true, this, player2);
        addDropTarget(supportMenu1);
        addDropTarget(supportMenu2);
        //Motherships
        mothershipToken1 = new MothershipToken(player1.getMs(), (Farstar.STAGE_WIDTH - TokenType.MS.getWidth()) * 0.5f, Farstar.STAGE_HEIGHT * 0.1f, this, null, supportMenu1);
        mothershipToken2 = new MothershipToken(player2.getMs(), (Farstar.STAGE_WIDTH - TokenType.MS.getWidth()) * 0.5f, (Farstar.STAGE_HEIGHT * 0.91f) - TokenType.MS.getHeight(), this, null, supportMenu2);
        addDropTarget(mothershipToken1);
        addDropTarget(mothershipToken2);
        //Decks
        deck1 = new CardSource(Farstar.STAGE_WIDTH*0.96f, Farstar.STAGE_HEIGHT*0.4f,this, player1, true);
        deck2 = new CardSource(Farstar.STAGE_WIDTH*0.96f, Farstar.STAGE_HEIGHT*0.559f, this, player2, false);
        addActor(deck1);
        addActor(deck2);
        //Shipyards
        yardMenu1 = new YardMenu(player1.getShipyard(), false, 202f, Farstar.STAGE_HEIGHT*0.078f, this, player1);
        yardButton1.setPosition(Farstar.STAGE_WIDTH*0.1f, Farstar.STAGE_HEIGHT*0.029f);
        addActor(yardButton1);
        yardMenu2 = new YardMenu(player2.getShipyard(), true, 202f, Farstar.STAGE_HEIGHT*0.76f, this, player2);
        yardButton2.setPosition(Farstar.STAGE_WIDTH*0.1f, Farstar.STAGE_HEIGHT*0.91f);
        addActor(yardButton2);
        //Hands
        handMenu1 = new HandMenu(player1.getHand(),Farstar.STAGE_WIDTH*0.5f, -Farstar.STAGE_HEIGHT*0.25f, this, player1, true);
        handMenu2 = new HandMenu(player2.getHand(),Farstar.STAGE_WIDTH*0.5f, Farstar.STAGE_HEIGHT*0.95f, this, player2, false);
        //TokenZoom
        createTokenZooms();
    }

    @Override
    protected void createTokenZooms() {
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
            /*getBattleScreen().drawDebugSimpleBox2(fleetMenu1.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugSimpleBox2(fleetMenu2.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugSimpleBox2(supportMenu1.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);
            getBattleScreen().drawDebugSimpleBox2(supportMenu2.getSimpleBox2(), getBattleScreen().getShapeRenderer(), batch);*/
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
