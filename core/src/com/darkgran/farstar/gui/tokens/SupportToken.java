package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

public class SupportToken extends ClickToken implements DisableMark {
    private TextureRegion disableMark;

    public SupportToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.SUPPORT, false, true);
        setMark(AssetLibrary.getInstance().getAtlasRegion("disable-S"));
        setZIndex(0);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        drawMark(batch, getX(), getY());
    }

    @Override
    void click(int button) {
        super.click(button);
        if (button == 0) {
            getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(this, getCard().getBattlePlayer());
        }
    }

    @Override
    public void destroy() {
        remove();
        if (getCardListMenu()!=null) {
            getCardListMenu().getTokens().remove(this);
            getCardListMenu().getCardList().remove(getCard());
            getCardListMenu().getBattleStage().getAnimationManager().newDeathEffect(getX(), getY(), TokenType.SUPPORT);
        }
    }

    @Override
    public TextureRegion getMark() {
        return disableMark;
    }

    @Override
    public void setMark(TextureRegion texture) {
        disableMark = texture;
    }

    @Override
    public boolean isDisabled() {
        return getCard().isUsed();
    }
}
