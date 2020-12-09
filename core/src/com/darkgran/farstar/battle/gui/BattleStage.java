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
import com.darkgran.farstar.ListeningStage;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.players.CardType;
import com.darkgran.farstar.util.SimpleBox2;

import java.awt.*;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private final Texture turn = new Texture("images/turn.png");
    public final ImageButton turnButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(turn)));
    private final Texture yardPic = new Texture("images/yard.png");
    private FakeToken fakeToken;
    private final DuelMenu duelMenu;
    private final Texture combatEndPic = new Texture("images/combat_end.png");
    private final ImageButton combatEndButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(combatEndPic)));

    public final static int TOKEN_WIDTH = 78; //future: (re)move

    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen, DuelMenu duelMenu) {
        super(game, viewport);
        this.battleScreen = battleScreen;
        this.duelMenu = duelMenu;
        combatEndButton.setBounds(Farstar.STAGE_WIDTH*3/4, Farstar.STAGE_HEIGHT*1/5, (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
    }

    public void enableCombatEnd() {
        addActor(combatEndButton);
        combatEndButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                battleScreen.getBattle().getCombatManager().endCombat();
            }
        });
    }

    public void disableCombatEnd() {
        combatEndButton.remove();
        combatEndButton.removeListener(combatEndButton.getClickListener());
    }

    public DropTarget returnDropTarget(float x, float y) { return null; }

    public void processDrop(float x, float y, Token token) {
        CombatManager combatManager = getBattleScreen().getBattle().getCombatManager();
        DropTarget targetHit = returnDropTarget(x, y);
        if (targetHit != null) {
            if (combatManager.isActive()) {
                combatManager.processDrop(token, targetHit, getCombatDropToken(x, y, targetHit));
            } else {
                getBattleScreen().getBattle().getRoundManager().processDrop(token, targetHit, getRoundDropPosition(x, y, targetHit, token.getCard().getCardInfo().getCardType()));
            }
        } else  if (token instanceof AnchoredToken) {
            ((AnchoredToken) token).resetPosition();
        }
        if (token instanceof FakeToken) {
            token.destroy();
        }
    }

    @Override
    public void setupListeners() {
        turnButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
               battleScreen.getBattle().getRoundManager().endTurn();
            }
        });
    }

    public void drawBattleStage(float delta, Batch batch) {
        if (fakeToken != null) { fakeToken.draw(batch); }
    }

    public void drawTokenMenu(TokenMenu tokenMenu, Batch batch) {
        for (int i = 0; i < tokenMenu.getTokens().size(); i++) {
            tokenMenu.getTokens().get(i).draw(batch);
        }
    }

    public void drawFleet(FleetMenu fleetMenu, Batch batch) {
        for (int i = 0; i < fleetMenu.getShips().length; i++) {
            if (fleetMenu.getShips()[i] != null) {
                fleetMenu.getShips()[i].draw(batch);
            }
        }
        if (DEBUG_RENDER) { getBattleScreen().drawDebugSimpleBox2(fleetMenu.getSimpleBox2(), getBattleScreen().getDebugRenderer(), batch); }
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
        boolean shipUpgrade = (cardType != CardType.BLUEPRINT && cardType != CardType.YARD);
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
        } else if (dropTarget instanceof JunkButton) {
            return 8;
        }
        return -1;
    }

    public Texture getYardPic() { return yardPic; }

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
        turn.dispose();
        disableCombatEnd();
        combatEndPic.dispose();
        super.dispose();
    }

}
