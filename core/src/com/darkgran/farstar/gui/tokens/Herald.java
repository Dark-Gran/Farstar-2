package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.SimpleVector2;

/**
 * TokenZoom with an instant but limited show-up and a ClickListener.
 */

public class Herald extends TokenZoom {

    public Herald(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, float counterCap) {
        super(battleCard, x, y, battleStage, cardListMenu, counterCap);
        setTouchable(Touchable.disabled);
        ClickListener clickListener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Herald.this.touched();
                return super.touchDown(event, x, y, pointer, button);
            }
        };
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
        getCounter().update(delta);
        if (!isHidden() && !getCounter().isEnabled()) {
            disable(); //in-future: fade-out?
            setTouchable(Touchable.disabled);
        }
    }

    @Override
    public void enable(BattleCard battleCard, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != battleCard) {
            setTouchable(Touchable.enabled);
            if (getCounter().isEnabled()) {
                getCounter().setAccumulator(0);
            }
            setHidden(false);
            getCounter().setEnabled(true);
            setup(battleCard, targetType, targetXY);
        }
    }

}
