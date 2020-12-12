package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;

import java.util.ArrayList;

public class AbilityPicker extends BaseMenu {
    private boolean active = false;
    private ArrayList<AbilityInfo> abilityInfos = new ArrayList<>();
    private ArrayList<AbilityPickerOption> abilityGraphics = new ArrayList<>();
    private final Texture texture;

    public AbilityPicker(float x, float y, BattleStage battleStage, Player player, Texture texture) {
        super(x, y, false, battleStage, player);
        this.texture = texture;
    }

    public void draw() { }

    public void enable() {
        active = true;
        refreshOptions();
        addActors();
    }

    public void disable() {
        removeActors();
        active = false;
    }

    public void refreshOptions() {
        for (int i = 0; i < abilityInfos.size(); i++) {
            if (i >= abilityGraphics.size()) {
                abilityGraphics.add(createOption(abilityInfos.get(i)));
            }
        }
        if (abilityGraphics.size() > abilityInfos.size()) {
            removeActors(abilityInfos.size(), abilityGraphics.size());
            abilityGraphics.subList(abilityInfos.size(), abilityGraphics.size()).clear();
        }
    }

    public void removeActors(int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            abilityGraphics.get(i).remove();
        }
    }

    public void removeActors() {
        for (AbilityPickerOption abilityPickerOption : abilityGraphics) {
            abilityPickerOption.remove();
        }
    }

    public void addActors() {
        for (AbilityPickerOption abilityPickerOption : abilityGraphics) {
            getBattleStage().addActor(abilityPickerOption);
        }
    }

    public AbilityPickerOption createOption(AbilityInfo abilityInfo) {
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
        texture.dispose();
    }

}
