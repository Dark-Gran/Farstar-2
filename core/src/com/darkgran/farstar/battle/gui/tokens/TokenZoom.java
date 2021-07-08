package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleCounter;
import com.darkgran.farstar.util.SimpleVector2;

public abstract class TokenZoom extends PrintToken {
    private final SimpleCounter counter;
    private boolean enabled = true;
    private boolean counting;
    private boolean hidden = true;

    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, int counterCap) {
        super(card, x, y, battleStage, cardListMenu);
        counter = new SimpleCounter(false, counterCap, 0);
    }

    public void update(float delta) {
        counter.update();
        if (counting && !counter.isEnabled()) {
            counting = false;
            hidden = false;
        }
    }

    public void deactivate() {
        enabled = false;
        hidden = true;
        counter.setEnabled(false);
        counter.setCount(0);
        counting = false;
    }

    public void reactivate() {
        if (getCard() != null && getTargetType() != null) {
            enabled = true;
            if (getTargetType() == TokenType.HAND || getTargetType() == TokenType.YARD) {
                hidden = false;
            } else {
                counting = true;
                counter.setEnabled(true);
            }
        }
    }

    @Override
    public void enable(Card card, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != card) {
            if (targetType == TokenType.HAND || targetType == TokenType.YARD) {
                hidden = false;
            } else {
                hidden = true;
                counting = true;
                counter.setEnabled(true);
            }
            setup(card, targetType, targetXY);
        }
    }

    @Override
    public void draw(Batch batch) {
        if (isEnabled() && !hidden && getCard() != null && getTargetType() != null && getTargetXY() != null) {
            super.draw(batch);
        }
    }

    public SimpleCounter getCounter() {
        return counter;
    }

    public boolean isCounting() {
        return counting;
    }

    public void setCounting(boolean counting) {
        this.counting = counting;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
