package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.cards.Mothership;

import java.util.Locale;

public class PlayerFactory {

    public Player getPlayer(String playerType, int playerID, int mothershipId) {
        if (playerType == null) {
            return null;
        }
        switch (playerType.toUpperCase(Locale.ROOT)) {
            case "LOCAL":
                return new LocalPlayer((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard());
            case "AUTO":
                return new Automaton((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(20), new Yard(), BotSettings.BotTier.AUTOMATON);
        }
        return null;
    }

}
