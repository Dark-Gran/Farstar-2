package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.ColorPalette;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.TextFont;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class Token extends TextFont {
    private Dragger dragger;
    private final Card card;
    private final TokenName tokenName = new TokenName();
    private final TokenOffense tokenOffense = new TokenOffense();
    private final TokenDefense tokenDefense = new TokenDefense();
    private final BattleStage battleStage;
    private final CardListMenu cardListMenu;
    private boolean inDuel = false;

    public Token(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu){
        this.card = card;
        String res = "Battlestation";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        setWidth(layout.width);
        setHeight(layout.height*4);
        setX(x);
        setY(y);
        this.cardListMenu = cardListMenu;
        this.battleStage = battleStage;
        battleStage.addActor(this);
        setupListener();
    }

    public void setupListener() { }

    public void draw(Batch batch) {
        Color color = new Color();
        //Price
        color.set(1, 1, 1, 1);
        if (this instanceof YardToken || this instanceof HandToken || this instanceof FakeToken) {
            //TODO
        }
        //Offense+Defense
        color = ColorPalette.getTypeColor(card.getCardInfo().getOffenseType());
        tokenOffense.draw(getFont(), batch, getX(), getY()+getHeight()/3, String.valueOf(card.getCardInfo().getOffense()), color);
        color = ColorPalette.getTypeColor(card.getCardInfo().getDefenseType());
        tokenDefense.draw(getFont(), batch, getX()+getWidth()*5/6, getY()+getHeight()/3, String.valueOf(card.getHealth()), color);
        //Name
        if (card instanceof Ship) { if (((Ship) card).haveFought()) { color.set(1, 0, 0, 1); } }
        else if (isInDuel()) { color.set(0, 1, 0, 1); }
        tokenName.draw(getFont(), batch, getX(), getY()+getHeight(), card.getCardInfo().getName(), color);
        //Debug
        if (DEBUG_RENDER) { battleStage.getBattleScreen().drawDebugSimpleBox2(new SimpleBox2(getX(), getY(), getWidth(), getHeight()), battleStage.getBattleScreen().getDebugRenderer(), batch); }
    }

    public void destroy() {
        remove();
        if (this instanceof FakeToken) { battleStage.setFakeToken(null); }
        else {
            if (cardListMenu !=null) {
                cardListMenu.removeToken(this);
                cardListMenu.getCardList().getCards().remove(getCard());
            }
        }
    }

    public void addCardToJunk() {
        if (getCardListMenu() != null) {
            getCardListMenu().getPlayer().getJunkpile().addCard(this.getCard());
        } else if (this instanceof FleetToken) {
            FleetToken fleetToken = (FleetToken) this;
            if (fleetToken.getFleetMenu() != null) {
                fleetToken.getFleetMenu().getFleet().getJunkpile().addCard(this.getCard());
            }
        }
    }

    public Card getCard() { return card; }

    public BattleStage getBattleStage() { return battleStage; }

    public CardListMenu getCardListMenu() { return cardListMenu; }

    public Dragger getDragger() { return dragger; }

    public void setDragger(Dragger dragger) { this.dragger = dragger; }

    public boolean isInDuel() { return inDuel; }

    public void setInDuel(boolean inDuel) { this.inDuel = inDuel; }

    public Token getThis() { return this; }

}
