package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.*;
import com.darkgran.farstar.cards.*;
import com.darkgran.farstar.gui.battlegui.*;
import com.darkgran.farstar.gui.tokens.FleetToken;
import com.darkgran.farstar.gui.tokens.Token;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *  "Just Play Something":
 *  -- No sensors beyond PossibilityAdvisor
 *  -- No planning (atm not even a frame for it - both turn and combat work with "what comes first")
 *  -- Possibility "nonsense"-filter to substitute turn/tactical-planning (ie. filters out some "typical bad moves")
 *  -- In combat, attempts to pick unique targets for each ship and also not to attack with it (= risk damage) unless the attack is necessary to destroy the opposing ship.
 *
 *  There is only so much that can be achieved with "play anything, but not this".
 *  "Visualize all possible outcomes, pick the best one and play what leads to it" is required for better plays.
 *  However, Automaton should not be upgraded - instead a new class should extend Bot in the future.
 */
public final class Automaton extends Bot {

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    //---------------//
    //-PLAYING-CARDS-//
    //---------------//

    @Override
    protected void turn(boolean combat, CombatOK combatOK) {
        if (!isDisposed() && !getBattle().isEverythingDisabled()) {
            super.turn(combat, combatOK);
            PossibilityInfo bestPossibility = combat ? getTacticalPossibility() : getTurnPossibility();
            if (bestPossibility != null) {
                if (!combat) {
                    playCardInDeployment(bestPossibility);
                } else {
                    playCardInTactical(bestPossibility, combatOK);
                }
            } else {
                report("No possibilities.");
                if (!combat) { delayedEndTurn(); } //creates feeling of "thinking" after the last move - switch for endTurn() if needed
                else { combatReady(combatOK); }
            }
        }
    }

