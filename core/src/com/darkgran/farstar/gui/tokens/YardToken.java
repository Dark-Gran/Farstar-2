package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.YardMenu;

public class YardToken extends ClickToken implements FakingTokens{
    private final YardMenu yardMenu;

    public YardToken(BattleCard battleCard, float x, float y, BattleStage battleStage, YardMenu yardMenu) {
        super(battleCard, x, y, battleStage, yardMenu, TokenType.YARD, false, true);
        this.yardMenu = yardMenu;
        setTouchable(Touchable.disabled);
    }

    @Override
    boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0) {
            if (yardMenu.isOpen() && getBattleStage().getMainDrag() == null && getBattleStage().getBattleScreen().getBattle().getRoundManager().isTokenMoveEnabled(this)) {
                newFake(event, x, y, pointer, button, FakeTokenType.YARD);
                if (getCard().getBattlePlayer().getFleet().countShips() >= 5) {
                    yardMenu.switchVisibility(false);
                }
                setPicked(true);
            }
            return false;
        } else {
            return super.touchDown(event, x, y, pointer, button);
        }
    }
}
