package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends CardList {

    public Deck(ArrayList<Card> cards) {
        super(cards);
        shuffle();
    }

    public Deck() {
        setupSize();
        setCards(new ArrayList<>());
        int[] ids = new int[]{
                6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
                6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
                18 ,18
        };
        for (int i = 0; i < getMaxSize(); i++) {
            getCards().add(new Card(Battle.CARD_LIBRARY.getCard(ids[i]), null));
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
        if (getCards().size() == 0) {
            eatJunk();
            shuffle();
        }
        if (getCards().size() > 0) {
            Card card = getCards().get(0);
            getCards().remove(0);
            return card;
        } else { return null; }
    }

    public void eatJunk() {
        ArrayList<Card> junkCards = getPlayer().getJunkpile().getCards();
        for (Card junk : junkCards) { getCards().add(junk); }
        getPlayer().getJunkpile().setCards(new ArrayList<Card>());
    }

    private void shuffle() {
        ArrayList<Card> list = getCards();
        Collections.shuffle(list);
        setCards(list);
    }

}
