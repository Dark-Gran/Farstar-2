package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleCounter;
import com.darkgran.farstar.util.SimpleVector2;

public abstract class TokenZoom extends PrintToken {
    private final SimpleCounter counter;
    private boolean activated;
    private boolean hidden = true;

    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, int counterCap) {
        super(card, x, y, battleStage, cardListMenu);
        counter = new SimpleCounter(false, counterCap, 0);
    }

    public void update(float delta) {
        counter.update();
        if (activated && !counter.isEnabled()) {
            activated = false;
            hidden = false;
        }
    }

    @Override
    public void enable(Card card, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != card) {
            if (targetType == TokenType.HAND || targetType == TokenType.YARD) {
                hidden = false;
            } else {
                activated = true;
                counter.setEnabled(true);
            }
            setup(card, targetType, targetXY);
        }
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void draw(Batch batch) {
        if (!hidden && getCard() != null && getTargetType() != null && getTargetXY() != null) {
            super.draw(batch);
        }
    }

    public SimpleCounter getCounter() {
        return counter;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
