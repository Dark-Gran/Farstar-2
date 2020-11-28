package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.util.TextFont;

public class Token extends TextFont {
    private Card card;
    private final CardName cardName = new CardName();
    private final CardOffense cardOffense = new CardOffense();
    private final CardDefense cardDefense = new CardDefense();
    private final BattleStage battleStage;
    private final TokenMenu tokenMenu;

    public Token(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu){
        setCard(card);
        String res = "Battlestation";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        setWidth(layout.width);
        setHeight(layout.height);
        setX(x);
        setY(y);
        this.tokenMenu = tokenMenu;
        this.battleStage = battleStage;
        battleStage.addActor(this);
        setupListener();
    }

    public void setupListener() { }

    public void draw(Batch batch) {
        cardName.draw(getFont(), batch, getX()-getWidth()/2, getY()+getHeight()*3, card.getCardInfo().getName());
        cardOffense.draw(getFont(), batch, getX()-getWidth()/10-getWidth()/2, getY(), String.valueOf(card.getCardInfo().getOffense()));
        cardDefense.draw(getFont(), batch, getX()-getWidth()/10+getWidth()/2, getY(), String.valueOf(card.getCardInfo().getDefense()));
    }

    public Card getCard() { return card; }

    public void setCard(Card card) { this.card = card; }

    public BattleStage getBattleStage() { return battleStage; }

    public TokenMenu getTokenMenu() { return tokenMenu; }

}
