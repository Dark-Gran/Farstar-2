package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public abstract class ClickToken extends Token {
    private ClickListener clickListener = new ClickListener(){ };

    public ClickToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType) {
        super(card, x, y, battleStage, cardListMenu, tokenType);
        this.addListener(clickListener);
    }

    public void click(int button) { }

    public ClickListener getClickListener() {
        return clickListener;
    }

}
