package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.AssetLibrary;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.util.SimpleBox2;

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
    private final TokenType tokenType;
    private Texture portrait;
    private Texture frame;

    public Token(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType){
        setFont("");
        tokenPrice.setFont(getFontPath());
        tokenName.setFont(getFontPath());
        tokenOffense.setFont(getFontPath());
        tokenDefense.setFont(getFontPath());
        this.card = card;
        this.tokenType = tokenType;
        portrait = Farstar.ASSET_LIBRARY.getAssetManager().get(Farstar.ASSET_LIBRARY.getPortraitName(card.getCardInfo(), tokenType));
        frame = Farstar.ASSET_LIBRARY.getAssetManager().get(Farstar.ASSET_LIBRARY.getFrameName(card.getCardInfo(), tokenType));
        setWidth(tokenType.getWidth());
        setHeight(tokenType.getHeight());
        setX(x);
        setY(y);
        this.cardListMenu = cardListMenu;
        this.battleStage = battleStage;
        battleStage.addActor(this);
    }



    public Token(Card card, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType) {
        this.card = card;
        this.battleStage = battleStage;
        this.cardListMenu = cardListMenu;
        this.tokenType = tokenType;
    }

    public void draw(Batch batch) { //needs mem-perf rework
        if (card != null) {
            //Portrait + Frame
            batch.draw(portrait, getX(), getY());
            batch.draw(frame, getX(), getY());
            //Price
            Color color = new Color();
            color.set(1, 1, 1, 1);
            if (this instanceof YardToken || this instanceof HandToken || this instanceof FakeToken) {
                tokenPrice.drawText(tokenPrice.getFont(), batch, getX(), getY() + getHeight() * 4 / 3, card.getCardInfo().getEnergy() + ":" + card.getCardInfo().getMatter(), color);
            }
            //Name
            if (card instanceof Ship) {
                if (((Ship) card).haveFought()) {
                    color.set(1, 0, 0, 1);
                }
            }
            if (getCard().isPossible()) {
                color.set(0, 1, 0, 1);
            } else if (getCard().isInDuel()) {
                color.set(1, 1, 0, 1);
            }
            tokenName.drawText(tokenName.getFont(), batch, getX(), getY() + getHeight(), card.getCardInfo().getName(), color);
            //Offense+Defense
            color = ColorPalette.getTypeColor(card.getCardInfo().getOffenseType());
            if (!getCard().isMS()) {
                tokenOffense.drawText(tokenOffense.getFont(), batch, getX(), getY() + getHeight() / 3, String.valueOf(card.getCardInfo().getOffense()), color);
            }
            color = ColorPalette.getTypeColor(card.getCardInfo().getDefenseType());
            tokenDefense.drawText(tokenDefense.getFont(), batch, getX() + getWidth() * 5 / 6, getY() + getHeight() / 3, String.valueOf(card.getHealth()), color);
        }
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
