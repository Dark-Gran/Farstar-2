package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.Player;

public class DuelManager1v1 extends DuelManager {

    @Override
    public void launchDuel(Token attacker, Token defender, Player[] playersA, Player[] playersD) {
        setActive(true);

    }

}
