package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.Hand;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

public class HandMenu extends SimpleVector2 {
    private ArrayList<HandCard> cards = new ArrayList<>();
    private final Hand hand; //getCards() ArrayList<Card>
    private final float offsetX;

    public HandMenu(Hand hand, float x, float y) {
        this.hand = hand;
        setX(x);
        setY(y);
        String res = "Battlestation";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        offsetX = layout.width*3/2;
        generateCards();
        hand.receiveHandMenu(this);
    }

    private void generateCards() {
        cards.clear();
        for (int i = 0; i < hand.getCards().size(); i++) {
            cards.add(new HandCard(hand.getCards().get(i), getX() + offsetX*i, getY()));
        }
    }

    public void generateNewCard(Card card) {
        cards.add(new HandCard(card, getX() + offsetX*cards.size()-1, getY()));
    }

    public ArrayList<HandCard> getCards() { return cards; }

    public void setCards(ArrayList<HandCard> cards) { this.cards = cards; }

    public Hand getHand() { return hand; }

}
