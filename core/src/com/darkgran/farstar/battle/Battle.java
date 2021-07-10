package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.players.cards.Ship;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Battle {
    private BattleScreen battleScreen;
    private Player whoseTurn;
    private RoundManager roundManager;
    private CombatManager combatManager;
    private AbilityManager abilityManager;
    private boolean everythingDisabled = false;
    private ArrayList<Player> gameOvers = new ArrayList<Player>();

    public Battle() {
        System.out.println("Launching Battle...");
    }

    public BattleStage createBattleStage(@NotNull Farstar game, @NotNull Viewport viewport, @NotNull BattleScreen battleScreen) {
        return null;
    }

    public DuelManager createDuelManager() {
        return null;
    }

    protected void closeYards() { }

    public void dispose() {}

    public void startingSetup(@NotNull BattleScreen battleScreen, @NotNull RoundManager roundManager, @NotNull CombatManager combatManager, @NotNull AbilityManager abilityManager) {
        this.battleScreen = battleScreen;
        coinToss();
        roundManager.setStartingPlayer(whoseTurn);
        this.roundManager = roundManager;
        this.combatManager = combatManager;
        this.abilityManager = abilityManager;
    }

    protected void coinToss() { } //must setWhoseTurn

    protected void startingCards() { }

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

    public void tickEffects() {
        whoseTurn.getMs().tickEffects(abilityManager);
        whoseTurn.getFleet().tickEffectsOnAll(abilityManager);
        whoseTurn.getSupports().tickEffectsOnAll(abilityManager);
    }

    public void refreshUsedShips() { //for AbilityStarter.USE
        whoseTurn.getMs().setUsed(false);
        whoseTurn.getFleet().setUsedOnAll(false);
        whoseTurn.getSupports().setUsedOnAll(false);
    }

    public void unMarkAllPossibilities() {
        getRoundManager().getPossibilityAdvisor().unMarkAll(whoseTurn, this);
    }

    public void refreshPossibilities() {
        getRoundManager().getPossibilityAdvisor().refresh(whoseTurn, this);
    }

    public boolean activeCombatOrDuel() {
        return combatManager.isActive() || combatManager.getDuelManager().isActive();
    }

    public Player[] getEnemies(Player player) {
        return null;
    }

    public Player getWhoseTurn() { return whoseTurn; }

    public void setWhoseTurn(Player whoseTurn) { this.whoseTurn = whoseTurn; }

    public void passTurn() { } //setWhoseTurn to next player

    public RoundManager getRoundManager() { return roundManager; }

    public CombatManager getCombatManager() { return combatManager; }

    public AbilityManager getAbilityManager() { return abilityManager; }

    public boolean isEverythingDisabled() { return everythingDisabled; }

    public void setEverythingDisabled(boolean everythingDisabled) { this.everythingDisabled = everythingDisabled; }

    public ArrayList<Player> getGameOvers() { return gameOvers; }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }
}
