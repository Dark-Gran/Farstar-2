package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.BaseMenu;
import com.darkgran.farstar.battle.gui.Menu;
import com.darkgran.farstar.battle.players.cards.Card;

public class PossibilityInfo {
    private final Card card;
    private final Menu menu;

    public PossibilityInfo(Card card, Menu menu) {
        this.card = card;
        this.menu = menu;
    }

    public Card getCard() {
        return card;
    }

    public Menu getMenu() {
        return menu;
    }

}
