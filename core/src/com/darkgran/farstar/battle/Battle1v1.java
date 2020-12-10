package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;

import java.util.concurrent.ThreadLocalRandom;

public class Battle1v1 extends Battle {
    private final Player player1;
    private final Player player2;

    public Battle1v1() {
        super();
        //in future: pass the Players in parameters
        player1 = new Player((byte) 1, STARTING_ENERGY, STARTING_MATTER, new Mothership(0), new Deck(4), new Yard(1));
        player2 = new Player((byte) 2, STARTING_ENERGY, STARTING_MATTER, new Mothership(0), new Deck(4), new Yard(1));
        player1.getMs().setPlayer(player1);
        player1.getDeck().setPlayer(player1);
        player2.getMs().setPlayer(player2);
        player2.getDeck().setPlayer(player2);
    }

    @Override
    public BattleStage createBattleStage(Farstar game, Viewport viewport, BattleScreen battleScreen) {
        return new BattleStage1V1(game, viewport, battleScreen, new DuelMenu1v1(getCombatManager().getDuelManager()), player1, player2);
    }

    @Override
    public DuelManager createDuelManager() {
        return new DuelManager1v1();
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
    public void closeYards() {
        ((YardMenu) (player1.getShipyard().getTokenMenu())).setVisible(false);
        ((YardMenu) (player2.getShipyard().getTokenMenu())).setVisible(false);
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

    @Override
    public void setUsedForAllFleets(boolean used) {
        setUsedForFleet(player1, used);
        setUsedForFleet(player2, used);
    }

    @Override
    public void addGameOver(Player player) {
        getGameOvers().add(player);
        battleEnd();
    }

    @Override
    public void battleEnd() {
        super.battleEnd();
        System.out.println("Player #"+getWinnerID()+" wins!");
    }

    private int getWinnerID() {
        if (getGameOvers().contains(player1)) {
            return player2.getBattleID();
        } else {
            return player1.getBattleID();
        }
    }

    @Override
    public void tickEffects() {
        super.tickEffects();
        player1.getMs().checkEffects(getAbilityManager());
        player1.getFleet().checkEffectsOnAll(getAbilityManager());
        player2.getMs().checkEffects(getAbilityManager());
        player2.getFleet().checkEffectsOnAll(getAbilityManager());
    }

}
