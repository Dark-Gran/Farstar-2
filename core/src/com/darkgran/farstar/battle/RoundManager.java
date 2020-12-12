package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;

import java.util.ArrayList;

public class RoundManager {
    private final Battle battle;
    private boolean launched = false;
    private boolean firstTurnThisRound;
    private int roundNum = 0;
    private boolean targetingActive;
    private DeploymentInfo postponedDeploy = new DeploymentInfo();
    private AbilityPicker abilityPicker;
    public RoundManager(Battle battle) {
        this.battle = battle;
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
        battle.getWhoseTurn().getHand().drawCards(battle.getWhoseTurn().getDeck(), battle.CARDS_PER_TURN);
        resourceIncomes(battle.getWhoseTurn());
        System.out.println("Player #"+battle.getWhoseTurn().getBattleID()+" may play his cards.");
    }

    public void resourceIncomes(Player player) {
        int income = roundNum;
        if (income > battle.MAX_TECH_INCOME) {
            income = battle.MAX_TECH_INCOME;
        }
        //player.setEnergy(income);
        player.addEnergy(income);
        player.addMatter(income);
    }

    public void endTurn() {
        if (!battle.getCombatManager().isActive() && !battle.isEverythingDisabled() && !targetingActive) {
            battle.closeYards();
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

    public void processDrop(Token token, DropTarget dropTarget, int position, boolean postAbility) {
        boolean success = false;
        if (!targetingActive && token.getCardListMenu() != null) {
            CardType cardType = token.getCard().getCardInfo().getCardType();
            if (!battle.activeCombatOrDuel() || cardType == CardType.TACTIC) {
                //OUTSIDE COMBAT OR TACTIC
                Player whoseTurn;
                if (!battle.getCombatManager().getDuelManager().isActive()) { whoseTurn = battle.getWhoseTurn(); }
                else { whoseTurn = battle.getCombatManager().getDuelManager().getActivePlayer().getPlayer(); }
                if (token.getCardListMenu().getPlayer() == whoseTurn) {
                    Card targetCard = null;
                    if (whoseTurn.canAfford(token.getCard()) && tierAllowed(token.getCard().getCardInfo().getTier())) {
                        //TARGETING ANYWHERE FOR ACTION-CARDS
                        if (cardType == CardType.ACTION) {
                            success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                        //TARGETING SUPPORTS
                        } else if (dropTarget instanceof SupportMenu) {
                            if (((SupportMenu) dropTarget).getCardList() instanceof Supports && position != -1) {
                                Supports supports = (Supports) ((SupportMenu) dropTarget).getCardList();
                                if ((cardType == CardType.SUPPORT && supports == whoseTurn.getSupports()) || CardType.isSpell(cardType)) {
                                    if (!battle.activeCombatOrDuel()) {
                                        if (!postAbility) {
                                            Card target = null;
                                            if (supports.getCards().size() > SupportMenu.unTranslatePosition(position)) {
                                                target = supports.getCards().get(SupportMenu.unTranslatePosition(position));
                                            }
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
                                    if (!battle.getCombatManager().getDuelManager().isActive() || (fleet.getShips()[position].getToken() != null && fleet.getShips()[position].getToken().isInDuel())) {
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
                            if (!battle.activeCombatOrDuel() || ms.isInDuel()) {
                                //ABILITIES
                                if (CardType.isSpell(cardType)) { //&& ms.getCard().getPlayer() == whoseTurn
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
                        whoseTurn.payday(token.getCard());
                        token.addCardToJunk();
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
    }

    private boolean checkAllAbilities(Token caster, Token target, AbilityStarter abilityStarter, Player owner, DropTarget dropTarget) {
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
        if (abilityPicker != null) {
            abilityPicker.setAbilityInfos(new ArrayList<>());
            for (AbilityInfo option : options) {
                abilityPicker.getAbilityInfos().add(option);
            }
            abilityPicker.refreshOptions();
            abilityPicker.enable();
            postponedDeploy.saveInDeployment(caster, null, dropTarget, target);
        }
    }

    public boolean playAbility(Token caster, Card target, AbilityStarter abilityStarter, Player owner, DropTarget dropTarget, AbilityInfo ability) {
        boolean success = false;
        if (owner != null) {
            success = battle.getAbilityManager().playAbility(caster, target, ability, dropTarget);
            if (success) {
                owner.payday(ability.getResourcePrice().getEnergy(), ability.getResourcePrice().getMatter());
                if (abilityStarter == AbilityStarter.USE) {
                    caster.getCard().setUsed(true);
                }
            }
        }
        return success;
    }

    public void processPick(AbilityInfo ability) {
        if (!battle.isEverythingDisabled() && postponedDeploy.getCaster() != null) {
            if (abilityPicker != null) { abilityPicker.disable(); }
            if (playAbility(postponedDeploy.getCaster(), (postponedDeploy.getTarget()!=null) ? postponedDeploy.getTarget().getCard() : null, ability.getStarter(), postponedDeploy.getCaster().getCard().getPlayer(), postponedDeploy.getDrop(), ability)) {
                processDrop(postponedDeploy.getCaster(), postponedDeploy.getDrop(), postponedDeploy.getPosition(), true);
                postponedDeploy.resetInDeployment();
            }
        }
    }

    public void askForTargets(Token token, AbilityInfo ability, DropTarget dropTarget) {
        targetingActive = true;
        postponedDeploy.saveInDeployment(token, ability, dropTarget, null);
        System.out.println("Need a Target.");
    }

    public void processClick(Token token, Player owner) {
        if (!battle.isEverythingDisabled() && !battle.activeCombatOrDuel()) {
            if (targetingActive) {
                processTarget(token);
            } else if (owner == battle.getWhoseTurn() && tierAllowed(token.getCard().getCardInfo().getTier())) {
                checkAllAbilities(token, null, AbilityStarter.USE, owner, null);
            }
        }
    }

    public void tryCancel() {
        if (targetingActive) {
            endTargeting();
            System.out.println("Targeting Cancelled.");
        }
    }

    private void processTarget(Token target) {
        if (targetingActive && postponedDeploy.getCaster() != null) {
            if (AbilityManager.validAbilityTarget(postponedDeploy.getAbility(), postponedDeploy.getCaster().getCard(), target.getCard())) {
                //System.out.println("Playing ability...");
                if (battle.getAbilityManager().playAbility(postponedDeploy.getCaster(), target.getCard(), postponedDeploy.getAbility(), postponedDeploy.getDrop())) {
                    System.out.println("Targeted-Ability Success!");
                    if (postponedDeploy.getAbility().getStarter() == AbilityStarter.USE) {
                        battle.getWhoseTurn().payday(postponedDeploy.getAbility().getResourcePrice().getEnergy(), postponedDeploy.getAbility().getResourcePrice().getMatter());
                        postponedDeploy.getCaster().getCard().setUsed(true);
                        //System.out.println("Ability price paid.");
                    } else if (postponedDeploy.getDrop() != null) {
                        //System.out.println("Reprocessing original drop...");
                        processDrop(postponedDeploy.getCaster(), postponedDeploy.getDrop(), postponedDeploy.getPosition(), true);
                    }
                    endTargeting();
                }
            } else {
                System.out.println("Invalid Target.");
            }
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

    public boolean tierAllowed(int tier) { return tier <= roundNum; }

    public int getRoundNum() { return roundNum; }

    public Battle getBattle() { return battle; }

    public boolean isFirstTurnThisRound() { return firstTurnThisRound; }

    public boolean isTargetingActive() { return targetingActive; }

    public boolean isLaunched() { return launched; }

    public AbilityPicker getAbilityPicker() { return abilityPicker; }

    public void setAbilityPicker(AbilityPicker abilityPicker) { this.abilityPicker = abilityPicker; }

}
