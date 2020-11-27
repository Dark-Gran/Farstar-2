package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.FleetMenu;

public class Fleet { //not a CardList to keep the positioning
    private final int maxSize = 7;
    private Card[] cards = new Card[maxSize];
    private FleetMenu fleetMenu;

    public boolean addCard(Card card, int position) {
        if (cards[position] == null) {
            cards[position] = card;
            return true;
        } else {
            return false;
        }
    }

    public void receiveFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    public int getMaxSize() { return maxSize; }

    public Card[] getCards() { return cards; }
}
