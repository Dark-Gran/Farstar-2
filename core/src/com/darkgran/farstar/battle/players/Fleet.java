package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.FleetMenu;

public class Fleet { //not a CardList to keep the positioning
    private Card[] cards = new Card[7];
    private FleetMenu fleetMenu;

    public boolean addCard(Card card, int position) {
        boolean success = false;
        if (hasSpace() && position > -1 && position < 7) {
            if (position == 3) {
                if (cards[3] == null) {
                    setCard(card, position);
                    success = true;
                }
            } else {
                boolean side = position < 3;
                int start = side ? 2 : 4;
                int end = side ? -1 : 7;
                int change = side ? -1 : 1;
                Card cardToSet = card;
                int i;
                boolean sideHasSpace = false;
                for (i = start; i != end; i += change) {
                    if (cards[i] == null) {
                        sideHasSpace = true;
                        break;
                    }
                }
                if (!sideHasSpace) {
                    shiftAllCards(side);
                }
                for (i = start; i != end; i += change) {
                    if (cards[i] != null) {
                        if (i == position) {
                            Card holder = cards[i];
                            setCard(cardToSet, i);
                            cardToSet = holder;
                            success = true;
                            position += change;
                        }
                    } else {
                        setCard(cardToSet, i);
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
            if (cards[i+change] != null) {
                setCard(cards[i+change], i);
                removeCard(i+change);
            }
        }
    }

    public boolean noAttackers() {
        for (Card card : cards) { if (card != null) { return false; } }
        return true;
    }

    public boolean hasSpace() {
        for (Card card : cards) { if (card == null) { return true; } }
        return false;
    }

    private void removeCard(int position) {
        cards[position] = null;
        getFleetMenu().removeShip(position);
    }

    private void setCard(Card card, int position) {
        cards[position] = card;
        getFleetMenu().addShip(card, position);
    }

    public void receiveFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public Card[] getCards() { return cards; }
}
