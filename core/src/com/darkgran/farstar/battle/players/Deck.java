package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardList;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends CardList {

    public Deck(ArrayList<Card> cards) {
        super(cards);
        shuffle();
    }

    public Deck() {
        setupSize();
        clear();
        int[] ids = new int[]{
                6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
                6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
                18 ,18
        };
        for (int i = 0; i < getMaxSize(); i++) {
            add(new Card(Farstar.CARD_LIBRARY.getCard(ids[i]), null));
        }
        shuffle();
    }

    public Deck(int id) {
        super(id);
        shuffle();
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE);
    }

    public Card drawCard() {
        if (size() == 0) {
            eatJunk();
            shuffle();
        }
        if (size() > 0) {
            Card card = get(0);
            remove(0);
            return card;
        } else { return null; }
    }

    public void eatJunk() {
        ArrayList<Card> junkCards = getPlayer().getJunkpile();
        for (Card junk : junkCards) { add(junk); }
        getPlayer().getJunkpile().clear();
    }

    private void shuffle() {
        ArrayList<Card> list = (ArrayList<Card>) this.clone();
        Collections.shuffle(list);
        clear();
        addAll(list);
    }

}
