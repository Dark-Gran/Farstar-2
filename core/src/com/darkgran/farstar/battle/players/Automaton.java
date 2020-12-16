package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.players.cards.CardType;
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
            report("Playing a card: "+bestPossibility.getCard().getCardInfo().getName());
            System.out.println(bestPossibility.getMenu());
            if (deploy(bestPossibility.getCard(), bestPossibility.getMenu(), 3)) {
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
            //1. Have at least one defender
            if (getFleet().isEmpty()) {
                for (PossibilityInfo possibilityInfo : possibilities) {
                    if (CardType.isShip(possibilityInfo.getCard().getCardInfo().getCardType())) {
                        return possibilityInfo;
                    }
                }
            }
            //2. Play the first playable thing that's not "nonsense"
            for (PossibilityInfo possibilityInfo : possibilities) {
                if (!isNonsense(possibilityInfo)) {
                    return possibilityInfo;
                }
            }
        }
        return null;
    }

    private boolean isNonsense(PossibilityInfo possibilityInfo) {
        return false;
    }

    @Override
    public void newCombat() {
        getBattle().getCombatManager().endCombat();
    }

}
