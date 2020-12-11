package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;

public class RoundManager {
    private final Battle battle;
    private boolean launched = false;
    private boolean firstTurnThisRound;
    private int roundNum = 0;
    private boolean targetingActive;
    //following keeps info for postponed processDrop()
    private Token tokenInDeployment;
    private DropTarget dropInDeployment;
    private int abilityIxInDeployment = 0;
    private int positionInDeployment = 0; //the only one saved on each fleet-drop

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    //-------------//
    //-ROUND-FRAME-//
    //-------------//

    public void launch() {
        roundNum = 0;
        resetInDeployment();
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
        if (!targetingActive && token.getCardListMenu() != null && position != -1) {
            CardType cardType = token.getCard().getCardInfo().getCardType();
            if (!battle.activeCombatOrDuel() || cardType == CardType.TACTIC) {
                //OUTSIDE COMBAT OR TACTIC
                Player whoseTurn;
                if (!battle.getCombatManager().getDuelManager().isActive()) { whoseTurn = battle.getWhoseTurn(); }
                else { whoseTurn = battle.getCombatManager().getDuelManager().getActivePlayer().getPlayer(); }
                if (token.getCardListMenu().getPlayer() == whoseTurn && whoseTurn.canAfford(token.getCard()) && tierAllowed(token.getCard().getCardInfo().getTier())) {
                    Card targetCard = null;
                    //TARGETING ANYWHERE FOR ACTION-CARDS
                    if (cardType == CardType.ACTION) {
                        success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, true, dropTarget);
                    //TARGETING SUPPORTS
                    } else if (dropTarget instanceof SupportMenu) {
                        if (((SupportMenu) dropTarget).getCardList() instanceof Supports) {
                            Supports supports = (Supports) ((SupportMenu) dropTarget).getCardList();
                            if ((cardType == CardType.SUPPORT && supports == whoseTurn.getSupports()) || CardType.isSpell(cardType)) {
                                if (!battle.activeCombatOrDuel()) {
                                    if (!postAbility) {
                                        Card target = null;
                                        if (supports.getCards().size() > SupportMenu.unTranslatePosition(position)) {
                                            target = supports.getCards().get(SupportMenu.unTranslatePosition(position));
                                        }
                                        success = checkAllAbilities(token, target, AbilityStarter.DEPLOY, whoseTurn, false, dropTarget);
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
                        Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                        if (fleet == whoseTurn.getFleet() || !CardType.isShip(cardType)) {
                            if (!battle.getCombatManager().getDuelManager().isActive() || (fleet.getShips()[position].getToken() != null && fleet.getShips()[position].getToken().isInDuel())) {
                                //ABILITIES
                                positionInDeployment = position;
                                if (!postAbility) {
                                    success = checkAllAbilities(token, fleet.getShips()[position], AbilityStarter.DEPLOY, whoseTurn, false, dropTarget);
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
                    //TARGETING MS
                    } else if (dropTarget instanceof MothershipToken) {
                        MothershipToken ms = (MothershipToken) dropTarget;
                        if (!battle.activeCombatOrDuel() || ms.isInDuel()) {
                            //ABILITIES
                            if (CardType.isSpell(cardType)) { //&& ms.getCard().getPlayer() == whoseTurn
                                if (!postAbility) {
                                    success = checkAllAbilities(token, ms.getCard(), AbilityStarter.DEPLOY, whoseTurn, false, dropTarget);
                                }
                                if (postAbility || success) {
                                    targetCard = ms.getCard();
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

    private boolean checkAllAbilities(Token caster, Card target, AbilityStarter abilityStarter, Player owner, boolean payPrice, DropTarget dropTarget) {
        boolean success = true;
        if (abilityStarter != AbilityStarter.USE || !caster.getCard().isUsed()) {
            CardInfo cardInfo = caster.getCard().getCardInfo();
            for (int i = 0; i < cardInfo.getAbilities().size(); i++) {
                if (cardInfo.getAbilities().get(i) != null) {
                    if (cardInfo.getAbilities().get(i).getStarter() == abilityStarter) { //cardType == CardType.UPGRADE || cardType == CardType.TACTIC ||
                        AbilityInfo abilityInfo = cardInfo.getAbilities().get(i);
                        if (!payPrice || owner.canAfford(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter())) {
                            success = battle.getAbilityManager().playAbility(caster, target, i, dropTarget);
                            if (payPrice && success) {
                                owner.payday(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter());
                            }
                            break; //in-future: support multiple abilities with the same starter
                        }
                    }
                }
            }
        }
        return success;
    }

    public void askForTargets(Token token, int abilityIx, DropTarget dropTarget) {
        targetingActive = true;
        tokenInDeployment = token;
        abilityIxInDeployment = abilityIx;
        dropInDeployment = dropTarget;
        System.out.println("Need a Target.");
    }

    public void processClick(Token token, Player owner) {
        if (!battle.isEverythingDisabled() && !battle.activeCombatOrDuel()) {
            if (targetingActive) {
                processTarget(token);
            } else if (owner == battle.getWhoseTurn() && tierAllowed(token.getCard().getCardInfo().getTier())) {
                checkAllAbilities(token, null, AbilityStarter.USE, owner, true, null);
            }
        }
    }

    public void tryCancel() {
        if (targetingActive) {
            resetInDeployment();
            System.out.println("Targeting Cancelled.");
        }
    }

    private void processTarget(Token target) {
        if (targetingActive && tokenInDeployment != null && abilityIxInDeployment < tokenInDeployment.getCard().getCardInfo().getAbilities().size()) {
            AbilityInfo abilityInfo = tokenInDeployment.getCard().getCardInfo().getAbilities().get(abilityIxInDeployment);
            if (AbilityManager.validAbilityTarget(abilityInfo, tokenInDeployment.getCard(), target.getCard())) {
                //System.out.println("Playing ability...");
                if (battle.getAbilityManager().playAbility(tokenInDeployment, target.getCard(), abilityIxInDeployment, dropInDeployment)) {
                    System.out.println("Targeted-Ability Success!");
                    if (abilityInfo.getStarter() == AbilityStarter.USE) {
                        battle.getWhoseTurn().payday(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter());
                        tokenInDeployment.getCard().setUsed(true);
                        //System.out.println("Ability price paid.");
                    } else if (dropInDeployment != null) {
                        //System.out.println("Reprocessing original drop...");
                        processDrop(tokenInDeployment, dropInDeployment, positionInDeployment, true);
                    }
                    resetInDeployment();
                }
            } else {
                System.out.println("Invalid Target.");
            }
        }
    }

    private void resetInDeployment() {
        targetingActive = false;
        tokenInDeployment = null;
        abilityIxInDeployment = 0;
        dropInDeployment = null;
        //positionInDeployment = 0;
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

    public Token getTokenInDeployment() { return tokenInDeployment; }

    public boolean isTargetingActive() { return targetingActive; }

    public boolean isLaunched() { return launched; }

}
