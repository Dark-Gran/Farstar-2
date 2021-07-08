package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleVector2;

public abstract class ClickToken extends Token {
    private boolean zoomEnabled = true;

    private ClickListener clickListener = new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            getBattleStage().getCardZoom().setHidden(true);
            return true;
        }

        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            click(button);
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (zoomEnabled) { getBattleStage().getCardZoom().enable(getCard(), getTokenType(), new SimpleVector2(getX(), getY())); }
            super.enter(event, x, y, pointer, fromActor);
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (zoomEnabled) { getBattleStage().getCardZoom().disable(); }
            super.exit(event, x, y, pointer, toActor);
        }
    };

    public ClickToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics) {
        super(card, x, y, battleStage, cardListMenu, tokenType, noPics);
        this.addListener(clickListener);
    }

    public void click(int button) { }

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
