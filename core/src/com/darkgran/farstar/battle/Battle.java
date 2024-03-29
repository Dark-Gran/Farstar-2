package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Battle {
    private BattleScreen battleScreen;
    private final BattleType battleType;
    private BattlePlayer whoseTurn;
    private RoundManager roundManager;
    private CombatManager combatManager;
    private AbilityManager abilityManager;
    private boolean everythingDisabled = false;
    private final ArrayList<BattlePlayer> gameOvers = new ArrayList<>();

    public Battle(@NotNull BattleType battleType) {
        System.out.println("Launching Battle...");
        this.battleType = battleType;
    }

    public BattleStage createBattleStage(@NotNull Farstar game, @NotNull Viewport viewport, @NotNull BattleScreen battleScreen) {
        return null;
    }

    public CombatManager createCombatManager() {
        return null;
    }

    public boolean areYardsOpen() {
        return false;
    }

    public void closeYards() { }

    public void dispose() {
        BattleSettings.getInstance().dispose();
    }

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

    public boolean addGameOver(BattlePlayer battlePlayer) {
        if (!gameOvers.contains(battlePlayer)) {
            gameOvers.add(battlePlayer);
            return true;
        }
        return false;
    }

    public BattlePlayer getWinner() { return null; }

    public void battleEnd() {
        System.out.println("GAME OVER");
        setEverythingDisabled(true);
    }

    public void tacticTrigger() {
        getWhoseTurn().getMs().tacticTrigger(abilityManager);
        getWhoseTurn().getFleet().tacticTriggerOnAll(abilityManager);
        getWhoseTurn().getSupports().tacticTriggerOnAll(abilityManager);
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
        getRoundManager().getPossibilityAdvisor().unMarkAll(getWhoseTurn(), this);
    }

    public void refreshPossibilities() {
        getRoundManager().getPossibilityAdvisor().refresh(getWhoseTurn(), this);
    }

    public boolean aintCombatOrDuel() {
        return !combatManager.isActive() && !combatManager.getDuelManager().isActive();
    }

    public BattlePlayer getWhoseTurn() {
        return getCombatManager().isTacticalPhase() ? getCombatManager().getActivePlayer().getBattlePlayer() : whoseTurn;
    }

    public BattlePlayer[] getEnemies(BattlePlayer battlePlayer) {
        return null;
    }

    public BattlePlayer[] getAllies(BattlePlayer battlePlayer) { return null; }

    public void setWhoseTurn(BattlePlayer whoseTurn) { this.whoseTurn = whoseTurn; }

    public void passTurn() { } //setWhoseTurn to next player

    public RoundManager getRoundManager() { return roundManager; }

    public CombatManager getCombatManager() { return combatManager; }

    public AbilityManager getAbilityManager() { return abilityManager; }

    public boolean isEverythingDisabled() { return everythingDisabled; }

    public void setEverythingDisabled(boolean everythingDisabled) { this.everythingDisabled = everythingDisabled; }

    public ArrayList<BattlePlayer> getGameOvers() { return gameOvers; }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }

    public BattleType getBattleType() {
        return battleType;
    }
}
