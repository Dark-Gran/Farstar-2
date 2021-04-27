package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.TypePalette;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.gui.TextDrawer;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class Token extends Actor implements JustFont {
    private String fontPath = "";
    private Dragger dragger;
    private final Card card;
    private final TokenPrice tokenPrice = new TokenPrice();
    private final TokenName tokenName = new TokenName();
    private final TokenOffense tokenOffense = new TokenOffense();
    private final TokenDefense tokenDefense = new TokenDefense();
    private final BattleStage battleStage;
    private final CardListMenu cardListMenu;

    public Token(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu){
        setFont("");
        tokenPrice.setFont(getFontPath());
        tokenName.setFont(getFontPath());
        tokenOffense.setFont(getFontPath());
        tokenDefense.setFont(getFontPath());
        this.card = card;
        String res = "Battlestation";
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), res);
        setWidth(textWH.getX());
        setHeight(textWH.getY()*4);
        setX(x);
        setY(y);
        this.cardListMenu = cardListMenu;
        this.battleStage = battleStage;
        battleStage.addActor(this);
        setupListener();
    }

    protected void setupListener() { }

    public void draw(Batch batch) {
        Color color = new Color();
        //Price
        color.set(1, 1, 1, 1);
        if (this instanceof YardToken || this instanceof HandToken || this instanceof FakeToken) {
            tokenPrice.drawText(tokenPrice.getFont(), batch, getX(), getY()+getHeight()*4/3, card.getCardInfo().getEnergy()+":"+card.getCardInfo().getMatter(), color);
        }
        //Name
        if (card instanceof Ship) { if (((Ship) card).haveFought()) { color.set(1, 0, 0, 1); } }
        if (getCard().isPossible()) { color.set(0, 1, 0, 1); }
        else if (getCard().isInDuel()) { color.set(1, 1, 0, 1); }
        tokenName.drawText(tokenName.getFont(), batch, getX(), getY()+getHeight(), card.getCardInfo().getName(), color);
        //Offense+Defense
        color = TypePalette.getTypeColor(card.getCardInfo().getOffenseType());
        if (!getCard().isMS()) { tokenOffense.drawText(tokenOffense.getFont(), batch, getX(), getY()+getHeight()/3, String.valueOf(card.getCardInfo().getOffense()), color); }
        color = TypePalette.getTypeColor(card.getCardInfo().getDefenseType());
        tokenDefense.drawText(tokenDefense.getFont(), batch, getX()+getWidth()*5/6, getY()+getHeight()/3, String.valueOf(card.getHealth()), color);
        //Debug
        if (DEBUG_RENDER) { battleStage.getBattleScreen().drawDebugSimpleBox2(new SimpleBox2(getX(), getY(), getWidth(), getHeight()), battleStage.getBattleScreen().getShapeRenderer(), batch); }
    }

    public void destroy() {
        remove();
        if (this instanceof FakeToken) { battleStage.setFakeToken(null); }
        else {
            if (cardListMenu !=null) {
                cardListMenu.removeToken(this);
                cardListMenu.getCardList().remove(getCard());
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

    public Token getThis() { return this; }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }

}
