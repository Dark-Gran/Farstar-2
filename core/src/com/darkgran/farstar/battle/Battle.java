package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.cards.CardLibrary;
import com.darkgran.farstar.battle.gui.BattleMenu;
import com.darkgran.farstar.battle.gui.GUI;

public abstract class Battle implements BattleSettings {
    public final static CardLibrary CARD_LIBRARY = new CardLibrary();
    private Player whoseTurn;
    private RoundManager roundManager;

    public Battle() {
        System.out.println("Launching Battle...");
        loadLibrary();
    }

    public GUI createGUI(Farstar game, Viewport viewport) {
        return null;
    }

    public BattleMenu createBattleMenu(Farstar game, Viewport viewport) {
        return null;
    }

    public void loadLibrary() { CARD_LIBRARY.loadLocal("content/cards.json"); }

    public void launchBattle(RoundManager roundManager) {
        coinToss();
        startingCards();
        this.roundManager = roundManager;
        roundManager.newRound();
    }

    public void coinToss() { } //must setWhoseTurn

    public void startingCards() { }

    public Player getWhoseTurn() { return whoseTurn; }

    public void setWhoseTurn(Player whoseTurn) { this.whoseTurn = whoseTurn; }

    public void passTurn() { } //setWhoseTurn to next player

    public RoundManager getRoundManager() { return roundManager; }

}
