package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;

public class HandToken extends AnchoredToken {

    public HandToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.FLEET, false); //TokenType.FLEET = placeholder
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), false){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isEnabled() && button == 0 && getCard().getCardInfo().getCardType() == CardType.BLUEPRINT) {
                    getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(true);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.addListener(getDragger());
    }

    @Override
    public void destroy() {
        super.destroy();
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(false);
    }

}
