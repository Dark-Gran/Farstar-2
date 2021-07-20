package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.tokens.*;
import com.darkgran.farstar.gui.ButtonWithExtraState;
import com.darkgran.farstar.gui.ListeningStage;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;

import java.awt.*;
import java.util.ArrayList;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private FakeToken fakeToken;
    private final CombatMenu combatMenu;
    private ArrayList<DropTarget> dropTargets = new ArrayList<>();
    private final AbilityPicker abilityPicker;
    private final RoundCounter roundCounter;
    public final ButtonWithExtraState turnButton = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/turn.png"), Farstar.ASSET_LIBRARY.get("images/turnO.png"), Farstar.ASSET_LIBRARY.get("images/turnP.png"), Farstar.ASSET_LIBRARY.get("images/turnOP.png")){
        private final Texture turnBackground = Farstar.ASSET_LIBRARY.get("images/turn_b.png");
        private final Texture turnCombat = Farstar.ASSET_LIBRARY.get("images/turn_combat.png");
        private final Texture turnWait = Farstar.ASSET_LIBRARY.get("images/turn_wait.png");
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
            if (battleScreen.getBattle().getWhoseTurn() instanceof LocalBattlePlayer) {
                battleScreen.getBattle().getRoundManager().endTurn();
            }
        }
    };
    private final ButtonWithExtraState combatEndButton = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/combat_end.png"), Farstar.ASSET_LIBRARY.get("images/combat_endO.png"), Farstar.ASSET_LIBRARY.get("images/combat_endA.png"), Farstar.ASSET_LIBRARY.get("images/combat_endAO.png")) {
        @Override
        public void clicked() {
            battleScreen.getBattle().getCombatManager().startTacticalPhase();
        }
    };
    private TokenZoom cardZoom;
    private Herald herald;
    private ShotManager shotManager = new ShotManager();


    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen, CombatMenu combatMenu) {
        super(game, viewport);
        this.battleScreen = battleScreen;
        this.combatMenu = combatMenu;
        combatEndButton.setPosition(Farstar.STAGE_WIDTH*0.8f, Farstar.STAGE_HEIGHT*0.3f);
        combatEndButton.setDisabled(true);
        abilityPicker = new AbilityPicker(Farstar.STAGE_WIDTH/2f+5f, Farstar.STAGE_HEIGHT*0.27f, this, null, Farstar.ASSET_LIBRARY.get("images/yard.png"));
        battleScreen.getBattle().getRoundManager().setAbilityPicker(abilityPicker);
        roundCounter = new RoundCounter(Farstar.STAGE_WIDTH*0.003f, Farstar.STAGE_HEIGHT*0.475f, this, getBattleScreen().getBattle());
    }

    protected void createTopActors() { } /** MUST be called in constructor (as the very last thing) and set cardZoom+herald! */

    public void drawBottomActors(Batch batch) {
        combatMenu.drawDuels(batch, getBattleScreen().getShapeRenderer());
    }

    public void drawBattleStage(float delta, Batch batch) {
        roundCounter.draw(batch);
        shotManager.drawAttacks(batch, delta);
        abilityPicker.draw(batch);
        if (fakeToken != null) { fakeToken.draw(batch, getBattleScreen().getShapeRenderer()); }
        cardZoom.draw(batch);
        herald.draw(batch);
    }

    @Override
    public void act(float delta) {
        cardZoom.update(delta);
        herald.update(delta);
        super.act(delta);
    }

    public void updateDeckInfos() { }

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

    public DropTarget returnDropTarget(float x, float y) {
        for (DropTarget dropTarget : dropTargets) {
            if (isInBox(dropTarget.getSimpleBox2(), x, y)) { return dropTarget; }
        }
        return null;
    }

    public void addDropTarget(DropTarget dropTarget) {
        dropTargets.add(dropTarget);
    }

    public void processDrop(float x, float y, Token token) {
        if (token != null) {
            CombatManager combatManager = getBattleScreen().getBattle().getCombatManager();
            DropTarget targetHit = returnDropTarget(x, y);
            if (targetHit instanceof MothershipToken && token.getCard().getCardInfo().getCardType() == CardType.SUPPORT) {
                targetHit = ((MothershipToken) targetHit).getSupportMenu();
            }
            if (targetHit != null || CardType.isSpell(token.getCard().getCardInfo().getCardType())) {
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

    public int getRoundDropPosition(float x, float y, DropTarget dropTarget, CardType cardType) {
        if (dropTarget instanceof FleetMenu) {
            return getFleetDropPosition(x, y, (FleetMenu) dropTarget, CardType.isShip(cardType), true);
        } else if (dropTarget instanceof SupportMenu) {
            return getSupportDropPosition(x, y, (SupportMenu) dropTarget);
        } else if (dropTarget instanceof JunkButton) {
            return 8;
        }
        return -1;
    }

    public Token getCombatDropToken(float x, float y, DropTarget dropTarget) {
        if (dropTarget instanceof MothershipToken) {
            return ((MothershipToken) dropTarget);
        } else if (dropTarget instanceof FleetMenu) {
            int pos = getFleetDropPosition(x, y, (FleetMenu) dropTarget, false, false);
            Token[] ships = ((FleetMenu) dropTarget).getFleetTokens();
            if (pos > 0 && pos < ships.length && ships[pos] != null) {
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
            SimpleVector2 lr = fleetMenu.getFleet().getSideSizes(fleetMenu.getFleet().getShips());
            int count = fleetMenu.getFleet().countShips();
            if (!deployment || count % 2 != 0) {
                if (lr.getX() > lr.getY()) {
                    shift = fleetMenu.getOffset() / 2;
                } else if ((!deployment && lr.getX() < lr.getY()) || (deployment && lr.getX() <= lr.getY())) {
                    shift = -fleetMenu.getOffset() / 2;
                }
            }
            for (int i = 0; i < 8; i++) {
                if (x > shift + fleetMenu.getX() + fleetMenu.getOffset() * i && x < shift + fleetMenu.getX() + fleetMenu.getOffset() * (i + 1)) {
                    if (deployment) {
                        if (count != 6) {
                            if (lr.getX() > lr.getY() && (i < 3 && i > 0)) {
                                i--;
                            } else if (lr.getX() < lr.getY() && i > 3 && i < 6) {
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
            if (x > supportMenu.getX() + (supportMenu.getOffset() * i) && x < supportMenu.getX() + (supportMenu.getOffset() * (8))) {
                if (i != 3) {
                    return i;
                } else {
                    if (x < supportMenu.getX() + supportMenu.getWidth() / 2) {
                        return 2;
                    } else {
                        return 4;
                    }
                }
            }
        }
        return -1;
    }

    public boolean isInBox(SimpleBox2 simpleBox2, float x, float y) {
        Rectangle rectangle = new Rectangle((int) simpleBox2.getX(), (int) simpleBox2.getY(), (int) simpleBox2.getWidth(), (int) simpleBox2.getHeight());
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

    public void setShotManager(ShotManager shotManager) {
        this.shotManager = shotManager;
    }
}
