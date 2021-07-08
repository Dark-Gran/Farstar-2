package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public abstract class TokenZoom extends PrintToken {
    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
    }

    @Override
    public void draw(Batch batch) {
        if (getCard() != null && getTargetType() != null && getTargetXY() != null) {
            super.draw(batch);
        }
    }
}
