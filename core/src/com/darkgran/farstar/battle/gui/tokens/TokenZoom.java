package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleCounter;
import com.darkgran.farstar.util.SimpleVector2;

public abstract class TokenZoom extends PrintToken {
    private final SimpleCounter counter = new SimpleCounter(false, 30, 0);
    private boolean activated;
    private boolean visible;

    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
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
        }
        super.enable(card, targetType, targetXY);
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

}
