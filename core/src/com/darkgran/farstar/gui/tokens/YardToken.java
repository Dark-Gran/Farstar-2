package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
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
            if (yardMenu.isOpen() && getCard().getPlayer() instanceof LocalBattlePlayer &&  !getBattleStage().getAbilityPicker().isActive() && !yardMenu.getBattleStage().getBattleScreen().getBattle().getCombatManager().isActive() && !yardMenu.getBattleStage().getBattleScreen().getBattle().isEverythingDisabled()) {
                newFake(event, x, y, pointer, button, FakeTokenType.YARD);
                if (getCard().getPlayer().getFleet().countShips() >= 5) {
                    yardMenu.setOpen(false);
                }
                setPicked(true);
            }
            return false;
        } else {
            return super.touchDown(event, x, y, pointer, button);
        }
    }
}
