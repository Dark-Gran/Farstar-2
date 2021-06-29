package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
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

    @Override
    protected void setupOffset() {
        super.setupOffset();
        if (isNegativeOffset()) { setOffset(getOffset()*-1); } //switching back, FleetMenu handles offset differently
    }

    @Override
    public boolean isEmpty() {
        for (FleetToken ship : ships) { if (ship != null) { return false; } }
        return true;
    }

    public FleetToken addShip(Card card, int position) {
        float y = isNegativeOffset() ? getY() + getHeight() - TokenType.FLEET.getHeight() - 80f : getY() + 80f;
        ships[position] = new FleetToken(card, getX()+getOffset()*(position), y, getBattleStage(), null, this);
        return ships[position];
    }

    public void removeShip(int position) {
        ships[position] = null;
    }

    public Fleet getFleet() { return fleet; }

    public Token[] getShips() { return ships; }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }

}