package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.FleetMenu;

public class Fleet {
    private Ship[] ships = new Ship[7];
    private FleetMenu fleetMenu;

    public boolean addShip(Card card, int position) {
        boolean success = false;
        Ship ship = new Ship(card.getCardInfo());
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
                    shiftAllCards(side);
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
            }
        }
        return success;
    }

    private void shiftAllCards(boolean fromSide) {
        int start = fromSide ? 6 : 0;
        int end = fromSide ? 0 : 6;
        int change = fromSide ? -1 : 1;
        for (int i = start; i != end; i+=change) {
            if (ships[i+change] != null) {
                setShip(ships[i+change], i);
                removeShip(i+change);
            }
        }
    }

    public boolean noAttackers() {
        for (Ship ship : ships) { if (ship != null && !ship.haveFought()) { return false; } }
        return true;
    }

    public boolean hasSpace() {
        for (Ship ship : ships) { if (ship == null) { return true; } }
        return false;
    }

    private void removeShip(int position) {
        ships[position] = null;
        getFleetMenu().removeShip(position);
    }

    private void setShip(Ship ship, int position) {
        ships[position] = ship;
        getFleetMenu().addShip(ship, position);
    }

    public void receiveFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public Ship[] getShips() { return ships; }
}
