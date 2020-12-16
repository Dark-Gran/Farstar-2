package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.players.cards.Mothership;

/**
 *  "Just Play Something":
 *  -- No sensors beyond PossibilityAdvisor
 *  -- No planning
 */
public class Automaton extends Bot { //TODO

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    @Override
    public void turn() {
        deploy(getYard().get(0), getYard().getCardListMenu());
        delayAction(getBattle().getRoundManager()::endTurn);
    }

    @Override
    public void newCombat() {
        getBattle().getCombatManager().endCombat();
    }

}
