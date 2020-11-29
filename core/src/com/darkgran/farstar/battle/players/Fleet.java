package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.FleetMenu;

public class Fleet { //not a CardList to keep the positioning
    private Card[] cards = new Card[7];
    private FleetMenu fleetMenu;

    public boolean addCard(Card card, int position) {
        boolean success = false;
        if (position == 3) {
            if (cards[3] == null) {
                setCard(card, position);
                success = true;
            }
        } else { //TODO: shifting the whole fleet to one side when the targeted side is full
            Card cardToSet = card;
            if (position < 3) {
                for (int i = 2; i >= 0; i--) {
                    if (cards[i] != null) {
                        if (i == position) {
                            cardToSet = cards[i];
                            setCard(card, i);
                            success = true;
                            position--;
                        }
                    } else {
                        setCard(cardToSet, i);
                        success = true;
                        break;
                    }
                }
            } else {
                for (int i = 4; i <= 6; i++) {
                    if (cards[i] != null) {
                        if (i == position) {
                            cardToSet = cards[i];
                            setCard(card, i);
                            success = true;
                            position++;
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
