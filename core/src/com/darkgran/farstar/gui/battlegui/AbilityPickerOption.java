package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.cards.Card;
import com.darkgran.farstar.gui.tokens.PrintToken;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.gui.SimpleVector2;

public class AbilityPickerOption extends PrintToken {
    private ClickListener clickListener;
    private final String optionDescription;

    public AbilityPickerOption(Battle battle, AbilityInfo abilityInfo, BattleCard battleCard, float x, float y, String optionDescription) {
        super(battleCard, x, y, battle.getBattleScreen().getBattleStage(), null, false);
        this.optionDescription = optionDescription;
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
        setup(battleCard, TokenType.PRINT, new SimpleVector2(x, y));
    }

    @Override
    public String getCardDescription(Card card) {
        return optionDescription;
    }

    @Override
    public void setGlows() { }

    public ClickListener getClickListener() {
        return clickListener;
    }

}
