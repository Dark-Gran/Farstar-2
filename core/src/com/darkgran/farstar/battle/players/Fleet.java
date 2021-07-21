package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.gui.battlegui.FleetMenu;
import com.darkgran.farstar.gui.tokens.FleetToken;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.util.SimpleVector2;

public class Fleet implements BattleTicks {
    private final Junkpile junkpile;
    private Ship[] ships = new Ship[7];
    private FleetMenu fleetMenu;

    public Fleet(Junkpile junkpile) { this.junkpile = junkpile; }

    public boolean addShip(Token token, int position) { //in-future: split to be used on any Ship[] field (so it can be used by FleetMenu in predictCoordinates())
        boolean success = false;
        if (hasSpace() && position > -1 && position < 7) {
            Ship ship = new Ship(this, token.getCard().getCardInfo(), token.getCard().getBattlePlayer());
            ship.setUsed(true);
            if (position == 3 && ships[3] == null) {
                setShip(ship, position, null);
                success = true;
            } else {
                boolean side = position < 3;
                int start = side ? 2 : 4;
                if (position == 3) {
                    SimpleVector2 lr = getSideSizes(ships);
                    side = lr.x < lr.y;
                    start = 3;
                }
                int end = side ? -1 : 7;
                int change = side ? -1 : 1;
                Ship shipToSet = ship;
                int i;
                boolean sideHasSpace = false;
                for (i = start; i != end; i += change) {
                    if (ships[i] == null) {
                        sideHasSpace = true;
                        break;
                    }
                }
                if (!sideHasSpace) {
                    shiftAllShips(side, false);
                }
                for (i = start; i != end; i += change) {
                    if (ships[i] != null) {
                        if (i == position) {
                            Ship holder = ships[i];
                            setShip(shipToSet, i, (FleetToken) shipToSet.getToken());
                            shipToSet = holder;
                            success = true;
                            position += change;
                        }
                    } else {
                        setShip(shipToSet, i, shipToSet != null ? (FleetToken) shipToSet.getToken() : null);
                        success = true;
                        break;
                    }
                }
                centralizeShips();
            }
        }
        return success;
    }

    public SimpleVector2 getSideSizes(Ship[] arr) {
        int left = 0;
        int right = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (i < 3) {
                    left++;
                } else if (i > 3) {
                    right++;
                }
            }
        }
        return new SimpleVector2(left, right);
    }

    public void centralizeShips() {
        SimpleVector2 lr = getSideSizes(getShips());
        if (Math.abs(lr.x-lr.y) > 1) {
            shiftAllShips(lr.x > lr.y, false);
        }
    }

    public void checkForBlanks() {
        int bp = ships[3] == null && !isEmpty() ? 3 : hasBlank();
        while (bp != -1) {
            shiftShipsToBlank(bp);
            bp = hasBlank();
        }
    }

    public int hasBlank() {
        if (!isEmpty()) {
            byte step = 0;
            int blankPosition = -1;
            for (int i = 0; i < ships.length; i++) {
                switch (step) {
                    case 0: //nothing found yet
                        if (ships[i] != null) {
                            step = 1;
                        }
                        break;
                    case 1: //found some ships
                        if (ships[i] == null) {
                            step = 2;
                            blankPosition = i;
                        }
                        break;
                    case 2: //found ships and space
                        if (ships[i] != null) {
                            return blankPosition;
                        }
                        break;
                }
            }
        }
        return -1;
    }

    public void shiftAllShips(boolean fromSide, boolean noUpdate) {
        int start = fromSide ? 6 : 0;
        int end = fromSide ? 0 : 6;
        int change = fromSide ? -1 : 1;
        for (int i = start; i != end; i+=change) {
            if (ships[i+change] != null) {
                setShip(ships[i+change], i, (FleetToken) ships[i+change].getToken());
                removeShip(i+change, true);
            }
        }
        if (!noUpdate) {
            getFleetMenu().updateCoordinates(getFleetMenu().getFleetTokens());
        }
    }

    public void shiftShipsToBlank(int blankPosition) {
        if (blankPosition > 0 && blankPosition < 6) {
            boolean direction = blankPosition < 3;
            if (blankPosition == 3) {
                SimpleVector2 lr = getSideSizes(ships);
                if (lr.y < lr.x) {
                    direction = true;
                }
            }
            int end = direction ? 0 : 6;
            int change = direction ? -1 : 1;
            for (int i = blankPosition; i != end; i += change) {
                if (ships[i + change] != null) {
                    setShip(ships[i + change], i, (FleetToken) ships[i+change].getToken());
                    removeShip(i + change, true);
                }
            }
            getFleetMenu().updateCoordinates(fleetMenu.getFleetTokens());
        }
    }

    @Override
    public CardList getCardList() { return null; }

    @Override
    public void setUsedOnAll(boolean used) {
        for (Ship ship : ships) { if (ship != null) { ship.setUsed(used); } }
    }

    @Override
    public void tickEffectsOnAll(AbilityManager abilityManager) {
        for (Ship ship : ships) { if (ship != null) { ship.tickEffects(abilityManager); } }
    }

    @Override
    public void checkEffectsOnAll(AbilityManager abilityManager) {
        for (Ship ship : ships) { if (ship != null) { ship.checkEffects(abilityManager); } }
    }

    public boolean noAttackers() {
        for (Ship ship : ships) { if (ship != null && !ship.isUsed()) { return false; } }
        return true;
    }

    public boolean isEmpty() {
        for (Ship ship : ships) { if (ship != null) { return false; } }
        return true;
    }

    public int countShips() {
        int count = 0;
        for (Ship ship : ships) { if (ship != null) { count++; } }
        return count;
    }

    public boolean hasSpace() {
        for (Ship ship : ships) { if (ship == null) { return true; } }
        return false;
    }

    public void removeShip(Ship ship, boolean inAftermath) {
        for (int i = 0; i < ships.length; i++) {
            if (ships[i] == ship) {
                removeShip(i, inAftermath);
                if (!inAftermath) {
                    shiftShipsToBlank(i);
                    centralizeShips();
                }
            }
        }
    }

    public void removeShip(int position, boolean noUpdate) {
        ships[position] = null;
        getFleetMenu().removeShip(position, noUpdate);
    }

    private void setShip(Ship ship, int position, FleetToken fleetToken) {
        ships[position] = ship;
        if (fleetToken == null) {
            ship.setToken(getFleetMenu().addShip(ship, position));
        } else {
            ship.setToken(fleetToken);
            getFleetMenu().overwriteToken(position, fleetToken);
        }
    }

    public void receiveFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public Ship[] getShips() { return ships; }

    public Junkpile getJunkpile() { return junkpile; }
}
