package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.CardList;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

public abstract class TokenMenu extends SimpleVector2 {
    private ArrayList<ShipToken> tokens = new ArrayList<>();
    private final CardList cardList;
    private GlyphLayout layout = new GlyphLayout();
    private float offset;
    private boolean negativeOffset;
    private final Stage stage;

    public TokenMenu(CardList cardList, float x, float y, boolean negativeOffset, Stage stage) {
        setX(x);
        setY(y);
        this.cardList = cardList;
        cardList.receiveTokenMenu(this);
        this.negativeOffset = negativeOffset;
        setupOffset();
        this.stage = stage;
        generateTokens();
    }

    public void setupOffset() {
        String res = "Battlestation";
        layout.setText(new BitmapFont(), res);
        offset = layout.width;
        if (negativeOffset) { offset *= -1; }
    }

    public void generateTokens() {
        tokens.clear();
        for (int i = 0; i < cardList.getCards().size(); i++) {
            tokens.add(new ShipToken(cardList.getCards().get(i), getX() + offset*i, getY(), stage));
        }
    }

    public void generateNewToken(Card card) {
        tokens.add(new ShipToken(card, getX() + offset*tokens.size()-1, getY(), stage));
    }

    public ArrayList<ShipToken> getTokens() { return tokens; }

    public void setTokens(ArrayList<ShipToken> tokens) { this.tokens = tokens; }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public CardList getCardList() { return cardList; }

    public GlyphLayout getLayout() { return layout; }

    public boolean isNegativeOffset() { return negativeOffset; }

    public void setNegativeOffset(boolean negativeOffset) { this.negativeOffset = negativeOffset; }

    public Stage getStage() { return stage; }

}
