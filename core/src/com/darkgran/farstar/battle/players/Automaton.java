package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.*;
import com.darkgran.farstar.battle.players.cards.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.darkgran.farstar.battle.BattleSettings.BONUS_CARD_ID;

/**
 *  "Just Play Something":
 *  -- No sensors beyond PossibilityAdvisor
 *  -- No planning (atm not even the frame for it - both turn and combat work with "what comes first")
 *  -- Possibility "nonsense"-filter to substitute planning (ie. filters out some "typical bad moves")
 */
public class Automaton extends Bot {

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    //---------------//
    //-PLAYING-CARDS-//
    //---------------//

    @Override
    protected boolean turn(boolean combat, CombatOK combatOK) { //true = nothing to do (used in combat, see tactical())
        if (!isDisposed() && !getBattle().isEverythingDisabled()) {
            super.turn(combat, combatOK);
            PossibilityInfo bestPossibility = combat ? getTacticalPossibility() : getTurnPossibility();
            if (bestPossibility != null) {
                if (!combat) {
                    return playCardInDeployment(bestPossibility);
                } else {
                    return playCardInTactical(bestPossibility, combatOK);
                }
            } else {
                report("No possibilities.");
                if (!combat) { delayedEndTurn(); }
                else { combatReady(combatOK); }
            }
        }
        return true;
    }

    private boolean playCardInDeployment(PossibilityInfo possibilityInfo) {
        report("Playing a card: " + possibilityInfo.getCard().getCardInfo().getName());
        boolean success;
        if (isDeploymentMenu(possibilityInfo.getMenu()) || possibilityInfo.getCard().getCardInfo().getCardType() == CardType.MS) {
            success = useAbility(possibilityInfo.getCard(), possibilityInfo.getMenu());
        } else {
            success = deploy(possibilityInfo.getCard(), possibilityInfo.getMenu(), getBestPosition(possibilityInfo.getCard(), possibilityInfo.getMenu(), getBattle().getRoundManager().getPossibilityAdvisor().getTargetMenu(possibilityInfo.getCard(), this)));
        }
        if (!success && !isPickingAbility() && !isPickingTarget()) {
            report("error: playCardInDeployment() failed!");
            cancelTurn();
            return true;
        } else {
            delayedTurn(false, null);
            return false;
        }
    }

    private boolean playCardInTactical(PossibilityInfo possibilityInfo, CombatOK combatOK) {
        report("Playing a tactic: " + possibilityInfo.getCard().getCardInfo().getName());
        boolean success;
        //in-future consider outcomes of all duels instead of the classic position-pick
        int position = getBestPosition(possibilityInfo.getCard(), possibilityInfo.getMenu(), getBattle().getRoundManager().getPossibilityAdvisor().getTargetMenu(possibilityInfo.getCard(), this));
        success = deploy(possibilityInfo.getCard(), possibilityInfo.getMenu(), position);
        if (!success && !isPickingAbility() && !isPickingTarget()) {
            report("error: playCardInTactical() failed!");
            cancelTactical(combatOK);
            return true;
        } else {
            delayedTactical(combatOK);
            return false;
        }
    }

