package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.cards.AbilityInfo;

import java.util.ArrayList;

public class AbilityPicker extends BPB2DrawerBattle {
    private boolean active = false;
    private BattleCard battleCard = null;
    private ArrayList<AbilityInfo> abilityInfos = new ArrayList<>();
    private ArrayList<AbilityPickerOption> abilityGraphics = new ArrayList<>();
    private final Texture texture;
    private final float originX;
    private final float originY;
    private final static float SPACE_BETWEEN = 30f;


    public AbilityPicker(float x, float y, BattleStage battleStage, BattlePlayer battlePlayer, Texture texture) {
        super(x, y, battleStage, battlePlayer);
        originX = x;
        originY = y;
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch) {
        if (active) {
            for (AbilityPickerOption option : abilityGraphics) {
                option.draw(batch);
            }
        }
    }

    public void enable(BattleCard battleCard) {
        this.battleCard = battleCard;
        active = true;
        refreshOptions();
        addActors();
    }

    public void disable() {
        removeActors();
        active = false;
    }

    private void refreshOptions() {
        if (battleCard != null) {
            this.x = (originX-(((abilityInfos.size() * TokenType.PRINT.getWidth())+(abilityInfos.size()-1) * SPACE_BETWEEN)/2));
            abilityGraphics = new ArrayList<>();
            for (int i = 0; i < abilityInfos.size(); i++) {
                abilityGraphics.add(createOption(abilityInfos.get(i), i));
            }
        }
    }

    private void removeActors() {
        for (AbilityPickerOption abilityPickerOption : abilityGraphics) {
            abilityPickerOption.remove();
        }
    }

    private void addActors() {
        for (AbilityPickerOption abilityPickerOption : abilityGraphics) {
            getBattleStage().addActor(abilityPickerOption);
        }
    }

    private AbilityPickerOption createOption(AbilityInfo abilityInfo, int optionNumber) {
        AbilityPickerOption abilityPickerOption = new AbilityPickerOption(getBattleStage().getBattleScreen().getBattle(), abilityInfo, battleCard, x, y, getOptionDescription(battleCard.getCardInfo().getDescription(), optionNumber));
        abilityPickerOption.setBounds(x+(abilityGraphics.size()*(TokenType.PRINT.getWidth()+SPACE_BETWEEN)), y, TokenType.PRINT.getWidth(), TokenType.PRINT.getHeight());
        return abilityPickerOption;
    }

    /** Expects Hybrid-Descriptions to carry optionDescriptions on odd new-lines (\n). */
    public String getOptionDescription(String description, int optionNumber) {
        String optionDescription = "";
        String[] descArr = description.split("\n");
        int optionCount = 0;
        for (int i = 0; i < descArr.length; i++) {
            if (i % 2 != 0) {
                if (optionCount == optionNumber) {
                    optionDescription = descArr[i];
                }
                optionCount++;
            }
        }
        return optionDescription;
    }

    public ArrayList<AbilityInfo> getAbilityInfos() { return abilityInfos; }

    public void setAbilityInfos(ArrayList<AbilityInfo> abilityInfos) { this.abilityInfos = abilityInfos; }

    public ArrayList<AbilityPickerOption> getAbilityGraphics() { return abilityGraphics; }

    public void setAbilityGraphics(ArrayList<AbilityPickerOption> abilityGraphics) { this.abilityGraphics = abilityGraphics; }

    public BattleCard getCard() {
        return battleCard;
    }

    public boolean isActive() { return active; }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void dispose() {
        removeActors();
    }

}
