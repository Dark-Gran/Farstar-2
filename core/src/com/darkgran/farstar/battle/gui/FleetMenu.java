package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleBox2;

//The only "Menu" that does NOT extend CardListMenu! (ie. Fleet is not a CardList!)
//(uses Array instead of ArrayList)
public class FleetMenu extends BaseMenu implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private final Fleet fleet;
    private FleetToken[] ships = new FleetToken[7];

    public FleetMenu(Fleet fleet, float x, float y, float width, float height, BattleStage battleStage, Player player, boolean negativeOffset) {
        super(x, y, negativeOffset, battleStage, player);
        setWidth(width);
        setHeight(height);
        setupSimpleBox2(x, y, width, height);
        this.fleet = fleet;
        fleet.receiveFleetMenu(this);
        setupOffset();
    }

    public FleetToken addShip(Card card, int position) {
        float y = isNegativeOffset() ? getY() + getHeight() - TokenType.FLEET.getHeight() - 80f : getY() + 80f;
        ships[position] = new FleetToken(card, getX()+getOffset()*(position), y, getBattleStage(), null, this);
        updateCoordinates();
        return ships[position];
    }

    public void updateCoordinates() { //provides a visual shift of tokens so the even numbers seem centralized
        int count = 0;
        int left = 0;
        int right = 0;
        for (int i = 0; i < getShips().length; i++) {
            if (getShips()[i] != null) {
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
                for (int i = 0; i < getShips().length; i++) {
                    if (getShips()[i] != null) {
                        getShips()[i].setX(getX() + ((left > right) ? getOffset()/2 : -getOffset()/2) + getOffset() * i);
                        getShips()[i].setNewAnchor(getShips()[i].getX(), getShips()[i].getY());
                    }
                }
            } else { //odd
                for (int i = 0; i < getShips().length; i++) {
                    if (getShips()[i] != null) {
                        getShips()[i].setX(getX() + getOffset() * i);
                        getShips()[i].setNewAnchor(getShips()[i].getX(), getShips()[i].getY());
                    }
                }
            }
        }
    }

    public void drawTokens(Batch batch) {
        for (int i = 0; i < getShips().length; i++) {
            if (getShips()[i] != null) {
                getShips()[i].draw(batch);
            }
        }
    }

    @Override
    protected void setupOffset() {
        setOffset(TokenType.FLEET.getWidth()*1.03f);
    }

    @Override
    public boolean isEmpty() {
        for (FleetToken ship : ships) { if (ship != null) { return false; } }
        return true;
    }

    public void removeShip(int position, boolean noUpdate) {
        ships[position] = null;
        if (!noUpdate) { updateCoordinates(); }
    }

    public Fleet getFleet() { return fleet; }

    public FleetToken[] getShips() { return ships; }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }

}