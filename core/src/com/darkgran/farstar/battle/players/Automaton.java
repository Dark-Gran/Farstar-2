package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.BaseMenu;
import com.darkgran.farstar.battle.gui.DropTarget;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Mothership;
import com.darkgran.farstar.battle.players.cards.Ship;

import java.util.ArrayList;

import static com.darkgran.farstar.battle.BattleSettings.BONUS_CARD_ID;

/**
 *  "Just Play Something":
 *  -- No sensors beyond PossibilityAdvisor
 *  -- No planning (atm not even the frame for it)
 */
public class Automaton extends Bot { //TODO combat + duel

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    @Override
    public void turn() {
        if (!isDisposed()) {
            super.turn();
            PossibilityInfo bestPossibility = getBestPossibility();
            if (bestPossibility != null) {
                report("Playing a card: " + bestPossibility.getCard().getCardInfo().getName());
                boolean success;
                if (isDeploymentMenu(bestPossibility.getMenu()) || bestPossibility.getCard().getCardInfo().getCardType() == CardType.MS) {
                    success = useAbility(bestPossibility.getCard(), bestPossibility.getMenu());
                } else {
                    success = deploy(bestPossibility.getCard(), bestPossibility.getMenu(), getBestPosition(bestPossibility.getCard(), getBattle().getRoundManager().getPossibilityAdvisor().getTargetMenu(bestPossibility.getCard(), this)));
                }
                if (!success && !isPickingAbility() && !isPickingTarget()) {
                    report("turn() failed!");
                    cancelTurn();
                } else {
                    delayedTurn();
                }
            } else {
                report("No possibilities.");
                delayedEndTurn();
            }
        }
    }

    public int getBestPosition(Card card, BaseMenu targetMenu) {
        if (CardType.isShip(card.getCardInfo().getCardType())) {
            if (targetMenu.isEmpty()) {
                return 3;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }

    public PossibilityInfo getBestPossibility() {
        ArrayList<PossibilityInfo> possibilities = getBattle().getRoundManager().getPossibilityAdvisor().getPossibilities(this, getBattle());
        if (possibilities.size() > 0) {
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

    public boolean isNonsense(PossibilityInfo possibilityInfo) {
        return false; //in-future: TechType checks etc.
    }

    @Override
    public void chooseTargets(Token token, AbilityInfo ability) {
        Token target = null;
        if (ability != null && ability.getTargets() != null) {
            setPickingTarget(true);
            switch (ability.getTargets()) {
                case ANY_ALLY:
                case ALLIED_FLEET:
                    target = getAlliedTarget(token, ability);
                    break;
                case ANY: //expects that Upgrades cannot be used on enemies, ergo ANY must mean ANY_ENEMY (and it allows both only for the human player)
                case ANY_ENEMY:
                case ENEMY_FLEET:
                    target = getEnemyTarget(token, ability);
                    break;
            }
        }
        if (target != null) {
            getBattle().getRoundManager().processTarget(target);
        } else {
            report("chooseTargets() failed!");
            cancelTurn();
        }
    }

    public Token getAlliedTarget(Token token, AbilityInfo ability) {
        if (getFleet().isEmpty()) {
            return getMs().getToken();
        } else if (getFleet().getShips().length == 1) {
            return getFleet().getShips()[3].getToken();
        } else {
            Ship strongestShip = null;
            for (Ship ship : getFleet().getShips()) {
                if (ship != null) {
                    if (strongestShip == null || (ship.getCardInfo().getEnergy() + ship.getCardInfo().getMatter() * 2 - ship.getDamage()) > (strongestShip.getCardInfo().getEnergy() + strongestShip.getCardInfo().getMatter() * 2 - strongestShip.getDamage())) {
                        strongestShip = ship;
                    }
                }
            }
            if (strongestShip != null) {
                return strongestShip.getToken();
            } else {
                return null;
            }
        }
    }

    public Token getEnemyTarget(Token token, AbilityInfo ability) {
        Player[] enemies = getBattle().getEnemies(this);
        Token picked = null;
        Ship weakestShip = null;
        for (Player enemy : enemies) {
            if (picked == null && enemy.getFleet().isEmpty()) {
                picked = enemy.getMs().getToken();
            } else if (picked == null && enemy.getFleet().getShips().length == 1) {
                picked = enemy.getFleet().getShips()[3].getToken();
            } else {
                for (Ship ship : enemy.getFleet().getShips()) {
                    if (ship != null) {
                        if (weakestShip == null || (ship.getCardInfo().getEnergy() + ship.getCardInfo().getMatter() * 2 - ship.getDamage()) < (weakestShip.getCardInfo().getEnergy() + weakestShip.getCardInfo().getMatter() * 2 - weakestShip.getDamage())) {
                            weakestShip = ship;
                            picked = ship.getToken();
                        }
                    }
                }
            }
        }
        return picked;
    }

    @Override
    public void pickAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        if (options.size() > 0) {
            setPickingAbility(true);
            if (caster.getCard().getCardInfo().getId() == BONUS_CARD_ID) {
                getBattle().getRoundManager().processPick(options.get(1)); //atm always picks the matter
            } else {
                getBattle().getRoundManager().processPick(options.get(0)); //atm always picks the first one
            }
        } else {
            report("pickAbility() failed!");
            cancelTurn();
        }
    }

    @Override
    public void newCombat() {
        getBattle().getCombatManager().endCombat();
    }

}
