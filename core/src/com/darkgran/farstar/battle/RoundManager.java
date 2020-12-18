package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.MothershipToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardInfo;
import com.darkgran.farstar.battle.players.cards.CardType;

import java.util.ArrayList;

import static com.darkgran.farstar.battle.BattleSettings.CARDS_PER_TURN;
import static com.darkgran.farstar.battle.BattleSettings.MAX_TECH_INCOME;

public class RoundManager {
    private final Battle battle;
    private final PossibilityAdvisor possibilityAdvisor;
    private boolean launched = false;
    private boolean firstTurnThisRound;
    private int roundNum = 0;
    private boolean targetingActive;
    private DeploymentInfo postponedDeploy = new DeploymentInfo();
    private AbilityPicker abilityPicker;

    public RoundManager(Battle battle, PossibilityAdvisor possibilityAdvisor) {
        this.battle = battle;
        this.possibilityAdvisor = possibilityAdvisor;
    }

    //-------------//
    //-ROUND-FRAME-//
    //-------------//

    public void launch() {
        roundNum = 0;
        endTargeting();
        launched = true;
        newRound();
    }

    public void newRound() {
        roundNum++;
        firstTurnThisRound = true;
        newTurn();
    }

    public void newTurn() {
        battle.getWhoseTurn().getHand().drawCards(battle.getWhoseTurn().getDeck(), CARDS_PER_TURN);
        resourceIncomes(battle.getWhoseTurn());
        System.out.println("Player #"+battle.getWhoseTurn().getBattleID()+" may play his cards.");
        if (battle.getWhoseTurn() instanceof Bot) {
            ((Bot) battle.getWhoseTurn()).newTurn();
        } else {
            possibilityAdvisor.markPossibilities(battle.getWhoseTurn(), battle);
        }
    }

    public void resourceIncomes(Player player) {
        int income = roundNum;
        if (income > MAX_TECH_INCOME) {
            income = MAX_TECH_INCOME;
        }
        player.setEnergy(income);
        player.addMatter(income);
    }

    public void endTurn() {
        if (!battle.getCombatManager().isActive() && !battle.isEverythingDisabled() && !targetingActive) {
            battle.closeYards();
            battle.unMarkAllPossibilities();
            battle.getCombatManager().launchCombat();
        }
    }

    public void afterCombat() {
        if (!battle.isEverythingDisabled()) {
            battle.tickEffects();
            battle.refreshUsedShips();
            battle.passTurn();
            if (firstTurnThisRound) {
                firstTurnThisRound = false;
                newTurn();
            } else {
                newRound();
            }
        }
    }

    //---------------//
    //-PLAYING-CARDS-//
    //---------------//

