package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.*;
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
public class Automaton extends Bot {

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    //---------------//
    //-PLAYING-CARDS-//
    //---------------//

    @Override
    public void turn() {
        if (!isDisposed()) {
            super.turn();
            PossibilityInfo bestPossibility = getTurnPossibility();
            if (bestPossibility != null) {
                report("Playing a card: " + bestPossibility.getCard().getCardInfo().getName());
                boolean success;
                if (isDeploymentMenu(bestPossibility.getMenu()) || bestPossibility.getCard().getCardInfo().getCardType() == CardType.MS) {
                    success = useAbility(bestPossibility.getCard(), bestPossibility.getMenu());
                } else {
                    success = deploy(bestPossibility.getCard(), bestPossibility.getMenu(), getBestPosition(bestPossibility.getCard(), bestPossibility.getMenu(), getBattle().getRoundManager().getPossibilityAdvisor().getTargetMenu(bestPossibility.getCard(), this)));
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

    @Override
    public DropTarget getDropTarget(CardType cardType) {
        if (cardType == CardType.SUPPORT) {
            return (SupportMenu) getSupports().getCardListMenu();
        } else {
            if (!CardType.isShip(cardType) && getFleet().isEmpty()) {
                return (DropTarget) getMs().getToken();
            } else {
                return getFleet().getFleetMenu();
            }
        }
    }

    public int getBestPosition(Card card, BaseMenu sourceMenu, BaseMenu targetMenu) {
        if (CardType.isShip(card.getCardInfo().getCardType())) {
            if (targetMenu.isEmpty()) {
                return 3;
            } else {
                return 2;
            }
        } else if (targetMenu instanceof FleetMenu && (card.getCardInfo().getCardType() == CardType.UPGRADE || card.isTactic())){
            FleetMenu fleetMenu = (FleetMenu) targetMenu;
            Token ally = getAlliedTarget(cardToToken(card, sourceMenu));
            for (int i = 0; i < fleetMenu.getShips().length; i++) {
                if (fleetMenu.getShips()[i] != null) {
                    if (fleetMenu.getShips()[i].getCard() == ally.getCard()) {
                        return i;
                    }
                }
            }
            return -1;
        } else {
            return 3;
        }
    }

    public PossibilityInfo getTurnPossibility() {
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

    public boolean isNonsense(PossibilityInfo possibilityInfo) { //TODO colors
        return (possibilityInfo.getCard().isTactic() && !getBattle().getCombatManager().isActive());
    }

    @Override
    //asked to pick Target for ability
    public void chooseTargets(Token token, AbilityInfo ability) {
        Token target = null;
        if (ability != null && ability.getTargets() != null) {
            setPickingTarget(true);
            switch (ability.getTargets()) {
                case ANY_ALLY:
                case ALLIED_FLEET:
                    target = getAlliedTarget(token);
                    break;
                case ANY: //expects that Upgrades cannot be used on enemies, ergo ANY must mean ANY_ENEMY (and it allows both only for the human player)
                case ANY_ENEMY:
                case ENEMY_FLEET:
                    target = getEnemyTarget(token, false);
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

    @Override
    public Token getAlliedTarget(Token caster) {
        if (getFleet().isEmpty()) {
            return getMs().getToken();
        } else {
            Ship strongestShip = null;
            for (Ship ship : getFleet().getShips()) {
                if (ship != null) {
                    if (strongestShip == null || isBiggerShip(ship, strongestShip)) {
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

    @Override
    public Token getEnemyTarget(Token attacker, boolean checkReach) {
        Player[] enemies = getBattle().getEnemies(this);
        Token picked = null;
        Ship weakestShip = null;
        for (Player enemy : enemies) {
            if (picked == null && enemy.getFleet().isEmpty()) {
                picked = enemy.getMs().getToken();
            } else {
                for (Ship ship : enemy.getFleet().getShips()) {
                    if (ship != null) {
                        if ((weakestShip == null || isBiggerShip(weakestShip, ship)) && (!checkReach || getBattle().getCombatManager().canReach(attacker, ship.getToken(), this.getFleet()))) {
                            weakestShip = ship;
                            picked = ship.getToken();
                        }
                    }
                }
            }
        }
        return picked;
    }

    private boolean isBiggerShip(Ship A, Ship B) { //return A>B (in-future: consider abilities)
        return A.getCardInfo().getOffense() + A.getCardInfo().getDefense() - A.getDamage() > B.getCardInfo().getOffense() + B.getCardInfo().getDefense() - B.getDamage();
    }

    @Override
    //asked to pick an ability
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

    //--------//
    //-COMBAT-//
    //--------//

    @Override
    public void combat() {
        super.combat();
        if (getBattle().getCombatManager().isActive()) {
            for (Ship ship : getFleet().getShips()) {
                if (ship != null && !ship.haveFought()) {
                    delayedLaunchDuel(ship);
                    break;
                }
            }
        }
    }

    @Override
    public void duel(DuelOK duelOK) { //atm expects all Tactics to be meant for allies
        super.duel(duelOK);
        boolean success = false;
        PossibilityInfo bestPossibility = getDuelPossibility();
        if (bestPossibility != null) {
            report("Playing a tactic: " + bestPossibility.getCard().getCardInfo().getName());
            int position = -1;
            for (int i = 0; i < getFleet().getFleetMenu().getShips().length; i++) {
                if (getFleet().getFleetMenu().getShips()[i] != null) {
                    if (getFleet().getFleetMenu().getShips()[i].getCard().isInDuel()) {
                        position = i;
                        break;
                    }
                }
            }
            if (deploy(bestPossibility.getCard(), bestPossibility.getMenu(), position)) {
                success = true;
                delayedDuel(duelOK);
            } else {
                report("Failed to deploy the tactic!");
                success = false;
            }
        } else {
            report("No duel possibilities.");
        }
        if (!success) { duelReady(duelOK); }
    }

    public PossibilityInfo getDuelPossibility() {
        ArrayList<PossibilityInfo> possibilities = getBattle().getRoundManager().getPossibilityAdvisor().getPossibilities(this, getBattle());
        if (possibilities.size() > 0) {
            //1. Play the first playable thing that's not "nonsense"
            for (PossibilityInfo possibilityInfo : possibilities) {
                if (!isNonsense(possibilityInfo)) {
                    return possibilityInfo;
                }
            }
        }
        return null;
    }

    //------//
    //-MISC-//
    //------//

    @Override
    public void gameOver(int winnerID) {
        if (winnerID == getBattleID()) {
            report("Wow, I've won! How did I do that?");
        }
        super.gameOver(winnerID);
    }

}