    @Override
    protected DropTarget getDropTarget(CardType cardType) {
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

    private int getBestPosition(Card card, Menu sourceMenu, Menu targetMenu) {
        if (CardType.isShip(card.getCardInfo().getCardType())) {
            if (targetMenu.isEmpty()) {
                return 3;
            } else {
                return 2;
            }
        } else if (targetMenu instanceof FleetMenu && CardType.isSpell(card.getCardInfo().getCardType())){
            FleetMenu fleetMenu = (FleetMenu) targetMenu;
            Token ally = getAlliedTarget(cardToToken(card, sourceMenu), null);
            for (int i = 0; i < fleetMenu.getFleetTokens().length; i++) {
                if (fleetMenu.getFleetTokens()[i] != null) {
                    if (fleetMenu.getFleetTokens()[i].getCard() == ally.getCard()) {
                        return i;
                    }
                }
            }
            return -1;
        } else {
            return 3;
        }
    }

    private PossibilityInfo getTurnPossibility() {
        ArrayList<PossibilityInfo> possibilities = getBattle().getRoundManager().getPossibilityAdvisor().getPossibilities(this, getBattle());
        if (possibilities.size() > 0) {
            //1. Have at least one defender
            if (getFleet().isEmpty()) {
                PossibilityInfo ship = null;
                for (PossibilityInfo possibilityInfo : possibilities) {
                    if (CardType.isShip(possibilityInfo.getCard().getCardInfo().getCardType())) {
                        if (ship == null) { ship = possibilityInfo; }
                        if (AbilityManager.hasAttribute(possibilityInfo.getCard(), EffectType.GUARD)) { //Guard Preference
                            return possibilityInfo;
                        }
                    }
                }
                if (ship != null) {
                    return ship;
                }
            }
            //2. Play the first playable thing that's not "nonsense"
            for (PossibilityInfo possibilityInfo : possibilities) {
                if (aintNonsense(possibilityInfo)) {
                    return possibilityInfo;
                }
            }
        }
        return null;
    }

    private boolean aintNonsense(PossibilityInfo possibilityInfo) {
        return (possibilityInfo.getCard().isTactic() == getBattle().getCombatManager().isTacticalPhase()) && !abilityNonsense(possibilityInfo.getCard());
    }

    private boolean abilityNonsense(Card card) {
        for (AbilityInfo ability : card.getCardInfo().getAbilities()) {
            for (Effect effect : ability.getEffects()) {
                if (effect != null && effect.getEffectType() != null) {
                    switch (effect.getEffectType()) {
                        case CHANGE_STAT:
                            if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 2 && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
                                Object changeInfo = effect.getEffectInfo().get(1);
                                EffectTypeSpecifics.ChangeStatType changeStatType = EffectTypeSpecifics.ChangeStatType.valueOf(effect.getEffectInfo().get(0).toString());
                                Token allyToken;
                                Card ally;
                                Card enemy;
                                //Validate change of Type (color)
                                if (changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE || changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE) {
                                    if (getBattle().getCombatManager().isTacticalPhase()) { //COMBAT ONLY
                                        allyToken = getAlliedTarget(card.getToken(), null);
                                        Map.Entry<Token, DuelManager.AttackInfo> duel = getBattle().getCombatManager().getDuel(allyToken);
                                        if (duel == null) { return true; }
                                        ally = allyToken.getCard();
                                        enemy = getBattle().getCombatManager().getDuelOpponent(allyToken).getCard();
                                        if (techTypeNonsense(ally, enemy, changeStatType, changeInfo)) {
                                            return true;
                                        }
                                        if (ability.isPurelyTypeChange()) {
                                            if ((changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && (ally.getCardInfo().getOffense() <= 1 || enemy.getCardInfo().getDefense() < ally.getCardInfo().getOffense())) || (changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && (enemy.getCardInfo().getOffense() <= 1 || ally.getCardInfo().getDefense() < enemy.getCardInfo().getOffense()))) {
                                                return true;
                                            } else {
                                                TechType techType = TechType.valueOf(changeInfo.toString()); //in-future: rework invalidating "switches" (changing both types at the same time)
                                                return ((changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && techType == ally.getCardInfo().getDefenseType() || changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && techType == ally.getCardInfo().getOffenseType()));
                                            }
                                        }
                                    }
                                //Validate Ability change
                                } else if (changeStatType == EffectTypeSpecifics.ChangeStatType.ABILITY){
                                    EffectType effectType = EffectType.valueOf(changeInfo.toString());
                                    switch (effectType) {
                                        case FIRST_STRIKE:
                                            if (getBattle().getCombatManager().isTacticalPhase()) { //COMBAT ONLY
                                                allyToken = getAlliedTarget(card.getToken(), EffectType.FIRST_STRIKE);
                                                Map.Entry<Token, DuelManager.AttackInfo> duel = getBattle().getCombatManager().getDuel(allyToken);
                                                if (duel == null) { return true; }
                                                ally = allyToken.getCard();
                                                enemy = getBattle().getCombatManager().getDuelOpponent(allyToken).getCard();
                                                if (enemy.isMS() || (duel.getValue().getUpperStrike() != null && duel.getValue().getUpperStrike() == ally)) {
                                                    return true;
                                                }
                                            } else { //OUTSIDE COMBAT
                                                for (Ship ship : getFleet().getShips()) {
                                                    if (ship != null && !AbilityManager.hasAttribute(ship, EffectType.FIRST_STRIKE)) {
                                                        return false;
                                                    }
                                                }
                                                return true;
                                            }
                                            break;
                                        }
                                    }
                                }
                            break;
                        case REPAIR:
                            return getWoundedAlly()==null;
                        case CHANGE_RESOURCE:
                            if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
                                //Object changeInfo = effect.getEffectInfo().get(1);
                                EffectTypeSpecifics.ChangeResourceType resource = EffectTypeSpecifics.ChangeResourceType.valueOf(effect.getEffectInfo().get(0).toString());
                                //int change = AbilityManager.floatObjectToInt(changeInfo);
                                switch (resource) {
                                    case ENERGY:
                                        return getEnergy() > getMatter();
                                    case MATTER:
                                        return getMatter()/2 > getEnergy();
                                }
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }

    private Token getWoundedAlly() {
        if (getMs().getDamage() > getMs().getCardInfo().getDefense()/2) { //PREFERS MS BELOW 50% HEALTH
            return getMs().getToken();
        }
        for (Ship ship : getFleet().getShips()) {
            if (ship != null && ship.getDamage() > 0) {
                return ship.getToken();
            }
        }
        if (getMs().getDamage() > 0) {
            return getMs().getToken();
        }
        return null;
    }

    private boolean techTypeNonsense(Card ally, Card enemy, EffectTypeSpecifics.ChangeStatType changeStatType, Object changeInfo) {
        return (changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && DuelManager.noneToInferior(ally.getCardInfo().getOffenseType()) != TechType.INFERIOR && ally.getCardInfo().getOffenseType() != enemy.getCardInfo().getDefenseType()) || (changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && ally.getCardInfo().getDefenseType() == enemy.getCardInfo().getOffenseType());
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
                    EffectType attribute = null;
                    if (AbilityManager.upgradesFirstStrike(token.getCard())) { //in-future: check for changing all abilities (no ability givers in game atm except the ones with Attribute)
                        attribute = EffectType.FIRST_STRIKE;
                    } else if (AbilityManager.hasAttribute(token.getCard(), EffectType.GUARD)) {
                        attribute = EffectType.GUARD;
                    }
                    if (AbilityManager.hasEffectType(token.getCard(), EffectType.REPAIR)) {
                        target = getWoundedAlly();
                    } else {
                        target = getAlliedTarget(token, attribute); //in-future: check against field of attributes instead of the first attribute (again, does not matter with "prototype cards")
                    }
                    break;
                case ANY: //expects that "Upgrades" cannot be used on enemies, ergo ANY must mean ANY_ENEMY (it's "ANY" only for the whims of human player)
                case ANY_ENEMY:
                case ENEMY_FLEET:
                    target = getEnemyTarget(token, false);
                    break;
            }
        }
        if (target != null) {
            getBattle().getRoundManager().processTarget(target);
        } else {
            report("error: chooseTargets() failed!");
            cancelTurn();
        }
    }

    @Override
    protected Token getAlliedTarget(Token caster, EffectType effectType) {
        if (getFleet().isEmpty() && !EffectType.isAttribute(effectType)) {
            if (!AbilityManager.hasEffectType(getMs(), effectType)) {
                return getMs().getToken();
            }
        } else {
            Ship strongestShip = null;
            for (Ship ship : getFleet().getShips()) {
                if (ship != null) {
                    if (strongestShip == null || isBiggerShip(ship, strongestShip)) {
                        if (strongestShip == null || !AbilityManager.hasEffectType(strongestShip, effectType)) {
                            strongestShip = ship;
                        }
                    }
                }
            }
            if (strongestShip != null) {
                return strongestShip.getToken();
            }
        }
        return null;
    }

    @Override
    protected Token getEnemyTarget(Token attacker, boolean checkReach) {
        Player[] enemies = getBattle().getEnemies(this);
        Token picked = null;
        Ship weakestShip = null;
        for (Player enemy : enemies) {
            if (picked == null && enemy.getFleet().isEmpty()) {
                picked = enemy.getMs().getToken();
            } else {
                for (Ship ship : enemy.getFleet().getShips()) {
                    if (ship != null) {
                        if ((weakestShip == null || isBiggerShip(weakestShip, ship)) && (!checkReach || getBattle().getCombatManager().canReach(attacker, ship.getToken(), enemy.getFleet()))) {
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
                getBattle().getRoundManager().processPick(options.get(1));
            } else { //atm there is only Labour Deck
                getBattle().getRoundManager().processPick(options.get(0));
            }
        } else {
            report("error: pickAbility() failed!");
            cancelTurn();
        }
    }

    //--------//
    //-COMBAT-//
    //--------//

    @Override
    protected void combat() { //duel-pick
        super.combat();
        HashMap<Token, DuelManager.AttackInfo> duels = new HashMap<>();
        Token enemy;
        for (Ship ship : getFleet().getShips()) {
            if (ship != null && !ship.isUsed()) {
                enemy = getEnemyTarget(ship.getToken(), true);
                if (enemy != null && getBattle().getCombatManager().canReach(ship.getToken(), enemy, enemy.getCard().getPlayer().getFleet())) {
                    duels.put(ship.getToken(), new DuelManager.AttackInfo(enemy));
                }
            }
        }
        if (duels.size() > 0) {
            getBattle().getCombatManager().setDuels(duels);
            getBattle().getCombatManager().startTacticalPhase();
        } else {
            report("No possible duels.");
            delayedCombatEnd();
        }
    }

    private PossibilityInfo getTacticalPossibility() {
        ArrayList<PossibilityInfo> possibilities = getBattle().getRoundManager().getPossibilityAdvisor().getPossibilities(this, getBattle());
        if (possibilities.size() > 0) {
            //1. Play the first playable thing that's not "nonsense"
            for (PossibilityInfo possibilityInfo : possibilities) {
                if (aintNonsense(possibilityInfo)) {
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
    public void gameOver(Player winner) {
        if (winner == this) {
            report("Wow, I've won! How did I do that?");
        }
        super.gameOver(winner);
    }

}
