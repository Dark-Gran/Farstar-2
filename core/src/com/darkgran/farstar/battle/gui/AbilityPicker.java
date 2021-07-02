package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.gui.PB2Drawer;

import java.util.ArrayList;

public class AbilityPicker extends PB2Drawer {
    private boolean active = false;
    private ArrayList<AbilityInfo> abilityInfos = new ArrayList<>();
    private ArrayList<AbilityPickerOption> abilityGraphics = new ArrayList<>();
    private final Texture texture;

    public AbilityPicker(float x, float y, BattleStage battleStage, Player player, Texture texture) {
        super(x, y, battleStage, player);
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch) {
        //super.draw(batch);
    }

    public void enable() {
        active = true;
        refreshOptions();
        addActors();
    }

    public void disable() {
        removeActors();
        active = false;
    }

    private void refreshOptions() {
        abilityGraphics = new ArrayList<>();
        for (AbilityInfo abilityInfo : abilityInfos) {
            abilityGraphics.add(createOption(abilityInfo));
        }
    }

    private void removeActors(int fromIndex, int toIndex) {
        for (int i = fromIndex; i <= toIndex && i < abilityGraphics.size(); i++) {
            abilityGraphics.get(i).remove();
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

    private AbilityPickerOption createOption(AbilityInfo abilityInfo) {
        AbilityPickerOption abilityPickerOption = new AbilityPickerOption(texture, getBattleStage().getBattleScreen().getBattle(), abilityInfo);
        abilityPickerOption.setBounds(getX()+((abilityGraphics.size())*(float) Farstar.STAGE_WIDTH/20), getY(), (float) Farstar.STAGE_WIDTH/20,(float) Farstar.STAGE_HEIGHT/20);
        return abilityPickerOption;
    }

    public ArrayList<AbilityInfo> getAbilityInfos() { return abilityInfos; }

    public void setAbilityInfos(ArrayList<AbilityInfo> abilityInfos) { this.abilityInfos = abilityInfos; }

    public ArrayList<AbilityPickerOption> getAbilityGraphics() { return abilityGraphics; }

    public void setAbilityGraphics(ArrayList<AbilityPickerOption> abilityGraphics) { this.abilityGraphics = abilityGraphics; }

    public boolean isActive() { return active; }

    public void dispose() {
        removeActors();
    }

}
