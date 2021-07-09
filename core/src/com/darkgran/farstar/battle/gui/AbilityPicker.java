package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.gui.PB2Drawer;

import java.util.ArrayList;

public class AbilityPicker extends PB2Drawer {
    private boolean active = false;
    private Card card = null;
    private ArrayList<AbilityInfo> abilityInfos = new ArrayList<>();
    private ArrayList<AbilityPickerOption> abilityGraphics = new ArrayList<>();
    private final Texture texture;
    private final float originX;
    private final float originY;
    private final static float SPACE_BETWEEN = 30f;

    public AbilityPicker(float x, float y, BattleStage battleStage, Player player, Texture texture) {
        super(x, y, battleStage, player);
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

    public void enable(Card card) {
        this.card = card;
        active = true;
        refreshOptions();
        addActors();
    }

    public void disable() {
        removeActors();
        active = false;
    }

    private void refreshOptions() {
        if (card != null) {
            setX(originX-(((abilityInfos.size() * TokenType.PRINT.getWidth())+(abilityInfos.size()-1) * SPACE_BETWEEN)/2));
            abilityGraphics = new ArrayList<>();
            for (AbilityInfo abilityInfo : abilityInfos) {
                abilityGraphics.add(createOption(abilityInfo));
            }
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
        AbilityPickerOption abilityPickerOption = new AbilityPickerOption(getBattleStage().getBattleScreen().getBattle(), abilityInfo, card, getX(), getY());
        abilityPickerOption.setBounds(getX()+(abilityGraphics.size()*(TokenType.PRINT.getWidth()+SPACE_BETWEEN)), getY(), TokenType.PRINT.getWidth(), TokenType.PRINT.getHeight());
        return abilityPickerOption;
    }

    public ArrayList<AbilityInfo> getAbilityInfos() { return abilityInfos; }

    public void setAbilityInfos(ArrayList<AbilityInfo> abilityInfos) { this.abilityInfos = abilityInfos; }

    public ArrayList<AbilityPickerOption> getAbilityGraphics() { return abilityGraphics; }

    public void setAbilityGraphics(ArrayList<AbilityPickerOption> abilityGraphics) { this.abilityGraphics = abilityGraphics; }

    public Card getCard() {
        return card;
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
