package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.Effect;
import com.darkgran.farstar.battle.players.ai.Automaton;
import com.darkgran.farstar.battle.players.ai.BotTier;
import com.darkgran.farstar.battle.players.cards.CardInfo;
import com.darkgran.farstar.battle.players.cards.Mothership;

import java.util.ArrayList;

public interface InstanceFactory {

    //Creators
    static Player createLocalPlayer(int playerID, int mothershipId) {
        return new LocalPlayer((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard());
    }

    static Player createAutomaton(int playerID, int mothershipId) {
        return new Automaton((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard(), BotTier.AUTOMATON);
    }

    //Copycats
    static CardInfo instanceCardInfo(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getCardType(), cardInfo.getTier(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense(), cardInfo.getOffenseType(), cardInfo.getDefenseType(), cardInfo.getAbilities());
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
