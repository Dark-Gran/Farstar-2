package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;

public class AbilityPickerOption extends ImageButton {

    public AbilityPickerOption(Texture texture, Battle battle, AbilityInfo abilityInfo) {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                battle.getRoundManager().processPick(abilityInfo);
            }
        });
    }

}
