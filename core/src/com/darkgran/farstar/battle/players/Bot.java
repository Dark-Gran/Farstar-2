package com.darkgran.farstar.battle.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Mothership;

public abstract class Bot extends Player implements BotSettings {
    private Battle battle;
    private final BotTier botTier;
    private final float timerDelay;

    public Bot(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard);
        this.botTier = botTier;
        this.timerDelay = getTimerDelay(botTier);
        report("Hello Universe!");
    }

    public void newTurn() {
        report("My Turn Began!");
        delayAction(this::turn);
    }

    public void turn() { }

    public void deploy(Card card, CardListMenu cardListMenu) { }

    public void newCombat() { }

    public void newDuelOK() { }

    public void delayAction(Runnable runnable) {
        Timer.schedule(new Timer.Task(){
            public void run () { Gdx.app.postRunnable(runnable); }
        }, timerDelay);
    }

    public void report(String message) {
        System.out.println(botTier+": "+message);
    }

    public void setBattle(Battle battle) { this.battle = battle; }

    public Battle getBattle() { return battle; }

    public BotTier getBotTier() { return botTier; }

}
