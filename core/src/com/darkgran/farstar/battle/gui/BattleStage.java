package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.ButtonWithExtraState;
import com.darkgran.farstar.gui.ListeningStage;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.gui.tokens.AnchoredToken;
import com.darkgran.farstar.battle.gui.tokens.FakeToken;
import com.darkgran.farstar.battle.gui.tokens.MothershipToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.util.SimpleBox2;

import java.awt.*;
import java.util.ArrayList;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private FakeToken fakeToken;
    private final DuelMenu duelMenu;
    private final Texture combatEndPic = Farstar.ASSET_LIBRARY.getAssetManager().get("images/combat_end.png");
    private final ImageButton combatEndButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(combatEndPic)));
    private ArrayList<DropTarget> dropTargets = new ArrayList<>();
    private final AbilityPicker abilityPicker;
    private final RoundCounter roundCounter;
    public final ButtonWithExtraState turnButton = new ButtonWithExtraState(Farstar.ASSET_LIBRARY.getAssetManager().get("images/turn.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/turnO.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/turnP.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/turnOP.png")){
        @Override
        public void clicked() {
            if (battleScreen.getBattle().getWhoseTurn() instanceof LocalPlayer) {
                battleScreen.getBattle().getRoundManager().endTurn();
            }
        }
    };

    public final static int TOKEN_WIDTH = 78; //future: (re)move

    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen, DuelMenu duelMenu) {
        super(game, viewport);
        this.battleScreen = battleScreen;
        this.duelMenu = duelMenu;
        combatEndButton.setBounds(Farstar.STAGE_WIDTH*3/4, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        abilityPicker = new AbilityPicker(Farstar.STAGE_WIDTH*1/12, Farstar.STAGE_HEIGHT*1/3, this, null, Farstar.ASSET_LIBRARY.getAssetManager().get("images/yard.png"));
        battleScreen.getBattle().getRoundManager().setAbilityPicker(abilityPicker);
        roundCounter = new RoundCounter(Farstar.STAGE_WIDTH*0.003f, Farstar.STAGE_HEIGHT*0.475f, this, getBattleScreen().getBattle());
    }

    public void updateDeckInfos() { }

    public void enableCombatEnd() {
        if (battleScreen.getBattle().getWhoseTurn() instanceof LocalPlayer) {
            addActor(combatEndButton);
            combatEndButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    battleScreen.getBattle().getCombatManager().endCombat();

                }
            });
        }
    }

    public void disableCombatEnd() {
        combatEndButton.remove();
        combatEndButton.removeListener(combatEndButton.getClickListener());
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
        CombatManager combatManager = getBattleScreen().getBattle().getCombatManager();
        DropTarget targetHit = returnDropTarget(x, y);
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

    public void drawBattleStage(float delta, Batch batch) {
        if (fakeToken != null) { fakeToken.draw(batch); }
        abilityPicker.draw(batch);
        roundCounter.draw(batch);
    }

    public void drawTokenMenu(CardListMenu cardListMenu, Batch batch) {
        for (int i = 0; i < cardListMenu.getTokens().size(); i++) {
            cardListMenu.getTokens().get(i).draw(batch);
        }
    }

    public void drawFleet(FleetMenu fleetMenu, Batch batch) {
        for (int i = 0; i < fleetMenu.getShips().length; i++) {
            if (fleetMenu.getShips()[i] != null) {
                fleetMenu.getShips()[i].draw(batch);
            }
        }
    }

    public boolean isInBox(SimpleBox2 simpleBox2, float x, float y) {
        Rectangle rectangle = new Rectangle((int) simpleBox2.getX(), (int) simpleBox2.getY(), (int) simpleBox2.getWidth(), (int) simpleBox2.getHeight());
        return rectangle.contains(x, y);
    }

    public Token getCombatDropToken(float x, float y, DropTarget dropTarget) {
        if (dropTarget instanceof MothershipToken) {
            return ((MothershipToken) dropTarget);
        } else if (dropTarget instanceof FleetMenu) {
            FleetMenu fleetMenu = (FleetMenu) dropTarget;
            Token[] ships = fleetMenu.getShips();
            for (int i = 0; i < ships.length; i++) {
                if (x > fleetMenu.getX() + (TOKEN_WIDTH * i) && x < fleetMenu.getX() + (TOKEN_WIDTH * (i + 1))) {
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
            Token[] ships = fleetMenu.getShips();
            for (int i = 0; i < ships.length; i++) {
                if (x > fleetMenu.getX() + (TOKEN_WIDTH * i) && x < fleetMenu.getX() + (TOKEN_WIDTH * (i + 1))) {
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
            Token[] ships = fleetMenu.getShips();
            if (!shipUpgrade && ships[3] == null) { //middle token empty
                return 3;
            } else {
                for (int i = 0; i < 7; i++) {
                    if (x > fleetMenu.getX() + (TOKEN_WIDTH * i) && x < fleetMenu.getX() + (TOKEN_WIDTH * (i + 1))) {
                        if (i != 3 || shipUpgrade) {
                            return i;
                        } else { //hit middle token (not empty) - pick left/right
                            if (x < fleetMenu.getX() + fleetMenu.getWidth() / 2) {
                                return 2;
                            } else {
                                return 4;
                            }
                        }
                    }
                }
                return -1;
            }
        } else if (dropTarget instanceof SupportMenu) {
            SupportMenu supportMenu = (SupportMenu) dropTarget;
            for (int i = 0; i < 7; i++) {
                if (x > supportMenu.getX() + (TOKEN_WIDTH * i) && x < supportMenu.getX() + (TOKEN_WIDTH * (i + 1))) {
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

    public BattleScreen getBattleScreen() { return battleScreen; }

    public FakeToken getFakeToken() { return fakeToken; }

    public void setFakeToken(FakeToken fakeToken) {
        if (this.fakeToken != null) { this.fakeToken.remove(); }
        this.fakeToken = fakeToken;
        if (fakeToken != null) { this.addActor(fakeToken); }
    }

    public DuelMenu getDuelMenu() { return duelMenu; }

    @Override
    public void dispose() {
        turnButton.removeListener(turnButton.getClickListener());
        disableCombatEnd();
        abilityPicker.dispose();
        super.dispose();
    }

}
