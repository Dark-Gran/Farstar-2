package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Deck extends CardList {
    private Player player;

    public Deck(int id) {
        super(id);
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE);
    }

    public Card drawCard() {
        if (getCards().size() == 0) { eatJunk(); }
        if (getCards().size() > 0) {
            Card card = getCards().get(0);
            getCards().remove(0);
            return card;
        } else { return null; }
    }

    public void eatJunk() { //in future: add shuffle
        ArrayList<Card> junkCards = player.getJunkpile().getCards();
        for (Card junk : junkCards) { getCards().add(junk); }
        player.getJunkpile().setCards(new ArrayList<Card>());
    }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}
