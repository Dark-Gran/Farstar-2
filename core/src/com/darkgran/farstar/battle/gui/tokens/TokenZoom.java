package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.ColorPalette;
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
    private Explainer explainer = new Explainer(
            ColorPalette.LIGHT,
            ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f),
            "fonts/bahnschrift30.fnt",
            "test"
    );

    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, int counterCap) {
        super(card, x, y, battleStage, cardListMenu, false);
        counter = new SimpleCounter(false, counterCap, 0);
    }

    public void update(float delta) {
        counter.update();
    }

    public void deactivate(boolean rightClick) {
        if (!rightClick || !(getTargetType() != null && (getTargetType() == TokenType.HAND || getTargetType() == TokenType.JUNK))) {
            deactivate();
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
        enabled = true;
        if (getCard() != null && getTargetType() != null) {
            hidden = false;
        }
    }

    @Override
    public void enable(Card card, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != card) {
            hidden = false;
            super.enable(card, targetType, targetXY);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (explainer != null) { explainer.setPosition(x, y); }
    }

    @Override
    public void draw(Batch batch) {
        if (isEnabled() && !hidden && getCard() != null && getTargetType() != null && getTargetXY() != null) {
            super.draw(batch);
            explainer.draw(batch, getBattleStage().getBattleScreen().getShapeRenderer());
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