    public boolean processDrop(Token token, DropTarget dropTarget, int position, boolean postAbility, boolean payPrice) {
        boolean success = false;
        if (!targetingActive && !abilityPicker.isActive() && token.getCardListMenu() != null) {
            CardType cardType = token.getCard().getCardInfo().getCardType();
            if (!battle.activeCombatOrDuel() || cardType == CardType.TACTIC) {
                //OUTSIDE COMBAT OR TACTIC
                Player whoseTurn;
                if (!battle.getCombatManager().getDuelManager().isActive()) { whoseTurn = battle.getWhoseTurn(); }
                else { whoseTurn = battle.getCombatManager().getDuelManager().getActivePlayer().getPlayer(); }
                if (token.getCardListMenu().getPlayer() == whoseTurn) {
                    Card targetCard = null;
                    if (possibilityAdvisor.isPossibleToDeploy(whoseTurn, whoseTurn, token.getCard(), false, battle)) {
                        //TARGETING ANYWHERE FOR ACTION-CARDS
                        if (cardType == CardType.ACTION) {
                            if (!postAbility) {
                                success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                            }
                        //TARGETING SUPPORTS
                        } else if (dropTarget instanceof SupportMenu) {
                            if (((SupportMenu) dropTarget).getCardList() instanceof Supports && position != -1) {
                                Supports supports = (Supports) ((SupportMenu) dropTarget).getCardList();
                                if ((cardType == CardType.SUPPORT && supports == whoseTurn.getSupports()) || CardType.isSpell(cardType)) {
                                    if (!battle.activeCombatOrDuel()) {
                                        if (!postAbility) {
                                            success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                                        }
                                        if (cardType == CardType.SUPPORT) {
                                            if (postAbility || success) {
                                                success = supports.addCard(token.getCard());
                                            }
                                        }
                                    }
                                }
                            }
                            //TARGETING FLEET
                        } else if (dropTarget instanceof FleetMenu) {
                            if (cardType != CardType.SUPPORT) {
                                Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                                if ((fleet == whoseTurn.getFleet() || !CardType.isShip(cardType)) && position != -1) {
                                    if (!battle.getCombatManager().getDuelManager().isActive() || (fleet.getShips()[position].getToken() != null && fleet.getShips()[position].isInDuel())) {
                                        //ABILITIES
                                        postponedDeploy.setPosition(position);
                                        if (!postAbility) {
                                            success = checkAllAbilities(token, (fleet.getShips()[position] != null) ? fleet.getShips()[position].getToken() : null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                                        }
                                        if (postAbility || success) {
                                            if (fleet.getShips()[position] != null) {
                                                targetCard = fleet.getShips()[position].getToken().getCard();
                                            }
                                            //DEPLOYMENT
                                            if (CardType.isShip(cardType)) {
                                                success = fleet.addShip(token, position);
                                            }
                                        }
                                    }
                                }
                            }
                            //TARGETING MS
                        } else if (dropTarget instanceof MothershipToken) {
                            MothershipToken ms = (MothershipToken) dropTarget;
                            if (!battle.activeCombatOrDuel() || ms.getCard().isInDuel()) {
                                //ABILITIES
                                if (CardType.isSpell(cardType)) {
                                    if (!postAbility) {
                                        success = checkAllAbilities(token, ms, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                                    }
                                    if (postAbility || success) {
                                        targetCard = ms.getCard();
                                    }
                                }
                            }
                        }
                    }
                    //PAYMENT + DISCARD (incl. targeting discard)
                    if (success || postAbility) {
                        //System.out.println("Drop Success.");
                        if (targetCard != null && cardType == CardType.TACTIC && battle.getCombatManager().getDuelManager().isActive()) {
                            battle.getCombatManager().getDuelManager().saveTactic(token.getCard(), targetCard);
                        }
                        if (payPrice) { whoseTurn.payday(token.getCard()); }
                        token.addCardToJunk();
                        possibilityAdvisor.refresh(battle.getWhoseTurn(), battle);
                    } else if (dropTarget instanceof JunkButton && token instanceof HandToken) { //Target: Discard
                        Junkpile junkpile = ((JunkButton) dropTarget).getPlayer().getJunkpile();
                        if (junkpile == whoseTurn.getJunkpile()) {
                            token.addCardToJunk();
                            success = true;
                        }
                    }
                }
            }
        }
        //HAND ONLY
        if (token instanceof HandToken) {
            if (!success && !postAbility) {
                ((HandToken) token).resetPosition();
            } else {
                token.destroy();
            }
        }
        return success;
    }

    public boolean checkAllAbilities(Token caster, Token target, AbilityStarter abilityStarter, Player owner, DropTarget dropTarget) {
        if (abilityStarter != AbilityStarter.USE || !caster.getCard().isUsed()) {
            CardInfo cardInfo = caster.getCard().getCardInfo();
            ArrayList<AbilityInfo> options = new ArrayList<>();
            for (int i = 0; i < cardInfo.getAbilities().size(); i++) {
                if (cardInfo.getAbilities().get(i) != null) {
                    if (cardInfo.getAbilities().get(i).getStarter() == abilityStarter) { //cardType == CardType.UPGRADE || cardType == CardType.TACTIC ||
                        AbilityInfo abilityInfo = cardInfo.getAbilities().get(i);
                        if (owner.canAfford(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter())) {
                            options.add(abilityInfo);
                        }
                    }
                }
            }
            if (options.size() == 1) {
                return playAbility(caster, (target!=null) ? target.getCard() : null, abilityStarter, owner, dropTarget, options.get(0));
            } else if (options.size() > 1) {
                askForAbility(caster, target, dropTarget, options);
                return false;
            }
        }
        return true;
    }

    public void askForAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        postponedDeploy.saveInDeployment(caster, null, dropTarget, target);
        if (battle.getWhoseTurn() instanceof Bot) {
            ((Bot) battle.getWhoseTurn()).pickAbility(caster, target, dropTarget, options);
        } else if (abilityPicker != null) {
            abilityPicker.setAbilityInfos(new ArrayList<>());
            for (AbilityInfo option : options) {
                abilityPicker.getAbilityInfos().add(option);
            }
            abilityPicker.enable();
        }
    }

    public boolean playAbility(Token caster, Card target, AbilityStarter abilityStarter, Player owner, DropTarget dropTarget, AbilityInfo ability) {
        boolean success = battle.getAbilityManager().playAbility(caster, target, ability, dropTarget);
        if (success) {
            if (owner != null) { owner.payday(ability.getResourcePrice().getEnergy(), ability.getResourcePrice().getMatter()); }
            if (abilityStarter == AbilityStarter.USE) {
                caster.getCard().setUsed(true);
            }
            possibilityAdvisor.refresh(battle.getWhoseTurn(), battle);
        }
        return success;
    }

    public void processPick(AbilityInfo ability) {
        if (!battle.isEverythingDisabled() && postponedDeploy.getCaster() != null) {
            if (abilityPicker != null) { abilityPicker.disable(); }
            if (playAbility(postponedDeploy.getCaster(), (postponedDeploy.getTarget()!=null) ? postponedDeploy.getTarget().getCard() : null, ability.getStarter(), postponedDeploy.getCaster().getCard().getPlayer(), postponedDeploy.getDrop(), ability)) {
                processDrop(postponedDeploy.getCaster(), postponedDeploy.getDrop(), postponedDeploy.getPosition(), true, ability.getStarter()==AbilityStarter.DEPLOY);
                postponedDeploy.resetInDeployment();
            }
        }
    }

    public void askForTargets(Token token, AbilityInfo ability, DropTarget dropTarget) {
        targetingActive = true;
        postponedDeploy.saveInDeployment(token, ability, dropTarget, null);
        System.out.println("Need a Target.");
        if (battle.getWhoseTurn() instanceof Bot) {
            ((Bot) battle.getWhoseTurn()).chooseTargets(token, ability);
        }
    }

    public void processClick(Token token, Player owner) {
        if (!battle.isEverythingDisabled() && !battle.activeCombatOrDuel() && getBattle().getWhoseTurn() instanceof LocalPlayer) {
            if (targetingActive) {
                processTarget(token);
            } else if (possibilityAdvisor.isPossibleToDeploy(owner, battle.getWhoseTurn(), token.getCard(), false, battle)) {
                checkAllAbilities(token, null, AbilityStarter.USE, owner, null);
            }
        }
    }

    public void processTarget(Token target) {
        if (targetingActive && postponedDeploy.getCaster() != null) {
            if (battle.getAbilityManager().validAbilityTarget(postponedDeploy.getAbility(), postponedDeploy.getCaster().getCard(), target.getCard())) {
                //System.out.println("Playing ability...");
                if (battle.getAbilityManager().playAbility(postponedDeploy.getCaster(), target.getCard(), postponedDeploy.getAbility(), postponedDeploy.getDrop())) {
                    System.out.println("Targeted-Ability Success!");
                    if (postponedDeploy.getAbility().getStarter() == AbilityStarter.USE) {
                        battle.getWhoseTurn().payday(postponedDeploy.getAbility().getResourcePrice().getEnergy(), postponedDeploy.getAbility().getResourcePrice().getMatter());
                        postponedDeploy.getCaster().getCard().setUsed(true);
                        possibilityAdvisor.refresh(battle.getWhoseTurn(), battle);
                        //System.out.println("Ability price paid.");
                    } else {
                        //System.out.println("Reprocessing original drop...");
                        targetingActive = false;
                        processDrop(postponedDeploy.getCaster(), postponedDeploy.getDrop(), postponedDeploy.getPosition(), true, postponedDeploy.getAbility().getStarter()==AbilityStarter.DEPLOY);
                    }
                    endTargeting();
                }
            } else {
                System.out.println("Invalid Target.");
            }
        }
    }

    public void tryCancel() {
        if (targetingActive) {
            endTargeting();
            System.out.println("Targeting Cancelled.");
        }
        if (abilityPicker.isActive()) {
            abilityPicker.disable();
            postponedDeploy.resetInDeployment();
        }
    }

    private void endTargeting() {
        targetingActive = false;
        postponedDeploy.resetInDeployment();
    }

    //-----------//
    //-UTILITIES-//
    //-----------//

    public static boolean ownsToken(Player player, Token token) {
        if (token instanceof FleetToken) {
            return player == ((FleetToken) token).getFleetMenu().getPlayer();
        } else {
            return player == token.getCardListMenu().getPlayer();
        }
    }

    public int getRoundNum() { return roundNum; }

    public Battle getBattle() { return battle; }

    public boolean isFirstTurnThisRound() { return firstTurnThisRound; }

    public boolean isTargetingActive() { return targetingActive; }

    public boolean isLaunched() { return launched; }

    public AbilityPicker getAbilityPicker() { return abilityPicker; }

    public void setAbilityPicker(AbilityPicker abilityPicker) { this.abilityPicker = abilityPicker; }

    public PossibilityAdvisor getPossibilityAdvisor() { return possibilityAdvisor; }

}
