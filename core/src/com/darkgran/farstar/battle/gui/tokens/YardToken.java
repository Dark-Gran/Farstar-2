package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.YardMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public class YardToken extends ClickToken {
    private final YardMenu yardMenu;

    public YardToken(Card card, float x, float y, BattleStage battleStage, YardMenu yardMenu) {
        super(card, x, y, battleStage, yardMenu, TokenType.YARD, false, true);
        this.yardMenu = yardMenu;
        setTouchable(Touchable.disabled);
        this.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (button == 0) {
                    if (yardMenu.isOpen() && !yardMenu.getBattleStage().getBattleScreen().getBattle().getCombatManager().isActive() && !yardMenu.getBattleStage().getBattleScreen().getBattle().isEverythingDisabled()) {
                        getBattleStage().setFakeToken(new FakeToken(getCard(), getX(), getY(), getBattleStage(), getCardListMenu()));
                        event.setRelatedActor(getBattleStage().getFakeToken());
                        event.getStage().addTouchFocus(getBattleStage().getFakeToken().getDragger(), getBattleStage().getFakeToken(), getBattleStage().getFakeToken(), event.getPointer(), event.getButton());
                        getBattleStage().getFakeToken().getDragger().touchDown(event, x, y, pointer, button);
                    }
                    return false;
                } else {
                    return super.touchDown(event, x, y, pointer, button);
                }
            }
        });
    }

}
