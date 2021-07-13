package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class SupportToken extends ClickToken implements DisableMark {
    private Texture disableMark;

    public SupportToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.SUPPORT, false, true);
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
            getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(this, getCard().getPlayer());
        }
    }

    @Override
    public void destroy() {
        remove();
        if (getCardListMenu()!=null) {
            getCardListMenu().getTokens().remove(this);
            getCardListMenu().getCardList().remove(getCard());
        }
    }

    @Override
    public Texture getMark() {
        return disableMark;
    }

    @Override
    public void setMark(Texture texture) {
        disableMark = texture;
    }

    @Override
    public boolean isDisabled() {
        return getCard().isUsed();
    }
}
