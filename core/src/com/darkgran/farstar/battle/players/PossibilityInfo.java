package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.BaseMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class PossibilityInfo {
    private final Card card;
    private final BaseMenu menu;

    public PossibilityInfo(Card card, BaseMenu menu) {
        this.card = card;
        this.menu = menu;
    }

    public Card getCard() {
        return card;
    }

    public BaseMenu getMenu() {
        return menu;
    }

}