    private void playCardInDeployment(PossibilityInfo possibilityInfo) {
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
        } else {
            delayedTurn(false, null);
        }
    }

    private void playCardInTactical(PossibilityInfo possibilityInfo, CombatOK combatOK) {
        report("Playing a tactic: " + possibilityInfo.getCard().getCardInfo().getName());
        boolean success;
        //in-future consider outcomes of all duels instead of the classic position-pick
        int position = getBestPosition(possibilityInfo.getCard(), possibilityInfo.getMenu(), getBattle().getRoundManager().getPossibilityAdvisor().getTargetMenu(possibilityInfo.getCard(), this));
        success = deploy(possibilityInfo.getCard(), possibilityInfo.getMenu(), position);
        if (!success && !isPickingAbility() && !isPickingTarget()) {
            report("error: playCardInTactical() failed!");
            cancelTactical(combatOK);
        } else {
            delayedTactical(combatOK);
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

    private int getBestPosition(BattleCard battleCard, Menu sourceMenu, Menu targetMenu) {
        if (CardType.isShip(battleCard.getCardInfo().getCardType())) {
            if (targetMenu.isEmpty()) {
                return 3;
            } else {
                return 2;
            }
        } else if (targetMenu instanceof FleetMenu && CardType.isSpell(battleCard.getCardInfo().getCardType())){
            FleetMenu fleetMenu = (FleetMenu) targetMenu;
            Token ally = getAlliedTarget(cardToToken(battleCard, sourceMenu), null);
            if (ally != null) {
                for (int i = 0; i < fleetMenu.getFleetTokens().length; i++) {
                    if (fleetMenu.getFleetTokens()[i] != null) {
                        if (fleetMenu.getFleetTokens()[i].getCard() == ally.getCard()) {
                            return i;
                        }
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

    private boolean abilityNonsense(BattleCard battleCard) { //in-future: consider effect duration (temp vs perm) - atm not needed (all current tactic cards are temp, all current non-tactics are perm)
        boolean overallNonsense = false; //"consider all at once for TechSwitches" quick-fix
        boolean currentNonsense;
        byte effectIx = 0;
        for (AbilityInfo ability : battleCard.getCardInfo().getAbilities()) {
            for (Effect effect : ability.getEffects()) {
                if (effect != null && effect.getEffectType() != null) {
                    currentNonsense = false;
                    switch (effect.getEffectType()) {
                        case CHANGE_STAT:
                            if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 2 && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
                                Object changeInfo = effect.getEffectInfo().get(1);
                                EffectTypeSpecifics.ChangeStatType changeStatType = EffectTypeSpecifics.ChangeStatType.valueOf(effect.getEffectInfo().get(0).toString());
                                Token allyToken;
                                BattleCard ally;
                                ArrayList<Token> enemies;
                                BattleCard enemy;
                                BattleCard biggestEnemyShip = null;
                                //Validate change of Type (color)
                                if (changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE || changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE) {
                                    if (getBattle().getCombatManager().isTacticalPhase()) { //COMBAT ONLY
                                        allyToken = getAlliedTarget(battleCard.getToken(), null);
                                        Map.Entry<FleetToken, DuelManager.AttackInfo> duel = CombatManager.getDuel(allyToken, getBattle().getCombatManager().getDuels());
                                        if (duel == null) {
                                            return true;
                                        }
                                        ally = allyToken.getCard();
                                        enemies = CombatManager.getDuelOpponent(allyToken, getBattle().getCombatManager().getDuels());
                                        if (enemies == null || enemies.size() == 0) {
                                            return true;
                                        }
                                        //enemy = enemies.get(0).getCard();
                                        for (Token enemyToken : enemies) {
                                            enemy = enemyToken.getCard();
                                            if (biggestEnemyShip == null || isBiggerShip(enemy, biggestEnemyShip)) {
                                                if (biggestEnemyShip != null) { currentNonsense = false; }
                                                biggestEnemyShip = enemy;
                                                //"don't change what is correct"
                                                if ((changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && enemy.getHealth() >= ally.getCardInfo().getOffense()) || (changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && enemy.getCardInfo().getOffense() > 1 && enemy.getCardInfo().getOffense() <= ally.getHealth() && !enemy.isMS())) {
                                                    if (alreadyHasCorrectTech(ally, enemy, changeStatType)) {
                                                        overallNonsense = true;
                                                        currentNonsense = true;
                                                    }
                                                }
                                                if (ability.isPurelyTypeChange()) {
                                                    if ((effectIx == 0 || overallNonsense)) {
                                                        TechType techType = TechType.valueOf(changeInfo.toString());
                                                        //"don't change when it isn't an actual a change"
                                                        if ((changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && techType == ally.getCardInfo().getDefenseType()) || (changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && techType == ally.getCardInfo().getOffenseType())) {
                                                            overallNonsense = true;
                                                            //"don't change pointlessly"; in-future: consider "overall" properly (eg. don't allow changing type on stat 1 but allow it if the other stat gets changed as well (and it is the right move))
                                                        } else if (!currentNonsense) {
                                                            //atm because of how tech-switch-effects are ordered, correct Defense will enable/disable playing card that in fact (in)validates Offense (= bot sacrifices damage if it protects the ship)
                                                            overallNonsense = (changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && (ally.getCardInfo().getOffense() <= 1 || (BattleSettings.getInstance().OVERWHELMED_DEBUFF_ENABLED && enemy.getHealth() < ally.getCardInfo().getOffense()) || techType == enemy.getCardInfo().getDefenseType())) || (changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && (enemy.getCardInfo().getOffense() <= 1 || (BattleSettings.getInstance().OVERWHELMED_DEBUFF_ENABLED && ally.getHealth() < enemy.getCardInfo().getOffense()) || (techType != enemy.getCardInfo().getOffenseType() && !TechType.isInferior(enemy.getCardInfo().getOffenseType()) && !enemy.isMS())));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //Validate Ability change
                                } else if (changeStatType == EffectTypeSpecifics.ChangeStatType.ABILITY) {
                                    EffectType effectType = EffectType.valueOf(changeInfo.toString());
                                    switch (effectType) { //more cases will be added in future
                                        case FIRST_STRIKE:
                                            if (getBattle().getCombatManager().isTacticalPhase()) { //COMBAT ONLY
                                                allyToken = getAlliedTarget(battleCard.getToken(), EffectType.FIRST_STRIKE);
                                                Map.Entry<FleetToken, DuelManager.AttackInfo> duel = CombatManager.getDuel(allyToken, getBattle().getCombatManager().getDuels());
                                                if (duel == null) {
                                                    return true;
                                                }
                                                ally = allyToken.getCard();
                                                enemies = CombatManager.getDuelOpponent(allyToken, getBattle().getCombatManager().getDuels());
                                                if (enemies == null || enemies.size() == 0) {
                                                    return true;
                                                }
                                                overallNonsense = true;
                                                for (Token enemyToken : enemies) {
                                                    enemy = enemyToken.getCard();
                                                    if (biggestEnemyShip == null || isBiggerShip(enemy, biggestEnemyShip)) {
                                                        biggestEnemyShip = enemy;
                                                        if (!enemy.isMS() && (duel.getValue().getUpperStrike() == null || duel.getValue().getUpperStrike() != ally) && DuelManager.getDmgAgainstShields(ally.getCardInfo().getOffense(), enemy.getHealth(), AbilityManager.getArmor(enemy), ally.getCardInfo().getOffenseType(), enemy.getCardInfo().getDefenseType()) >= enemy.getHealth()) { //note: if OUTNUMBERED_DEBUFF becomes enabled, this condition must be updated
                                                            overallNonsense = false;
                                                        }
                                                    }
                                                }
                                                return overallNonsense;
                                            } else { //OUTSIDE COMBAT
                                                for (Ship ship : getFleet().getShips()) {
                                                    if (ship != null && !AbilityManager.hasAttribute(ship, EffectType.FIRST_STRIKE)) {
                                                        return false;
                                                    }
                                                }
                                                return true;
                                            }
                                    }
                                }
                            } else {
                                return true;
                            }
                            break;
                        case REPAIR:
                            Token w = getWoundedAlly();
                            if (w != null && effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 1 && effect.getEffectInfo().get(0) != null) {
                                return w.getCard().getDamage()>=AbilityManager.floatObjectToInt(effect.getEffectInfo().get(0));
                            }
                            return true;
                        case CHANGE_RESOURCE: //in-future: rework (see pickAbility())
                            if (battleCard.getCardInfo().getId() == BattleSettings.getInstance().BONUS_CARD_ID || battleCard.getCardInfo().getId() == 20) {
                                return false;
                            }
                            if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
                                //Object changeInfo = effect.getEffectInfo().get(1);
                                EffectTypeSpecifics.ChangeResourceType resource = EffectTypeSpecifics.ChangeResourceType.valueOf(effect.getEffectInfo().get(0).toString());
                                //int change = AbilityManager.floatObjectToInt(changeInfo);
                                switch (resource) {
                                    case ENERGY:
                                        return getEnergy() > getMatter() || getMatter() <= 1;
                                    case MATTER:
                                        return getMatter()/2 > getEnergy();
                                }
                            } else {
                                return true;
                            }
                            break;
                    }
                }
                effectIx++;
            }
        }
        return overallNonsense;
    }

    private boolean alreadyHasCorrectTech(BattleCard ally, BattleCard enemy, EffectTypeSpecifics.ChangeStatType changeStatType) { //stops changing type that is already "correct" (relative to the enemy)
        return (changeStatType == EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE && !TechType.isInferior(ally.getCardInfo().getOffenseType()) && ally.getCardInfo().getOffenseType() != enemy.getCardInfo().getDefenseType()) || (changeStatType == EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE && ally.getCardInfo().getDefenseType() == enemy.getCardInfo().getOffenseType());
    }

    private Token getWoundedAlly() {
        if (getMs().getDamage() >= getMs().getCardInfo().getDefense()/2) { //PREFERS MS WITH 50% OR LESS HEALTH
            return getMs().getToken();
        }
        for (Ship ship : getFleet().getShips()) {
            if (ship != null && ship.getDamage() > 0) {
                return ship.getToken();
            }
        }
        if (getMs().getDamage() > 0) { //in-future: don't waste resources to keep MS pristine unless it is the only possibility (= not using wastes Energy)
            return getMs().getToken();
        }
        return null;
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
                        target = getAlliedTarget(token, attribute); //in-future: check against field of attributes instead of the first attribute (again, does not matter with "prototype battleCards")
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
        if (getFleet().isEmpty() && !EffectType.isParaoffenseEffect(effectType)) {
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
        BattlePlayer[] enemies = getBattle().getEnemies(this);
        Token picked = null;
        Ship strongestKillable = null;
        float dmg;
        boolean desperate = false;
        while (picked == null) {
            for (BattlePlayer enemy : enemies) {
                if (picked == null && enemy.getFleet().isEmpty()) {
                    picked = enemy.getMs().getToken();
                } else {
                    for (Ship ship : enemy.getFleet().getShips()) {
                        if (ship != null) {
                            dmg = DuelManager.getDmgAgainstShields(attacker.getCard().getCardInfo().getOffense(), ship.getHealth(), AbilityManager.getArmor(ship), attacker.getCard().getCardInfo().getOffenseType(), ship.getCardInfo().getDefenseType());
                            if ((strongestKillable == null || (isBiggerShip(ship, strongestKillable)) && dmg >= ship.getHealth()) && (!checkReach || getBattle().getCombatManager().canReach(attacker, ship.getToken(), enemy.getFleet()))) {
                                strongestKillable = ship;
                                picked = ship.getToken();
                            }
                        }
                    }
                    if (picked == null && desperate) {
                        picked = enemy.getMs().getToken();
                    }
                }
            }
            desperate = true;
        }
        return picked;
    }

    private boolean isBiggerShip(BattleCard A, BattleCard B) { //return A>B (in-future: consider abilities etc)
        return A.getCardInfo().getOffense() + A.getHealth() > B.getCardInfo().getOffense() + B.getHealth();
    }

    @Override
    //asked to pick an ability
    public void pickAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        if (options.size() > 0) {
            setPickingAbility(true);
            //in-future: consider eco properly
            //atm there are only SpareResources and LabourDeck, so the decision is always Energy vs Matter
            //if (caster.getCard().getCardInfo().getId() == BONUS_CARD_ID) {}
            if (getEnergy() < getMatter()-1) {
                getBattle().getRoundManager().processPick(options.get(0));
            } else {
                getBattle().getRoundManager().processPick(options.get(1));
            }
        } else {
            report("error: pickAbility() failed!");
            cancelTurn();
        }
    }

    //--------//
    //-COMBAT-//
    //--------//

    private final TreeMap<FleetToken, DuelManager.AttackInfo> duels = new TreeMap<>();

    @Override
    protected void combat() { //Duel-picking
        super.combat();
        Token enemy;
        //First "draft"
        for (Ship ship : getFleet().getShips()) {
            if (ship != null && !ship.isUsed()) {
                enemy = getDuelTarget(ship.getToken());
                if (enemy != null) {
                    duels.put((FleetToken) ship.getToken(), new DuelManager.AttackInfo(ship.getToken(), enemy));
                }
            }
        }
        //Retarget overkills
        for (Ship ship : getFleet().getShips()) {
            if (ship != null && !ship.isUsed()) {
                ArrayList<Token> enemies = CombatManager.getDuelOpponent(ship.getToken(), duels);
                if (enemies != null && enemies.size() > 0) {
                    Token currentEnemy = enemies.get(0); //Attackers always have only one target
                    if (!currentEnemy.getCard().isMS() && isAlreadyTargetedFatally(currentEnemy, duels, ship.getToken())) {
                        duels.remove((FleetToken) ship.getToken());
                        enemy = getDuelTarget(ship.getToken());
                        if (enemy != null) {
                            if (enemy != currentEnemy) { //"don't attack (= receive dmg) for no reason"
                                duels.put((FleetToken) ship.getToken(), new DuelManager.AttackInfo(ship.getToken(), enemy));
                            }
                        }
                    }
                }
            }
        }
        //Post
        if (duels.size() > 0) {
            getBattle().getCombatManager().setDuels(duels);
            getBattle().getCombatManager().startTacticalPhase();
            duels.clear();
        } else {
            report("No possible duels.");
            delayedCombatEnd();
        }
    }

    private Token getDuelTarget(Token attacker) {
        BattlePlayer[] enemies = getBattle().getEnemies(this);
        Token picked = null;
        Ship strongestKillable = null;
        float dmg;
        boolean desperate = false;
        boolean noTargets = false;
        while (picked == null && !noTargets) {
            for (BattlePlayer enemy : enemies) {
                if (!desperate || picked == null) {
                    if (desperate) {
                        if (getBattle().getCombatManager().canReach(attacker, enemy.getMs().getToken(), enemy.getFleet())) {
                            picked = enemy.getMs().getToken();
                        }
                    }
                    if (picked == null) {
                        for (Ship ship : enemy.getFleet().getShips()) {
                            if (ship != null) {
                                dmg = DuelManager.getDmgAgainstShields(attacker.getCard().getCardInfo().getOffense(), ship.getHealth(), AbilityManager.getArmor(ship), attacker.getCard().getCardInfo().getOffenseType(), ship.getCardInfo().getDefenseType());
                                if ((desperate || !isAlreadyTargetedFatally(ship.getToken(), duels, null)) && (strongestKillable == null || (isBiggerShip(ship, strongestKillable) && dmg >= ship.getHealth())) && getBattle().getCombatManager().canReach(attacker, ship.getToken(), enemy.getFleet())) {
                                    strongestKillable = ship;
                                    picked = ship.getToken();
                                }
                            }
                        }
                    }
                }
            }
            if (!desperate) {
                desperate = true;
            } else {
                noTargets = true;
            }
        }
        return picked;
    }

    //returns whether the token has an opponent that will (supposedly) destroy it
    private boolean isAlreadyTargetedFatally(Token token, TreeMap<FleetToken, DuelManager.AttackInfo> duelMap, Token exclude) {
        ArrayList<Token> opponents = CombatManager.getDuelOpponent(token, duelMap);
        if (opponents != null && opponents.size() > 0) {
            int totalDmg = 0;
            int health = token.getCard().getHealth();
            int dmg;
            for (Token opponent : opponents) {
                if (opponent != exclude) {
                    dmg = DuelManager.getDmgAgainstShields(opponent.getCard().getCardInfo().getOffense(), health, AbilityManager.getArmor(token.getCard()), opponent.getCard().getCardInfo().getOffenseType(), token.getCard().getCardInfo().getDefenseType());
                    totalDmg += dmg;
                    health -= dmg;
                }
            }
            return totalDmg >= token.getCard().getHealth();
        }
        return false;
    }

    //TACTICAL PHASE
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
    public void gameOver(BattlePlayer winner) {
        if (winner == this) {
            report("Wow, I've won! How did I do that?");
        }
        super.gameOver(winner);
    }

}
