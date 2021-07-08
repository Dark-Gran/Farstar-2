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
    private boolean visible;

    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, int counterCap) {
        super(card, x, y, battleStage, cardListMenu);
        counter = new SimpleCounter(false, counterCap, 0);
    }

    public void update(float delta) {
        counter.update();
        if (activated && !counter.isEnabled()) {
            activated = false;
            visible = true;
        }
    }

    @Override
    public void enable(Card card, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != card) {
            if (targetType == TokenType.HAND || targetType == TokenType.YARD) {
                visible = true;
            } else {
                activated = true;
                counter.setEnabled(true);
            }
            setup(card, targetType, targetXY);
        }
    }

    @Override
    public void disable() {
        visible = false;
        super.disable();
    }

    @Override
    public void draw(Batch batch) {
        if (visible && getCard() != null && getTargetType() != null && getTargetXY() != null) {
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

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
