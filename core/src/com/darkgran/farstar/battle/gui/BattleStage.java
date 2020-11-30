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
import com.darkgran.farstar.util.SimpleBox2;

import java.awt.*;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private final Texture turn = new Texture("images/turn.png");
    public final ImageButton turnButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(turn)));
    private final Texture yardPic = new Texture("images/yard.png");
    private FakeToken fakeToken;
    private final DuelMenu duelMenu;

    public final int tokenWidth = 78; //future: (re)move

    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen, DuelMenu duelMenu) {
        super(game, viewport);
        this.battleScreen = battleScreen;
        this.duelMenu = duelMenu;
    }

    public void processDrop(float x, float y, Token token) {
        CombatManager combatManager = getBattleScreen().getBattle().getCombatManager();
        DropTarget targetHit = returnDropTarget(x, y);
        if (targetHit != null) {
            if (targetHit instanceof FleetMenu) {
                FleetMenu fleetMenu = (FleetMenu) targetHit;
                if (combatManager.isActive()) {
                    combatManager.combatTarget(token, fleetMenu.getFleet(), getDropTarget(x, y, fleetMenu));
                } else {
                    getBattleScreen().getBattle().getRoundManager().fleetDeployment(token, fleetMenu.getFleet(), getDropPosition(x, y, fleetMenu));
                }
            }
        } else  if (token instanceof AnchoredToken) {
            ((AnchoredToken) token).resetPosition();
        }
        if (token instanceof FakeToken) {
            token.destroy();
        }
    }

    public DropTarget returnDropTarget(float x, float y) { return null; }

    public void drawBattleStage(float delta, Batch batch) {
        if (fakeToken != null) { fakeToken.draw(batch); }
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

    @Override
    public void dispose() {
        turnButton.removeListener(turnButton.getClickListener());
        turn.dispose();
        super.dispose();
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
    }

    public boolean isInBox(SimpleBox2 simpleBox2, float x, float y) {
        Rectangle rectangle = new Rectangle((int) simpleBox2.getX(), (int) simpleBox2.getY(), (int) simpleBox2.getWidth(), (int) simpleBox2.getHeight());
        return rectangle.contains(x, y);
    }

    public int getDropTarget(float x, float y, FleetMenu fleetMenu) {
        Token[] ships = fleetMenu.getShips();
        for (int i = 0; i < ships.length; i++) {
            if (x > fleetMenu.getX()+(tokenWidth*i) && x < fleetMenu.getX()+(tokenWidth*(i+1))) {
                return i;
            }
        }
        return 7;
    }

    public int getDropPosition(float x, float y, FleetMenu fleetMenu) {
        Token[] ships = fleetMenu.getShips();
        if (ships[3] == null) { //middle token empty
            return 3;
        } else {
            for (int i = 0; i < ships.length; i++) {
                if (x > fleetMenu.getX()+(tokenWidth*i) && x < fleetMenu.getX()+(tokenWidth*(i+1))) {
                    if (i != 3) {
                        return i;
                    } else { //hit middle token (not empty) - pick left/right
                        if (x < fleetMenu.getX()+fleetMenu.getWidth()/2) {
                            return 2;
                        } else {
                            return 4;
                        }
                    }
                }
            }
            return 7;
        }
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

}
