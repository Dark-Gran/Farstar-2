package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.DeltaCounter;

public abstract class TokenZoom extends PrintToken {
    private final DeltaCounter counter = new DeltaCounter(false, 10, 0);

    public TokenZoom(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
    }

    public void update(float delta) {
        counter.update(delta);
    }

    @Override
    public void draw(Batch batch) {
        if (getCard() != null && getTargetType() != null && getTargetXY() != null) {
            super.draw(batch);
        }
    }

}
