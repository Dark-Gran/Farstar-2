package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleVector2;

/**
 * TokenZoom with an instant but limited show-up and a ClickListener.
 */

public class Herald extends TokenZoom {
    private final ClickListener clickListener = new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Herald.this.touched();
            return super.touchDown(event, x, y, pointer, button);
        }
    };

    public Herald(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, int counterCap) {
        super(card, x, y, battleStage, cardListMenu, counterCap);
        setTouchable(Touchable.disabled);
        addListener(clickListener);
    }

    @Override
    protected void drawGlows(Batch batch) { }

    public void touched() {
        disable();
        setTouchable(Touchable.disabled);
    }

    @Override
    public void update(float delta) {
        getCounter().update();
        if (!isHidden() && !getCounter().isEnabled()) {
            disable();
            setTouchable(Touchable.disabled);
        }
    }

    @Override
    public void enable(Card card, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != card) {
            setTouchable(Touchable.enabled);
            if (getCounter().isEnabled()) {
                getCounter().setCount(0);
            }
            setHidden(false);
            getCounter().setEnabled(true);
            setup(card, targetType, targetXY);
        }
    }
}
