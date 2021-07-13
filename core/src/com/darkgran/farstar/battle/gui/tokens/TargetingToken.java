package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class TargetingToken extends FakeToken {
    private Dragger dragger;

    public TargetingToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, true);
    }

    @Override
    public Dragger getDragger() {
        return dragger;
    }

    @Override
    public void setDragger(Dragger dragger) {
        this.dragger = dragger;
    }
}
