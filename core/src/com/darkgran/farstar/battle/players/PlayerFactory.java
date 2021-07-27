package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

import java.util.Locale;

public class PlayerFactory {

    public BattlePlayer getPlayer(String playerType, int playerID, int mothershipId) {
        if (playerType == null) {
            return null;
        }
        switch (playerType.toUpperCase(Locale.ROOT)) {
            case "LOCAL":
                return new LocalBattlePlayer((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard());
            case "AUTO":
                return new Automaton((byte) playerID, BattleSettings.STARTING_ENERGY, BattleSettings.STARTING_MATTER, new Mothership(mothershipId), new Deck(), new Yard(), BotSettings.BotTier.AUTOMATON);
        }
        return null;
    }

}
