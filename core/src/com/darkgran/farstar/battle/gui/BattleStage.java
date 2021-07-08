package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.tokens.*;
import com.darkgran.farstar.gui.ActorButton;
import com.darkgran.farstar.gui.ButtonWithExtraState;
import com.darkgran.farstar.gui.ListeningStage;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;

import java.awt.*;
import java.util.ArrayList;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private FakeToken fakeToken;
    private final DuelMenu duelMenu;
    private ArrayList<DropTarget> dropTargets = new ArrayList<>();
    private final AbilityPicker abilityPicker;
    private final RoundCounter roundCounter;
    public final ButtonWithExtraState turnButton = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.get("images/turn.png"), Farstar.ASSET_LIBRARY.get("images/turnO.png"), Farstar.ASSET_LIBRARY.get("images/turnP.png"), Farstar.ASSET_LIBRARY.get("images/turnOP.png")){
        @Override
        public void clicked() {
            if (battleScreen.getBattle().getWhoseTurn() instanceof LocalPlayer) {
                battleScreen.getBattle().getRoundManager().endTurn();
            }
        }
    };
    private final ActorButton combatEndButton = new ActorButton(Farstar.ASSET_LIBRARY.get("images/combat_end.png"), Farstar.ASSET_LIBRARY.get("images/combat_endO.png")) {
        @Override
        public void clicked() {
            battleScreen.getBattle().getCombatManager().endCombat();
        }
    };
    private final PrintToken cardZoom = new PrintToken(null, 0, 0, this, null){
        @Override
        public void shiftPosition() {
            if (getTargetXY() != null) {
                float newX = getTargetXY().getX()+getTargetType().getWidth()+5f;
                float newY = getTargetXY().getY();
                switch (getTargetType()) {
                    case SUPPORT:
                    case MS:
                        newY += (getCard().getPlayer().getBattleID() == 1) ? getTargetType().getHeight()*0.25f : -getCardPic().getHeight()*0.8f;
                        break;
                    case FLEET:
                        newY += getTargetType().getHeight()/2-getCardPic().getHeight() * ((getCard().getPlayer().getBattleID() == 1) ? 0.5f : 0.6f);
                        break;
                    case YARD:
                        newX += 5f;
                        newY = (getCard().getPlayer().getBattleID() == 1) ? Farstar.STAGE_HEIGHT*0.15f : Farstar.STAGE_HEIGHT*0.4f;
                        break;
                }
                setPosition(newX, newY);
            }
        }

        @Override
        public void draw(Batch batch) {
            if (getCard() != null && getTargetType() != null && getTargetXY() != null) {
                super.draw(batch);
            }
        }
    };


    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen, DuelMenu duelMenu) {
        super(game, viewport);
        this.battleScreen = battleScreen;
        this.duelMenu = duelMenu;
        combatEndButton.setPosition(Farstar.STAGE_WIDTH*0.82f, Farstar.STAGE_HEIGHT*0.3f);
        combatEndButton.setDisabled(true);
        abilityPicker = new AbilityPicker(Farstar.STAGE_WIDTH*1/12, Farstar.STAGE_HEIGHT*1/3, this, null, Farstar.ASSET_LIBRARY.get("images/yard.png"));
        battleScreen.getBattle().getRoundManager().setAbilityPicker(abilityPicker);
        roundCounter = new RoundCounter(Farstar.STAGE_WIDTH*0.003f, Farstar.STAGE_HEIGHT*0.475f, this, getBattleScreen().getBattle());
    }

    public void updateDeckInfos() { }

    public void enableCombatEnd() {
        if (battleScreen.getBattle().getWhoseTurn() instanceof LocalPlayer) {
            combatEndButton.setDisabled(false);
            addActor(combatEndButton);
        }
    }

    public void disableCombatEnd() {
        combatEndButton.setDisabled(true);
        combatEndButton.remove();
    }

    public void drawBattleStage(float delta, Batch batch) {
        roundCounter.draw(batch);
        abilityPicker.draw(batch);
        if (fakeToken != null) { fakeToken.draw(batch); }
        cardZoom.draw(batch);
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
            if (targetHit != null || token.getCard().getCardInfo().getCardType() == CardType.ACTION) {
                if (!token.getCard().isTactic() && combatManager.isActive() && !combatManager.getDuelManager().isActive()) {
                    combatManager.processDrop(token, targetHit, getCombatDropToken(x, y, targetHit));
                } else {
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

    public Token getCombatDropToken(float x, float y, DropTarget dropTarget) {
        if (dropTarget instanceof MothershipToken) {
            return ((MothershipToken) dropTarget);
        } else if (dropTarget instanceof FleetMenu) {
            FleetMenu fleetMenu = (FleetMenu) dropTarget;
            Token[] ships = fleetMenu.getFleetTokens();
            for (int i = 0; i < ships.length; i++) {
                if (x > fleetMenu.getX() + (fleetMenu.getOffset() * i) && x < fleetMenu.getX() + (fleetMenu.getOffset() * (i + 1))) {
                    return ships[i];
                }
            }
        }
        return null;
    }

    public int getCombatDropPosition(float x, float y, DropTarget dropTarget) {
        if (dropTarget instanceof MothershipToken) {
            return 7;
        } else if (dropTarget instanceof FleetMenu) {
            FleetMenu fleetMenu = (FleetMenu) dropTarget;
            Token[] ships = fleetMenu.getFleetTokens();
            for (int i = 0; i < ships.length; i++) {
                if (x > fleetMenu.getX() + (fleetMenu.getOffset() * i) && x < fleetMenu.getX() + (fleetMenu.getOffset() * (i + 1))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getRoundDropPosition(float x, float y, DropTarget dropTarget, CardType cardType) {
        boolean shipUpgrade = !CardType.isShip(cardType);
        if (dropTarget instanceof FleetMenu) {
            FleetMenu fleetMenu = (FleetMenu) dropTarget;
            Token[] ships = fleetMenu.getFleetTokens();
            if (!shipUpgrade && ships[3] == null) { //middle token empty
                return 3;
            } else {
                int count = 0;
                int left = 0;
                int right = 0;
                for (int i = 0; i < ships.length; i++) {
                    if (ships[i] != null) {
                        count++;
                    }
                    if (i < 3) {
                        left++;
                    } else if (i > 3) {
                        right++;
                    }
                }
                float shift = 0;
                if (count % 2 != 0) {
                    shift = ((left > right) ? fleetMenu.getOffset()/2 : -fleetMenu.getOffset()/2);
                }
                for (int i = 0; i < 8; i++) {
                    if (x > fleetMenu.getX() + shift + fleetMenu.getOffset() * i && (x < fleetMenu.getX() + shift + fleetMenu.getOffset() * (i+1))) {
                        SimpleVector2 lr = fleetMenu.getFleet().getSideSizes(fleetMenu.getFleet().getShips());
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
                        if (i > 6) { i = 6; }
                        return i;
                    }
                }
                return -1;
            }
        } else if (dropTarget instanceof SupportMenu) {
            SupportMenu supportMenu = (SupportMenu) dropTarget;
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
        } else if (dropTarget instanceof JunkButton) {
            return 8;
        }
        return -1;
    }

    public boolean isInBox(SimpleBox2 simpleBox2, float x, float y) {
        Rectangle rectangle = new Rectangle((int) simpleBox2.getX(), (int) simpleBox2.getY(), (int) simpleBox2.getWidth(), (int) simpleBox2.getHeight());
        return rectangle.contains(x, y);
    }

    public BattleScreen getBattleScreen() { return battleScreen; }

    public FakeToken getFakeToken() { return fakeToken; }

    public void setFakeToken(FakeToken fakeToken) {
        if (this.fakeToken != null) { this.fakeToken.remove(); }
        this.fakeToken = fakeToken;
        if (fakeToken != null) { this.addActor(fakeToken); }
    }

    public DuelMenu getDuelMenu() { return duelMenu; }

    public RoundCounter getRoundCounter() {
        return roundCounter;
    }

    public PrintToken getCardZoom() {
        return cardZoom;
    }

    @Override
    public void dispose() {
        turnButton.removeListener(turnButton.getClickListener());
        combatEndButton.remove();
        abilityPicker.dispose();
        super.dispose();
    }

}
