package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.Effect;

import java.util.ArrayList;

public interface InstanceFactory {

    static CardInfo instanceCardInfo(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getCardType(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense(), cardInfo.getOffenseType(), cardInfo.getDefenseType(), cardInfo.getAbilities());
    }

    static ArrayList<AbilityInfo> instanceAbilities(ArrayList<AbilityInfo> abilities) {
        ArrayList<AbilityInfo> newAbilities = new ArrayList<>();
        newAbilities.addAll(abilities);
        return newAbilities;
    }

    static Effect instanceEffect(Effect effect) {
        return new Effect(effect.getEffectType(), effect.getEffectInfo(), effect.getDuration());
    }

}
