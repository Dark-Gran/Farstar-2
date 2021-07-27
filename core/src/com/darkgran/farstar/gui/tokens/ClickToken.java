package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.SimpleVector2;

public abstract class ClickToken extends Token {
    private boolean zoomEnabled = true;

    private final ClickListener clickListener = new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return ClickToken.this.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            click(button);
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (zoomEnabled && pointer == -1) { getBattleStage().getCardZoom().enable(getCard(), getTokenType(), new SimpleVector2(getX(), getY())); }
            super.enter(event, x, y, pointer, fromActor);
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (zoomEnabled && pointer == -1) { getBattleStage().getCardZoom().disable(); }
            super.exit(event, x, y, pointer, toActor);
        }
    };

    public ClickToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, tokenType, noPics, connectCard);
        this.addListener(clickListener);
    }

    boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    void click(int button) { }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }

}
