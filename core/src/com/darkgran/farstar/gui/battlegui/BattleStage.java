package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.gui.tokens.*;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.cards.CardType;

import java.awt.*;
import java.util.ArrayList;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private FakeToken fakeToken;
    private final CombatMenu combatMenu;
    private final ArrayList<DropTarget> dropTargets = new ArrayList<>();
    private final AbilityPicker abilityPicker;
    private final RoundCounter roundCounter;
    public final ButtonWithExtraState turnButton = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.getAtlasRegion("turn"), Farstar.ASSET_LIBRARY.getAtlasRegion("turnO"), Farstar.ASSET_LIBRARY.getAtlasRegion("turnP"), Farstar.ASSET_LIBRARY.getAtlasRegion("turnOP")){
        private final TextureRegion turnBackground = Farstar.ASSET_LIBRARY.getAtlasRegion("turn-b");
        private final TextureRegion turnCombat = Farstar.ASSET_LIBRARY.getAtlasRegion("turn-combat");
        private final TextureRegion turnWait = Farstar.ASSET_LIBRARY.getAtlasRegion("turn-wait");
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(turnBackground, getX(), getY());
            if (!isDisabled()) {
                super.draw(batch, parentAlpha);
            } else {
                if (getBattleScreen().getBattle().getCombatManager().isActive()) {
                    batch.draw(turnCombat, getX(), getY());
                } else {
                    batch.draw(turnWait, getX(), getY());
                }
            }
        }
        @Override
        public void clicked() {
            if (isEnabled() && battleScreen.getBattle().getWhoseTurn() instanceof LocalBattlePlayer) {
                battleScreen.getBattle().getRoundManager().endTurn();
                battleScreen.hideScreenConceder();
            }
        }
    };
    private final ButtonWithExtraState combatEndButton = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.getAtlasRegion("combat-end"), Farstar.ASSET_LIBRARY.getAtlasRegion("combat-endO"), Farstar.ASSET_LIBRARY.getAtlasRegion("combat-endA"), Farstar.ASSET_LIBRARY.getAtlasRegion("combat-endAO")) {
        @Override
        public void clicked() {
            battleScreen.hideScreenConceder();
            battleScreen.getBattle().getCombatManager().startTacticalPhase();
        }
    };
    private TokenZoom cardZoom;
    private Herald herald;
    private final ShotManager shotManager = new ShotManager();
    private final AnimationManager animationManager = new AnimationManager();
    private BattleHelp battleHelp;
    private final ActorButton f1button = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("f1"), Farstar.ASSET_LIBRARY.getAtlasRegion("f1O")){
        //private SimpleImage2 bck = new SimpleImage2(0, 0, Farstar.ASSET_LIBRARY.getAtlasRegion("f1back"));
        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x, y);
            //bck.x = x;
            //bck.y = y;
        }
        /*@Override
        public void draw(Batch batch, float parentAlpha) {
            bck.draw(batch);
            super.draw(batch, parentAlpha);
        }*/
        @Override
        public void clicked() {
            if (getBattleHelp() != null) {
                toggleBattleHelp();
            }
        }
    };
    public void f1ButtonToNet() { //used to connect Net and F1 button (makes sense only if the default for Net is On)
        setF1ButtonVisibility(getBattleScreen().isNetEnabled());
    }
    public void toggleF1Button() {
        setF1ButtonVisibility(!getActors().contains(f1button, true));
    }
    public void setF1ButtonVisibility(boolean enable) {
        if (enable) {
            addActor(f1button);
        } else {
            f1button.remove();
        }
    }


    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen, CombatMenu combatMenu) {
        super(game, viewport);
        this.battleScreen = battleScreen;
        this.combatMenu = combatMenu;
        combatEndButton.setPosition(Farstar.STAGE_WIDTH*0.8f, Farstar.STAGE_HEIGHT*0.3f);
        combatEndButton.setDisabled(true);
        abilityPicker = new AbilityPicker(Farstar.STAGE_WIDTH/2f+5f, Farstar.STAGE_HEIGHT*0.27f, this, null);
        battleScreen.getBattle().getRoundManager().setAbilityPicker(abilityPicker);
        roundCounter = new RoundCounter(Farstar.STAGE_WIDTH*0.003f, Farstar.STAGE_HEIGHT*0.475f, getBattleScreen().getBattle());
    }

    protected void createTopActors() { } /** MUST be called in constructor (as the very last thing) and set cardZoom+herald! */

    public void updateDeckInfos() { }

    public void dropDownHands() { }

    public void drawBottomActors(float delta, Batch batch) {
        combatMenu.drawDuels(batch, getBattleScreen().getShapeRenderer());
        animationManager.draw(batch, delta, true);
    }

    public void drawBattleStage(float delta, Batch batch) {
        roundCounter.draw(batch);
        animationManager.draw(batch, delta, false);
        shotManager.drawAttacks(batch, delta, false);
        abilityPicker.draw(batch);
        herald.draw(batch);
    }

    public void drawTopBattleStage(float delta, Batch batch) { }

    public void drawZoomed(Batch batch) {
        if (fakeToken != null) { fakeToken.draw(batch, getBattleScreen().getShapeRenderer()); }
        cardZoom.draw(batch);
    }

    public void drawBattleHelp(Batch batch) {
        if (battleHelp != null) {
            battleHelp.draw(batch);
        }
    }

    public void toggleBattleHelp() {
        if (battleHelp != null) {
            battleHelp.setEnabled(!battleHelp.isEnabled());
            getBattleScreen().getBattle().closeYards();
            getBattleScreen().hideScreenConceder();
            dropDownHands();
        }
    }

    @Override
    public void act(float delta) {
        cardZoom.update(delta);
        herald.update(delta);
        super.act(delta);
    }

    public void enableCombatEnd() {
        if (battleScreen.getBattle().getWhoseTurn() instanceof LocalBattlePlayer) {
            combatEndButton.setDisabled(false);
            addActor(combatEndButton);
        }
    }

    public void disableCombatEnd() {
        combatEndButton.setDisabled(true);
        combatEndButton.remove();
    }

    public boolean coordsOverFleetMenus(float x, float y) {
        return false;
    }

    public static boolean coordsOverClickTokens(ClickToken[] clickTokens, float x, float y) {
        for (ClickToken clickToken : clickTokens) {
            if (clickToken != null && isInBox(new SimpleBox2(clickToken.getX(), clickToken.getY(), clickToken.getWidth(), clickToken.getHeight()), x, y)) {
                return true;
            }
        }
        return false;
    }

    public void processDrop(float x, float y, Token token) {
        if (token != null) {
            CombatManager combatManager = getBattleScreen().getBattle().getCombatManager();
            DropTarget targetHit = returnDropTarget(x, y);
            //Deploy-Retargeting
            if (!(token instanceof TargetingToken)) {
                if (token.getCard().getCardInfo().getCardType() == CardType.SUPPORT) {
                    if (targetHit instanceof MothershipToken) {
                        targetHit = ((MothershipToken) targetHit).getSupportMenu();
                    } else if (targetHit instanceof FleetMenu) {
                        targetHit = token.getCard().getBattlePlayer().getSupports().getSupportMenu();
                    }
                }
                if (CardType.isShip(token.getCard().getCardInfo().getCardType())) {
                    if (targetHit instanceof FleetMenu) {
                        targetHit = token.getCard().getBattlePlayer().getFleet().getFleetMenu();
                    }
                }
            }
            //Process
            if (!getBattleScreen().isConcederActive() && (targetHit != null || CardType.isSpell(token.getCard().getCardInfo().getCardType()))) {
                if (CardType.isShip(token.getCard().getCardInfo().getCardType()) && combatManager.isActive() && !combatManager.isTacticalPhase() && !combatManager.getDuelManager().isActive()) {
                    combatManager.processDrop(token instanceof TargetingToken ? token.getCard().getToken() : token, getCombatDropToken(x, y, targetHit));
                } else if (!combatManager.getDuelManager().isActive()) {
                    getBattleScreen().getBattle().getRoundManager().processDrop(token, targetHit, getRoundDropPosition(x, y, targetHit, token.getCard().getCardInfo().getCardType()), false, true);
                }
            } else if (token instanceof AnchoredToken) {
                ((AnchoredToken) token).resetPosition();
            }
            if (token instanceof FakeToken) {
                token.destroy();
            }
        }
    }

    public void addDropTarget(DropTarget dropTarget) {
        dropTargets.add(dropTarget);
    }

    public DropTarget returnDropTarget(float x, float y) {
        for (DropTarget dropTarget : dropTargets) {
            if (isInBox(dropTarget.getSimpleBox2(), x, y)) { return dropTarget; }
        }
        return null;
    }

    public int getRoundDropPosition(float x, float y, DropTarget dropTarget, CardType cardType) {
        if (dropTarget instanceof JunkButton) {
            return 8;
        } else if (dropTarget instanceof FleetMenu) {
            return getFleetDropPosition(x, y, (FleetMenu) dropTarget, CardType.isShip(cardType), true);
        } else if (dropTarget instanceof SupportMenu) {
            return getSupportDropPosition(x, y, (SupportMenu) dropTarget);
        }
        return -1;
    }

    public Token getCombatDropToken(float x, float y, DropTarget dropTarget) {
        if (dropTarget instanceof MothershipToken) {
            return ((MothershipToken) dropTarget);
        } else if (dropTarget instanceof FleetMenu) {
            int pos = getFleetDropPosition(x, y, (FleetMenu) dropTarget, false, false);
            Token[] ships = ((FleetMenu) dropTarget).getFleetTokens();
            if (pos >= 0 && pos < ships.length && ships[pos] != null) {
                return ships[pos];
            }
        }
        return null;
    }

    public int getFleetDropPosition(float x, float y, FleetMenu fleetMenu, boolean force3, boolean deployment) {
        Token[] ships = fleetMenu.getFleetTokens();
        if (force3 && ships[3] == null) { //middle token empty
            return 3;
        } else {
            float shift = 0f;
            SimpleVector2 lr = Fleet.getSideSizes(fleetMenu.getFleet().getShips());
            int count = fleetMenu.getFleet().countShips();
            if (!deployment || count % 2 != 0) {
                if (lr.x > lr.y) {
                    shift = fleetMenu.getOffset() / 2;
                } else if ((!deployment && lr.x < lr.y) || (deployment && lr.x <= lr.y)) {
                    shift = -fleetMenu.getOffset() / 2;
                }
            }
            for (int i = 0; i < 8; i++) {
                if (x > shift + fleetMenu.getX() + fleetMenu.getOffset() * i && x < shift + fleetMenu.getX() + fleetMenu.getOffset() * (i + 1)) {
                    if (deployment) {
                        if (count != 6) {
                            if (lr.x > lr.y && (i < 3 && i > 0)) {
                                i--;
                            } else if (lr.x < lr.y && i > 3 && i < 6) {
                                i++;
                            } else if (count % 2 != 0) {
                                if (i < 3 && i > 0) {
                                    i--;
                                }
                            }
                        }
                    }
                    return MathUtils.clamp(i, 0, 6);
                }
            }
            return -1;
        }
    }

    public int getSupportDropPosition(float x, float y, SupportMenu supportMenu) {
        for (int i = 0; i < 7; i++) {
            if (x > supportMenu.x + (supportMenu.getOffset() * i) && x < supportMenu.x + (supportMenu.getOffset() * (8))) {
                if (i != 3) {
                    return i;
                } else {
                    if (x < supportMenu.x + supportMenu.getWidth() / 2) {
                        return 2;
                    } else {
                        return 4;
                    }
                }
            }
        }
        return -1;
    }

    public static boolean isInBox(SimpleBox2 simpleBox2, float x, float y) {
        Rectangle rectangle = new Rectangle((int) simpleBox2.x, (int) simpleBox2.y, (int) simpleBox2.getWidth(), (int) simpleBox2.getHeight());
        return rectangle.contains(x, y);
    }

    @Override
    public void dispose() {
        turnButton.removeListener(turnButton.getClickListener());
        combatEndButton.remove();
        abilityPicker.dispose();
        shotManager.dispose();
        super.dispose();
    }

    public void setFakeToken(FakeToken fakeToken) {
        if (this.fakeToken != null) { this.fakeToken.remove(); }
        this.fakeToken = fakeToken;
        if (fakeToken != null) { this.addActor(fakeToken); }
    }

    public boolean isEnabled() {
        return (battleHelp == null || !battleHelp.isEnabled());
    }

    public FakeToken getFakeToken() { return fakeToken; }

    public BattleScreen getBattleScreen() { return battleScreen; }

    public CombatMenu getDuelMenu() { return combatMenu; }

    public RoundCounter getRoundCounter() {
        return roundCounter;
    }

    public void setCardZoom(TokenZoom cardZoom) {
        this.cardZoom = cardZoom;
    }

    public TokenZoom getCardZoom() {
        return cardZoom;
    }

    public Herald getHerald() {
        return herald;
    }

    public void setHerald(Herald herald) {
        this.herald = herald;
    }

    public ButtonWithExtraState getTurnButton() {
        return turnButton;
    }

    public ButtonWithExtraState getCombatEndButton() { return combatEndButton; }

    public AbilityPicker getAbilityPicker() { return abilityPicker; }

    public ShotManager getShotManager() {
        return shotManager;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public BattleHelp getBattleHelp() {
        return battleHelp;
    }

    public void setBattleHelp(BattleHelp battleHelp) {
        this.battleHelp = battleHelp;
    }

    public ActorButton getF1button() {
        return f1button;
    }
}
