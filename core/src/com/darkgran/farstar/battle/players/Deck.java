package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Deck extends CardList {

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE);
    }

    public Card drawCard() {
        Card card = getCards().get(0);
        getCards().remove(0);
        return card;
    }

}
