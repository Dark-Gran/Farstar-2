package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.FleetMenu;

public class Fleet { //not a CardList to keep the positioning
    private Card[] cards = new Card[7];
    private FleetMenu fleetMenu;

    public boolean addCard(Card card, int position) {
        if (cards[position] == null) {
            cards[position] = card;
            return true;
        } else {
            return false;
        }
    }

    private int getFreePosition() {
        /*if (ships[3] == null) {
            return 3;
        } else if (ships[2] == null) {
            return 2;
        } else if (ships[4] == null) {
            return 4;
        } else if (ships[1] == null) {
            return 1;
        } else if (ships[5] == null) {
            return 5;
        } else if (ships[0] == null) {
            return 0;
        } else if (ships[6] == null) {
            return 6;
        } else {*/
            return 7;
        //}
    }

    public void receiveFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    public Card[] getCards() { return cards; }
}
