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
public class Automaton extends Bot { //TODO check supports + kill on escape

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    @Override
    public void turn() {
        setPickingAbility(false);
        PossibilityInfo bestPossibility = getBestPossibility();
        if (bestPossibility != null) {
            report("Playing a card: "+bestPossibility.getCard().getCardInfo().getName());
            if (((isDeploymentMenu(bestPossibility.getMenu()) || bestPossibility.getCard().getCardInfo().getCardType() == CardType.MS) && useAbility(bestPossibility.getCard(), bestPossibility.getMenu())) || deploy(bestPossibility.getCard(), bestPossibility.getMenu(), getBestPosition(bestPossibility.getCard(), getBattle().getRoundManager().getPossibilityAdvisor().getTargetMenu(bestPossibility.getCard(), this)))) {
                if (!getBattle().getRoundManager().isTargetingActive() && !isPickingAbility()) {
                    turnContinue();
                }
            } else if (!isPickingAbility()) {
                report("turn() failed!");
                cancelTurn();
            } else {
                turnContinue();
            }
        } else {
            report("No possibilities.");
            delayAction(getBattle().getRoundManager()::endTurn);
        }
    }

    public void turnContinue() {
        delayAction(this::turn);
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
        if (ability != null && ability.getTargets() != null) {
            switch (ability.getTargets()) {
                case ANY_ALLY:
                case ALLIED_FLEET:
                    getAlliedTarget(token, ability);
                    break;
                case ANY_ENEMY:
                case ENEMY_FLEET:
                    getEnemyTarget(token, ability);
                    break;
            }
        } else {
            report("chooseTargets() failed!");
            cancelTurn();
        }
    }

    public void getAlliedTarget(Token token, AbilityInfo ability) {
        if (getFleet().isEmpty()) {
            getBattle().getRoundManager().processTarget(getMs().getToken());
        } else if (getFleet().getShips().length == 1) {
            getBattle().getRoundManager().processTarget(getFleet().getShips()[3].getToken());
        } else {
            Ship strongestShip = null;
            for (Ship ship : getFleet().getShips()) {
                if (strongestShip == null || (ship.getCardInfo().getEnergy()+ship.getCardInfo().getMatter()*2-ship.getDamage()) > (strongestShip.getCardInfo().getEnergy()+strongestShip.getCardInfo().getMatter()*2-strongestShip.getDamage())) {
                    strongestShip = ship;
                }
            }
            if (strongestShip != null) {
                getBattle().getRoundManager().processTarget(strongestShip.getToken());
            } else {
                report("getAlliedTarget() failed!");
                cancelTurn();
            }
        }
    }

    public void getEnemyTarget(Token token, AbilityInfo ability) {
        Player[] enemies = getBattle().getEnemies(this);
        if (enemies.length > 0) { //atm works only in 1V1
            Player enemy = enemies[0];
            if (enemy.getFleet().isEmpty()) {
                getBattle().getRoundManager().processTarget(enemy.getMs().getToken());
            } else if (enemy.getFleet().getShips().length == 1) {
                getBattle().getRoundManager().processTarget(enemy.getFleet().getShips()[3].getToken());
            } else {
                Ship weakestShip = null;
                for (Ship ship : enemy.getFleet().getShips()) {
                    if (weakestShip == null || (ship.getCardInfo().getEnergy() + ship.getCardInfo().getMatter() * 2 - ship.getDamage()) < (weakestShip.getCardInfo().getEnergy() + weakestShip.getCardInfo().getMatter() * 2 - weakestShip.getDamage())) {
                        weakestShip = ship;
                    }
                }
                if (weakestShip != null) {
                    getBattle().getRoundManager().processTarget(weakestShip.getToken());
                } else {
                    report("getAlliedTarget() failed!");
                    cancelTurn();
                }
            }
        } else {
            report("getAlliedTarget() (enemies.length) failed!");
            cancelTurn();
        }
    }

    @Override
    public void pickAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        if (options.size() > 0) {
            if (caster.getCard().getCardInfo().getId() == BONUS_CARD_ID) {
                getBattle().getRoundManager().processPick(options.get(1)); //atm always picks the matter
            } else {
                getBattle().getRoundManager().processPick(options.get(0)); //atm always picks the first one
            }
            setPickingAbility(true);
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
