package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.cards.CardLibrary;
import com.darkgran.farstar.battle.gui.GUI;

public abstract class Battle implements BattleSettings {
    public final static CardLibrary CARD_LIBRARY = new CardLibrary();
    private GUI gui;
    private Player whoseTurn;
    private RoundManager roundManager;

    public Battle() {
        System.out.println("Launching Battle...");
        loadLibrary();
    }

    public void loadLibrary() { CARD_LIBRARY.loadLocal("content/cards.json"); }

    public void launchBattle(RoundManager roundManager) {
        System.out.println("Battle Begins.");
        coinToss();
        startingCards();
        this.roundManager = roundManager;
        roundManager.newRound();
    }

    public void coinToss() { } //must setWhoseTurn

    public void startingCards() { }

    public void setGUI(GUI gui) { this.gui = gui; }

    public GUI getGUI() { return gui; }

    public Player getWhoseTurn() { return whoseTurn; }

    public void setWhoseTurn(Player whoseTurn) { this.whoseTurn = whoseTurn; }

}
