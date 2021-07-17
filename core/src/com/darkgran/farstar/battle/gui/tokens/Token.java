package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.AssetLibrary;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class Token extends Actor implements JustFont {
    private String fontPath = "";
    private Card card;
    private final TokenDefense tokenDefense;
    private final TokenOffense tokenOffense;
    private final TokenPrice tokenPrice;
    private final BattleStage battleStage;
    private final CardListMenu cardListMenu;
    private final TokenType tokenType;
    private boolean noPics;
    private Texture portrait;
    private Texture frame;
    private GlowState glowState = GlowState.DIM;
    private Texture glowG;
    private Texture glowY;
    private float glowOffsetX = 0f;
    private float glowOffsetY = 0f;
    public enum GlowState {
        DIM, POSSIBLE, PICKED;
    }
    private boolean picked;

    public Token(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics, boolean connectCard){
        setWidth(tokenType.getWidth());
        setHeight(tokenType.getHeight());
        setFont(AssetLibrary.getFontPath(tokenType.getFontSize(), "bahnschrift"));
        this.card = card;
        this.tokenType = tokenType;
        this.noPics = noPics;
        if (!noPics) {
            if (card != null) {
                portrait = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getPortraitName(card.getCardInfo(), tokenType));
                frame = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getFrameName(card.getCardInfo(), tokenType));
                setGlows();
            }
        }
        this.cardListMenu = cardListMenu;
        this.battleStage = battleStage;
        tokenDefense = new TokenDefense(getFontPath(), this);
        tokenOffense = new TokenOffense(getFontPath(), this);
        tokenPrice = new TokenPrice(getFontPath(), this);
        setPosition(x, y);
        if (card != null && connectCard) {
            card.setToken(this);
        }
        battleStage.addActor(this);
    }

    public Token(Card card, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean connectCard) {
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
        if (card != null && connectCard) {
            card.setToken(this);
        }
    }

    public void setGlows() {
        glowG = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/glowG", tokenType, false)+".png");
        glowY = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/glowY", tokenType, false)+".png");
        glowOffsetX = -glowG.getWidth()/2f+frame.getWidth()/2f;
        glowOffsetY = -glowG.getHeight()/2f+frame.getHeight()/2f;
    }

    public void setParts() {
        tokenDefense.setX(getX() + getWidth());
        tokenDefense.setY(getY());
        tokenOffense.setX(getX());
        tokenOffense.setY(getY());
        tokenPrice.setX(getX());
        tokenPrice.setY(getY() + getHeight());
    }

    public void setup(Card card, TokenType targetType, SimpleVector2 targetXY) { //used only by non-standard tokens (that do not connectCard)
        setCard(card);
        setPortrait(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getPortraitName(card.getCardInfo(), getTokenType())));
        setFrame(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getFrameName(card.getCardInfo(), getTokenType())));
        getTokenDefense().update();
        getTokenDefense().setPad(getTokenType());
        getTokenDefense().setupOffset();
        getTokenOffense().update();
        getTokenOffense().setPad(getTokenType());
        getTokenOffense().setupOffset();
        getTokenPrice().update();
        getTokenPrice().setPad(getTokenType());
        getTokenPrice().setupOffset();
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

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        setParts();
    }

    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        draw(batch);
    }

    public void draw(Batch batch) {
        if (card != null && !noPics) {
            drawGlows(batch);
            drawPortrait(batch);
            tokenDefense.draw(batch);
            tokenOffense.draw(batch);
            tokenPrice.draw(batch);
        }
        if (DEBUG_RENDER) {
            debugRender(batch);
        }
    }

    protected void drawGlows(Batch batch) {
        if (glowG != null) {
            switch (glowState) {
                case POSSIBLE:
                    batch.draw(glowG, getX() + glowOffsetX, getY() + glowOffsetY);
                    break;
                case PICKED:
                    batch.draw(glowY, getX() + glowOffsetX, getY() + glowOffsetY);
                    break;
            }
        }
    }

    protected void debugRender(Batch batch) {
        battleStage.getBattleScreen().drawDebugSimpleBox2(new SimpleBox2(getX(), getY(), getWidth(), getHeight()), battleStage.getBattleScreen().getShapeRenderer(), batch);
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
        getCard().setPossible(false);
        setPicked(false);
        if (getCardListMenu() != null) {
            getCardListMenu().getPlayer().getJunkpile().addCard(this.getCard());
        } else if (this instanceof FleetToken) {
            FleetToken fleetToken = (FleetToken) this;
            if (fleetToken.getFleetMenu() != null) {
                fleetToken.getFleetMenu().getFleet().getJunkpile().addCard(this.getCard());
            }
        }
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
        if (picked) { glowState = GlowState.PICKED; }
    }

    public boolean isPicked() {
        return picked;
    }

    public Card getCard() { return card; }

    public BattleStage getBattleStage() { return battleStage; }

    public CardListMenu getCardListMenu() { return cardListMenu; }



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

    protected void setCard(Card card) {
        this.card = card;
    }

    public void setGlowState(GlowState glowState) {
        this.glowState = glowState;
    }

    public GlowState getGlowState() {
        return glowState;
    }

    public Texture getGlowG() {
        return glowG;
    }

    public void setGlowG(Texture glowG) {
        this.glowG = glowG;
    }

    public Texture getGlowY() {
        return glowY;
    }

    public void setGlowY(Texture glowY) {
        this.glowY = glowY;
    }

    public float getGlowOffsetX() {
        return glowOffsetX;
    }

    public void setGlowOffsetX(float glowOffsetX) {
        this.glowOffsetX = glowOffsetX;
    }

    public float getGlowOffsetY() {
        return glowOffsetY;
    }

    public void setGlowOffsetY(float glowOffsetY) {
        this.glowOffsetY = glowOffsetY;
    }
}
