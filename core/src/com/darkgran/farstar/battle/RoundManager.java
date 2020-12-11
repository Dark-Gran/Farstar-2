package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;

public class RoundManager {
    private final Battle battle;
    private boolean firstTurnThisRound;
    private int roundNum = 0;
    private boolean targetingActive;
    private Card cardInDeployment;
    private int abilityIxInDeployment = 0;

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    //-------------//
    //-ROUND-FRAME-//
    //-------------//

    public void launch() {
        roundNum = 0;
        targetingActive = false;
        cardInDeployment = null;
        abilityIxInDeployment = 0;
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
        if (!battle.getCombatManager().isActive() && !battle.isEverythingDisabled()) {
            battle.closeYards();
            battle.getCombatManager().launchCombat();
        }
    }

    public void afterCombat() {
        if (!battle.isEverythingDisabled()) {
            battle.tickEffects();
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

    public void processDrop(Token token, DropTarget dropTarget, int position) {
        boolean success = false;
        if (!targetingActive && token.getCardListMenu() != null) {
            CardType cardType = token.getCard().getCardInfo().getCardType();
            if ((!battle.getCombatManager().isActive() && !battle.getCombatManager().getDuelManager().isActive()) || (cardType == CardType.TACTIC)) {
                //OUTSIDE COMBAT OR TACTIC
                Player whoseTurn;
                if (!battle.getCombatManager().getDuelManager().isActive()) { whoseTurn = battle.getWhoseTurn(); }
                else { whoseTurn = battle.getCombatManager().getDuelManager().getActivePlayer().getPlayer(); }
                if (token.getCardListMenu().getPlayer() == whoseTurn && whoseTurn.canAfford(token.getCard()) && tierAllowed(token.getCard().getCardInfo().getTier())) {
                    //TARGETING FLEET
                    Card targetCard = null;
                    if (dropTarget instanceof FleetMenu) {
                        Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                        if (fleet == whoseTurn.getFleet()) {
                            //ABILITIES
                            success = checkAllAbilities(token.getCard(), fleet.getShips()[position], AbilityStarter.DEPLOY, whoseTurn, false);
                            if (success) {
                                if (fleet.getShips()[position] != null) {
                                    targetCard = fleet.getShips()[position].getToken().getCard();
                                }
                                //DEPLOYMENT
                                if (cardType == CardType.BLUEPRINT || cardType == CardType.YARD) {
                                    success = fleet.addShip(token, position);
                                }
                            }
                        }
                    //TARGETING MS
                    } else if (dropTarget instanceof MothershipToken) {
                        MothershipToken ms = (MothershipToken) dropTarget;
                        //ABILITIES
                        if (ms.getPlayer() == whoseTurn && (cardType == CardType.UPGRADE || cardType == CardType.TACTIC)) {
                            success = checkAllAbilities(token.getCard(), ms.getCard(), AbilityStarter.DEPLOY, whoseTurn, false);
                            if (success) { targetCard = ms.getCard(); }
                        }
                    }
                    //PAYMENT + DISCARD (incl. targeting discard)
                    if (success) {
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
            if (!success) {
                ((HandToken) token).resetPosition();
            } else {
                token.destroy();
            }
        }
    }

    public void processClick(Card card, Player owner) {
        if (targetingActive) {
            processTarget(card);
        } else if (owner == battle.getWhoseTurn() && tierAllowed(card.getCardInfo().getTier())) {
            checkAllAbilities(card, null, AbilityStarter.USE, owner, true);
        }
    }

    private boolean checkAllAbilities(Card caster, Card target, AbilityStarter abilityStarter, Player owner, boolean payPrice) {
        boolean success = true;
        CardType cardType = caster.getCardInfo().getCardType();
        for (int i = 0; i < caster.getCardInfo().getAbilities().size(); i++) {
            if (caster.getCardInfo().getAbilities().get(i) != null) {
                if (caster.getCardInfo().getAbilities().get(i).getStarter() == abilityStarter) { //cardType == CardType.UPGRADE || cardType == CardType.TACTIC ||
                    AbilityInfo abilityInfo = caster.getCardInfo().getAbilities().get(i);
                    if (!payPrice || owner.canAfford(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter())) {
                        success = battle.getAbilityManager().playAbility(caster, target, i);
                        if (payPrice && success) {
                            owner.payday(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter());
                        }
                        break; //in-future: support multiple abilities with the same starter
                    }
                }
            }
        }
        return success;
    }

    public void askForTargets(Card card, int abilityIx) {
        targetingActive = true;
        cardInDeployment = card;
        abilityIxInDeployment = abilityIx;
        System.out.println("CardInDeployment saved.");
    }

    private void processTarget(Card card) {

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

    public Card getCardInDeployment() { return cardInDeployment; }

    public boolean isTargetingActive() { return targetingActive; }

}
