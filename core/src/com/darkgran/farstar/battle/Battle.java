package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.Ship;

import java.util.ArrayList;

public abstract class Battle implements BattleSettings {
    public final static CardLibrary CARD_LIBRARY = new CardLibrary();
    private Player whoseTurn;
    private RoundManager roundManager;
    private CombatManager combatManager;
    private boolean everythingDisabled = false;
    private ArrayList<Player> gameOvers = new ArrayList<Player>();

    public Battle() {
        System.out.println("Launching Battle...");
        loadLibrary();
    }

    public void loadLibrary() { CARD_LIBRARY.loadLocal("content/cards.json"); }

    public BattleStage createBattleStage(Farstar game, Viewport viewport, BattleScreen battleScreen) {
        return null;
    }

    public DuelManager createDuelManager() {
        return null;
    }

    public void closeYards() { }

    public void startingSetup(RoundManager roundManager, CombatManager combatManager) {
        coinToss();
        startingCards();
        this.roundManager = roundManager;
        this.combatManager = combatManager;
    }

    public void coinToss() { } //must setWhoseTurn

    public void startingCards() { }

    public void setUsedForAllFleets(boolean used) { }

    public void setUsedForFleet(Player player, boolean used) {
        for (Ship ship : player.getFleet().getShips()) { if (ship != null) { ship.setFought(used); } }
    }

    public void addGameOver(Player player) {
        gameOvers.add(player);
    }

    public void battleEnd() {
        System.out.println("GAME OVER");
        setEverythingDisabled(true);
    }

    public Player getWhoseTurn() { return whoseTurn; }

    public void setWhoseTurn(Player whoseTurn) { this.whoseTurn = whoseTurn; }

    public void passTurn() { } //setWhoseTurn to next player

    public RoundManager getRoundManager() { return roundManager; }

    public CombatManager getCombatManager() { return combatManager; }

    public boolean isEverythingDisabled() { return everythingDisabled; }

    public void setEverythingDisabled(boolean everythingDisabled) { this.everythingDisabled = everythingDisabled; }

    public ArrayList<Player> getGameOvers() { return gameOvers; }

    public void setGameOvers(ArrayList<Player> gameOvers) { this.gameOvers = gameOvers; }

}
