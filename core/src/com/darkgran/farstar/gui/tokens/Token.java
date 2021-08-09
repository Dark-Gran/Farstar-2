package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.gui.SimpleBox2;
import com.darkgran.farstar.gui.SimpleVector2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class Token extends Actor implements JustFont { //in-future: split to BattleToken and abstract Token (for Deck screen etc)
    private String fontPath = "";
    private BattleCard battleCard;
    private final TokenDefense tokenDefense;
    private final TokenOffense tokenOffense;
    private final TokenPrice tokenPrice;
    private final BattleStage battleStage;
    private final CardListMenu cardListMenu;
    private final TokenType tokenType;
    private final boolean noPics;
    private TextureRegion portrait;
    private TextureRegion frame;
    private GlowState glowState = GlowState.DIM;
    private TextureRegion glowG;
    private TextureRegion glowY;
    private float glowOffsetX = 0f;
    private float glowOffsetY = 0f;
    public enum GlowState {
        DIM, POSSIBLE, PICKED
    }
    private boolean picked;

    public Token(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics, boolean connectCard){
        setWidth(tokenType.getWidth());
        setHeight(tokenType.getHeight());
        setFont(AssetLibrary.getFontPath(tokenType.getDefaultFontSize(), "bahnschrift"));
        this.battleCard = battleCard;
        this.tokenType = tokenType;
        this.noPics = noPics;
        if (!noPics) {
            if (battleCard != null) {
                portrait = Farstar.ASSET_LIBRARY.getPortrait(battleCard.getCardInfo(), tokenType);
                frame = Farstar.ASSET_LIBRARY.getAtlasRegion(Farstar.ASSET_LIBRARY.getFrameName(battleCard.getCardInfo(), tokenType));
                setGlows();
            }
        }
        this.cardListMenu = cardListMenu;
        this.battleStage = battleStage;
        tokenDefense = new TokenDefense(getFontPath(), this);
        tokenOffense = new TokenOffense(getFontPath(), this);
        tokenPrice = new TokenPrice(getFontPath(), this);
        setPosition(x, y);
        if (battleCard != null && connectCard) {
            battleCard.setToken(this);
            battleStage.addActor(this);
        }
    }

    public void setGlows() {
        glowG = Farstar.ASSET_LIBRARY.getAtlasRegion(AssetLibrary.addTokenTypeAcronym("glowG-", tokenType, false));
        glowY = Farstar.ASSET_LIBRARY.getAtlasRegion(AssetLibrary.addTokenTypeAcronym("glowY-", tokenType, false));
        glowOffsetX = -glowG.getRegionWidth()/2f+frame.getRegionWidth()/2f;
        glowOffsetY = -glowG.getRegionHeight()/2f+frame.getRegionHeight()/2f;
    }

    public void setParts() {
        tokenDefense.x = this instanceof FakeToken ? getX() + getWidth() : Math.round(getX() + getWidth());
        tokenDefense.x = this instanceof FakeToken ? getX() + getWidth() : Math.round(getX() + getWidth());
        tokenDefense.y = getY();
        tokenOffense.x = getX();
        tokenOffense.y = getY();
        tokenPrice.x = getX();
        tokenPrice.y = this instanceof FakeToken ? getY() + getHeight() : Math.round(getY() + getHeight());
    }

    public void setup(BattleCard battleCard, TokenType targetType, SimpleVector2 targetXY) { //used only by non-standard tokens (that do not connectCard)
        setCard(battleCard);
        setPortrait(Farstar.ASSET_LIBRARY.getPortrait(battleCard.getCardInfo(), getTokenType()));
        setFrame(Farstar.ASSET_LIBRARY.getAtlasRegion(Farstar.ASSET_LIBRARY.getFrameName(battleCard.getCardInfo(), getTokenType())));
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
        if (!(this instanceof FakeToken)) {
            x = Math.round(x);
            y = Math.round(y);
        }
        super.setPosition(x, y);
        setParts();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        if (!(this instanceof FakeToken)) {
            x = Math.round(x);
            y = Math.round(y);
        }
        super.setBounds(x, y, width, height);
        setParts();
    }

    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        draw(batch);
    }

    public void draw(Batch batch) {
        if (battleCard != null && !noPics) {
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
        BattleScreen.drawDebugSimpleBox2(new SimpleBox2(getX(), getY(), getWidth(), getHeight()), battleStage.getBattleScreen().getShapeRenderer(), batch);
    }

    protected void drawPortrait(Batch batch) {
        if (portrait != null) { batch.draw(portrait, getX(), getY()); }
        if (frame != null && SuperScreen.ScreenSettings.tokenFramesEnabled) { batch.draw(frame, getX(), getY()); }
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
            getCardListMenu().getBattlePlayer().getJunkpile().addCard(this.getCard());
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
        else {
            glowState = getCard().isPossible() ? GlowState.POSSIBLE : GlowState.DIM;
        }
    }

    public boolean isPicked() {
        return picked;
    }

    public BattleCard getCard() { return battleCard; }

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

    public TextureRegion getPortrait() {
        return portrait;
    }

    public TextureRegion getFrame() {
        return frame;
    }

    public void setPortrait(TextureRegion portrait) {
        this.portrait = portrait;
    }

    public void setFrame(TextureRegion frame) {
        this.frame = frame;
    }

    protected void setCard(BattleCard battleCard) {
        this.battleCard = battleCard;
    }

    public void setGlowState(GlowState glowState) {
        this.glowState = glowState;
    }

    public GlowState getGlowState() {
        return glowState;
    }

    public TextureRegion getGlowG() {
        return glowG;
    }

    public void setGlowG(TextureRegion glowG) {
        this.glowG = glowG;
    }

    public TextureRegion getGlowY() {
        return glowY;
    }

    public void setGlowY(TextureRegion glowY) {
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
