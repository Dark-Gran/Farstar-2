package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.tokens.PrintToken;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.cards.Card;

public class AbilityPickerOption extends PrintToken {
    private ClickListener clickListener;

    public AbilityPickerOption(Battle battle, AbilityInfo abilityInfo, Card card, float x, float y) {
        super(card, x, y, battle.getBattleScreen().getBattleStage(), null, false);
        clickListener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (button == 0) {
                    battle.getRoundManager().processPick(abilityInfo);
                } else if (button == 1) {
                    battle.getRoundManager().tryCancel();
                }
            }
        };
        addListener(clickListener);
        setTouchable(Touchable.enabled);
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

}
