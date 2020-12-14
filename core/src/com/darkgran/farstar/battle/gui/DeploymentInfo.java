package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;

public class DeploymentInfo {
    private Token caster = null;
    private DropTarget drop = null;
    private AbilityInfo ability = null;
    private Token target = null;
    private int position = 0; //the only one saved on each fleet-drop

    public void saveInDeployment(Token caster, AbilityInfo ability, DropTarget dropTarget, Token target) {
        this.caster = caster;
        this.target = target;
        this.drop = dropTarget;
        this.ability = ability;
    }

    public void resetInDeployment() {
        caster = null;
        drop = null;
        ability = null;
        target = null;
        position = 0;
    }

    public Token getCaster() {
        return caster;
    }

    public void setCaster(Token caster) {
        this.caster = caster;
    }

    public DropTarget getDrop() {
        return drop;
    }

    public void setDrop(DropTarget drop) {
        this.drop = drop;
    }

    public AbilityInfo getAbility() {
        return ability;
    }

    public void setAbility(AbilityInfo ability) {
        this.ability = ability;
    }

    public Token getTarget() {
        return target;
    }

    public void setTarget(Token target) {
        this.target = target;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
