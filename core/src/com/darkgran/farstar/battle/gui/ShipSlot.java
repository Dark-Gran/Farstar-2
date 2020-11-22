package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.players.Card;

public class ShipSlot extends TextFont {
    private Card card;

    public ShipSlot(Card card, float x, float y){
        setCard(card);
        setX(x);
        setY(y);
    }

    public void draw(Batch batch) { //TODO
        String res = "Ship";
        getFont().draw(batch, res, getX(), getY());
    }

    public Card getCard() { return card; }

    public void setCard(Card card) { this.card = card; }

}
