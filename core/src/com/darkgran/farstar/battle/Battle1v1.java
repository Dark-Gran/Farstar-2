package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.gui.BattleMenu;
import com.darkgran.farstar.battle.gui.BattleMenu1v1;
import com.darkgran.farstar.battle.gui.GUI;
import com.darkgran.farstar.battle.gui.GUI1v1;

import java.util.concurrent.ThreadLocalRandom;

public class Battle1v1 extends Battle {
    private final Player player1;
    private final Player player2;

    public Battle1v1(Farstar game, Viewport viewport) {
        super();
        //in future: pass the Players in parameters
        player1 = new Player((byte) 1, STARTING_ENERGY, STARTING_MATTER, new Mothership(), new Deck(), new Shipyard());
        player2 = new Player((byte) 2, STARTING_ENERGY, STARTING_MATTER, new Mothership(), new Deck(), new Shipyard());
    }

    @Override
    public GUI createGUI(Farstar game, Viewport viewport) {
        return new GUI1v1(game, viewport, player1, player2);
    }

    @Override
    public BattleMenu createBattleMenu(Farstar game, Viewport viewport) {
        return new BattleMenu1v1(game, viewport);
    }

    @Override
    public void coinToss() {
        setWhoseTurn((ThreadLocalRandom.current().nextInt(0, 2) == 0) ? player1 : player2);
    }

    @Override
    public void passTurn() {
        setWhoseTurn(player1 == getWhoseTurn() ? player2 : player1);
    }

    @Override
    public void startingCards() {
        if (getWhoseTurn() == player1) {
            player1.getHand().drawCards(player1.getDeck(), STARTING_CARDS_ATT);
            player2.getHand().drawCards(player2.getDeck(), STARTING_CARDS_DEF);
        } else {
            player1.getHand().drawCards(player1.getDeck(), STARTING_CARDS_DEF);
            player2.getHand().drawCards(player2.getDeck(), STARTING_CARDS_ATT);
        }
    }

}
