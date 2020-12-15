package com.darkgran.farstar.battle.players.ai;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.Deck;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.Yard;
import com.darkgran.farstar.battle.players.cards.Mothership;

public class Automaton extends Player { //TODO
    private final BotTier botTier;
    private Battle battle;

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard);
        this.botTier = botTier;
        report("Hello World!");
    }

    public void newTurn() {
        report("My Turn Began!");
        report("No idea what to do yet!");
        battle.getRoundManager().endTurn();
    }

    public void newCombat() {
        battle.getCombatManager().endCombat();
    }

    private void report(String message) { System.out.println(botTier+": "+message); }

    public void setBattle(Battle battle) { this.battle = battle; }

}
