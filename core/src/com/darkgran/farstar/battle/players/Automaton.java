package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.players.cards.Mothership;

import java.util.ArrayList;

/**
 *  "Just Play Something":
 *  -- No sensors beyond PossibilityAdvisor
 *  -- No planning
 */
public class Automaton extends Bot {

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    @Override
    public void turn() {
        PossibilityInfo bestPossibility = getBestPossibility();
        if (bestPossibility != null) {
            report("Playing card.");
            if (deploy(bestPossibility.getCard(), bestPossibility.getMenu())) {
                delayAction(this::turn); //= repeat until no possibilities
            } else {
                report("Move failed!");
                delayAction(getBattle().getRoundManager()::endTurn);
            }
        } else {
            report("No possibilities.");
            delayAction(getBattle().getRoundManager()::endTurn);
        }
    }

    public PossibilityInfo getBestPossibility() {
        ArrayList<PossibilityInfo> possibilities = getBattle().getRoundManager().getPossibilityAdvisor().getPossibilities(this, getBattle());
        if (possibilities.size() > 0) { //TODO

            





        }
        return null;
    }

    @Override
    public void newCombat() {
        getBattle().getCombatManager().endCombat();
    }

}
