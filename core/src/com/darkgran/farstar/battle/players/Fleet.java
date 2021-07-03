package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.gui.FleetMenu;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.cards.Ship;

public class Fleet implements BattleTicks {
    private final Junkpile junkpile;
    private Ship[] ships = new Ship[7];
    private FleetMenu fleetMenu;

    public Fleet(Junkpile junkpile) { this.junkpile = junkpile; }

    public boolean addShip(Token token, int position) {
        boolean success = false;
        Ship ship = new Ship(this, token.getCard().getCardInfo(), token.getCard().getPlayer());
        ship.setFought(true);
        ship.setUsed(true);
        if (hasSpace() && position > -1 && position < 7) {
            if (position == 3) {
                if (ships[3] == null) {
                    setShip(ship, position);
                    success = true;
                }
            } else {
                boolean side = position < 3;
                int start = side ? 2 : 4;
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
                            setShip(shipToSet, i);
                            shipToSet = holder;
                            success = true;
                            position += change;
                        }
                    } else {
                        setShip(shipToSet, i);
                        success = true;
                        break;
                    }
                }
                centralizeShips();
            }
        }
        return success;
    }

    private void centralizeShips() {
        int left = 0;
        int right = 0;
        for (int i = 0; i < getShips().length; i++) {
            if (getShips()[i] != null) {
                if (i < 3) {
                    left++;
                } else if (i > 3) {
                    right++;
                }
            }
        }
        if (left != right) {
            shiftAllShips(left > right, false);
        }
    }

    public void shiftAllShips(boolean fromSide, boolean noUpdate) {
        int start = fromSide ? 6 : 0;
        int end = fromSide ? 0 : 6;
        int change = fromSide ? -1 : 1;
        for (int i = start; i != end; i+=change) {
            if (ships[i+change] != null) {
                setShip(ships[i+change], i);
                removeShip(i+change, true);
            }
        }
        if (!noUpdate) {
            getFleetMenu().updateCoordinates();
        }
    }

    public void shiftShipsToBlank(int blankPosition) {
        if (blankPosition > 0 && blankPosition < 6) {
            boolean direction = blankPosition < 3; //in-future fix shifting to 3
            int end = direction ? 0 : 6;
            int change = direction ? -1 : 1;
            for (int i = blankPosition; i != end; i += change) {
                if (ships[i + change] != null) {
                    setShip(ships[i + change], i);
                    removeShip(i + change, true);
                }
            }
            getFleetMenu().updateCoordinates();
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
        for (Ship ship : ships) { if (ship != null && !ship.haveFought()) { return false; } }
        return true;
    }

    public boolean isEmpty() {
        for (Ship ship : ships) { if (ship != null) { return false; } }
        return true;
    }

    public boolean hasSpace() {
        for (Ship ship : ships) { if (ship == null) { return true; } }
        return false;
    }

    public void removeShip(Ship ship) {
        for (int i = 0; i < ships.length; i++) {
            if (ships[i] == ship) {
                removeShip(i, false);
                shiftShipsToBlank(i);
                centralizeShips();
            }
        }
    }

    public void removeShip(int position, boolean noUpdate) {
        ships[position] = null;
        getFleetMenu().removeShip(position, noUpdate);
    }

    private void setShip(Ship ship, int position) {
        ships[position] = ship;
        ship.setToken(getFleetMenu().addShip(ship, position));
    }

    public void receiveFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public Ship[] getShips() { return ships; }

    public Junkpile getJunkpile() { return junkpile; }
}
