package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.util.SimpleBox2;

//The only "Menu" that does NOT extend CardListMenu! (ie. Fleet is not a CardList!)
//(uses Array instead of ArrayList)
public class FleetMenu extends BaseMenu implements DropTarget {
    private final Fleet fleet;
    private FleetToken[] fleetTokens = new FleetToken[7];
    private FleetToken[] tokensPrediction = new FleetToken[7];
    private ClickListener clickListener = new ClickListener(){};
    private boolean predicting;

    public FleetMenu(Fleet fleet, float x, float y, float width, float height, BattleStage battleStage, Player player, boolean negativeOffset) {
        super(x, y, negativeOffset, battleStage, player);
        setWidth(width);
        setHeight(height);
        setupSimpleBox2(x, y, width, height);
        this.fleet = fleet;
        fleet.receiveFleetMenu(this);
        setupOffset();
        getBattleStage().addListener(clickListener);
    }

    public FleetToken addShip(Card card, int position) {
        float y = isNegativeOffset() ? getY() + getHeight() - TokenType.FLEET.getHeight() - 80f : getY() + 80f;
        fleetTokens[position] = new FleetToken(card, getX()+getOffset()*(position), y, getBattleStage(), null, this);
        updateCoordinates();
        return fleetTokens[position];
    }

    public void updateCoordinates() { //provides a visual shift of tokens so the even numbers seem centralized
        int count = 0;
        int left = 0;
        int right = 0;
        for (int i = 0; i < getFleetTokens().length; i++) {
            if (getFleetTokens()[i] != null) {
                count++;
                if (i < 3) {
                    left++;
                } else if (i > 3) {
                    right++;
                }
            }
        }
        if (count != 0) {
            if (count % 2 == 0) { //even
                for (int i = 0; i < getFleetTokens().length; i++) {
                    if (getFleetTokens()[i] != null) {
                        getFleetTokens()[i].setX(getX() + ((left > right) ? getOffset()/2 : -getOffset()/2) + getOffset() * i);
                        getFleetTokens()[i].setNewAnchor(getFleetTokens()[i].getX(), getFleetTokens()[i].getY());
                    }
                }
            } else { //odd
                for (int i = 0; i < getFleetTokens().length; i++) {
                    if (getFleetTokens()[i] != null) {
                        getFleetTokens()[i].setX(getX() + getOffset() * i);
                        getFleetTokens()[i].setNewAnchor(getFleetTokens()[i].getX(), getFleetTokens()[i].getY());
                    }
                }
            }
        }
    }

    private void predictCoordinates() {
        Vector3 mouseInWorld3D = new Vector3();
        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        getBattleStage().getBattleScreen().getCamera().unproject(mouseInWorld3D);
        int pos = getBattleStage().getRoundDropPosition(mouseInWorld3D.x, mouseInWorld3D.y, this, CardType.YARDPRINT);
        if (fleet.hasSpace() && pos > -1 && pos < 7) { //fleet.addShip() validation
            if (pos != 3) {
                //Copy ships
                Ship[] shipsPrediction = new Ship[7];
                for (int c = 0; c < fleet.getShips().length; c++) {
                    shipsPrediction[c] = fleet.getShips()[c];
                }
                //Proceed with fleet.addShip()
                boolean side = pos < 3;
                int start = side ? 2 : 4;
                int end = side ? -1 : 7;
                int change = side ? -1 : 1;
                Ship shipToSet = null;
                int i;
                boolean sideHasSpace = false;
                for (i = start; i != end; i += change) {
                    if (shipsPrediction[i] == null) {
                        sideHasSpace = true;
                        break;
                    }
                }
                if (!sideHasSpace) {
                    fleet.shiftAllShips(shipsPrediction, side, false);
                }
                for (i = start; i != end; i += change) {
                    if (shipsPrediction[i] != null) {
                        if (i == pos) {
                            Ship holder = shipsPrediction[i];
                            shipsPrediction[i] = shipToSet;
                            shipToSet = holder;
                            pos += change;
                        }
                    } else {
                        shipsPrediction[i] = shipToSet;
                        break;
                    }
                }
                for (int c = 0; c < shipsPrediction.length; c++) {
                    if (shipsPrediction[c] == null) {
                        tokensPrediction[c] = null;
                    } else {
                        tokensPrediction[c] = (FleetToken) shipsPrediction[c].getToken();
                    }
                }
            }
        }
    }

    public void drawTokens(Batch batch) { //todo
        if (clickListener.isOver() != predicting) {
            predicting = clickListener.isOver();
            if (clickListener.isOver()) {
                predictCoordinates();
            }
        }
        System.out.println(predicting);
        if (!predicting) {
            drawShips(getFleetTokens(), batch);
        } else {
            drawShips(tokensPrediction, batch);
        }
    }

    private void drawShips(FleetToken[] arr, Batch batch) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                arr[i].draw(batch);
            }
        }
    }

    @Override
    protected void setupOffset() {
        setOffset(TokenType.FLEET.getWidth()*1.03f);
    }

    @Override
    public boolean isEmpty() {
        for (FleetToken ship : fleetTokens) { if (ship != null) { return false; } }
        return true;
    }

    public void removeShip(int position, boolean noUpdate) {
        fleetTokens[position] = null;
        if (!noUpdate) { updateCoordinates(); }
    }

    public void overwriteToken(int position, FleetToken fleetToken) {
        fleetTokens[position] = fleetToken;
        updateCoordinates();
    }

    public Fleet getFleet() { return fleet; }

    public FleetToken[] getFleetTokens() { return fleetTokens; }

    public boolean isPredicting() { return predicting; }

    @Override
    public SimpleBox2 getSimpleBox2() { return this; }

}