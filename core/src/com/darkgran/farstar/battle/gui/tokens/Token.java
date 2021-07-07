package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.util.SimpleBox2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class Token extends Actor implements JustFont {
    private String fontPath = "";
    private Dragger dragger;
    private final Card card;
    private final TokenDefense tokenDefense;
    private final TokenOffense tokenOffense;
    private final TokenPrice tokenPrice;
    private final BattleStage battleStage;
    private final CardListMenu cardListMenu;
    private final TokenType tokenType;
    private Texture portrait;
    private Texture frame;
    private boolean noPics;

    public Token(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics){
        setWidth(tokenType.getWidth());
        setHeight(tokenType.getHeight());
        setFont(tokenType.getFontPath());
        this.card = card;
        this.tokenType = tokenType;
        this.noPics = noPics;
        if (!noPics) {
            portrait = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getPortraitName(card.getCardInfo(), tokenType));
            frame = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getFrameName(card.getCardInfo(), tokenType));
        }
        this.cardListMenu = cardListMenu;
        this.battleStage = battleStage;
        tokenDefense = new TokenDefense(getFontPath(), this);
        tokenOffense = new TokenOffense(getFontPath(), this);
        tokenPrice = new TokenPrice(getFontPath(), this);
        setPosition(x, y);
        if (card != null) {
            card.setToken(this);
        }
        battleStage.addActor(this);
    }

    public Token(Card card, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType) {
        setWidth(tokenType.getWidth());
        setHeight(tokenType.getHeight());
        setX(0);
        setY(0);
        this.card = card;
        this.battleStage = battleStage;
        this.cardListMenu = cardListMenu;
        this.tokenType = tokenType;
        tokenDefense = new TokenDefense(getFontPath(), this);
        tokenOffense = new TokenOffense(getFontPath(), this);
        tokenPrice = new TokenPrice(getFontPath(), this);
        setParts();
        if (card != null) {
            card.setToken(this);
        }
    }

    public void setParts() {
        tokenDefense.setX(getX() + getWidth());
        tokenDefense.setY(getY());
        tokenOffense.setX(getX());
        tokenOffense.setY(getY());
        tokenPrice.setX(getX());
        tokenPrice.setY(getY() + getHeight());
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        setParts();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        setParts();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        setParts();
    }

    public void draw(Batch batch) {
        if (card != null) {
            drawPortrait(batch);
            tokenDefense.draw(batch);
            tokenOffense.draw(batch);
            tokenPrice.draw(batch);
        }
        //Debug
        //if (DEBUG_RENDER) { battleStage.getBattleScreen().drawDebugSimpleBox2(new SimpleBox2(getX(), getY(), getWidth(), getHeight()), battleStage.getBattleScreen().getShapeRenderer(), batch); }
    }

    protected void drawPortrait(Batch batch) {
        if (portrait != null) { batch.draw(portrait, getX(), getY()); }
        if (frame != null) { batch.draw(frame, getX(), getY()); }
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

    public boolean isNoPics() {
        return noPics;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public TokenDefense getTokenDefense() {
        return tokenDefense;
    }

    public TokenOffense getTokenOffense() {
        return tokenOffense;
    }

    public TokenPrice getTokenPrice() {
        return tokenPrice;
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }

    public Texture getPortrait() {
        return portrait;
    }

    public Texture getFrame() {
        return frame;
    }

    public void setPortrait(Texture portrait) {
        this.portrait = portrait;
    }

    public void setFrame(Texture frame) {
        this.frame = frame;
    }
}
