package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.players.*;

public class RoundManager {
    private final Battle battle;
    private int roundNum = 0;
    private boolean firstTurnThisRound;

    public RoundManager(Battle battle) {
        this.battle = battle;
    }

    public void launch() {
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
            tickEffects();
            battle.passTurn();
            if (firstTurnThisRound) {
                firstTurnThisRound = false;
                newTurn();
            } else {
                newRound();
            }
        }
    }

    private void tickEffects() {
        battle.getWhoseTurn().getMs().checkEffects(battle.getAbilityManager());
        battle.getWhoseTurn().getFleet().checkEffectsOnAll(battle.getAbilityManager());
    }

    public void processDrop(Token token, DropTarget dropTarget, int position) {
        boolean success = false;
        //OUTSIDE COMBAT
        if (token.getTokenMenu() != null && !battle.getCombatManager().isActive() && !battle.getCombatManager().getDuelManager().isActive()) {
            Player whoseTurn = battle.getWhoseTurn();
            //IS ON TURN
            if (token.getTokenMenu().getPlayer() == whoseTurn) {
                CardType cardType = token.getCard().getCardInfo().getCardType();
                //TARGETING FLEET
                if (dropTarget instanceof FleetMenu) {
                    Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                    if (fleet == whoseTurn.getFleet() && whoseTurn.canAfford(token.getCard())) {
                        //ABILITIES
                        success = checkAllAbilities(token.getCard(), fleet.getShips()[position], AbilityStarter.DEPLOY);
                        //DEPLOYMENT
                        if ((cardType == CardType.BLUEPRINT || cardType == CardType.YARD) && success) {
                            success = fleet.addShip(token, position);
                        }
                    }
                //TARGETING MS
                } else if (dropTarget instanceof MothershipToken) {
                    MothershipToken ms = (MothershipToken) dropTarget;
                    //ABILITIES
                    if (ms.getPlayer() == whoseTurn && whoseTurn.canAfford(token.getCard()) && cardType == CardType.UPGRADE) {
                        success = checkAllAbilities(token.getCard(), ms.getCard(), AbilityStarter.DEPLOY);
                    }
                }
                //PAYMENT + DISCARD (incl. targeting discard)
                if (success) {
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
        //HAND ONLY
        if (token instanceof HandToken) {
            if (!success) { ((HandToken) token).resetPosition(); }
            else { token.destroy(); }
        }
    }

    private boolean checkAllAbilities(Card caster, Card target, AbilityStarter abilityStarter) {
        boolean success = true;
        CardType cardType = caster.getCardInfo().getCardType();
        for (int i = 0; i < caster.getCardInfo().getAbilities().size(); i++) {
            if (caster.getCardInfo().getAbilities().get(i) != null) {
                if (cardType == CardType.UPGRADE || cardType == CardType.TACTIC || caster.getCardInfo().getAbilities().get(i).getStarter() == abilityStarter) {
                    success = battle.getAbilityManager().playAbility(caster, target, i);
                    if (!success) { break; }
                }
            }
        }
        return success;
    }

    public int getRoundNum() { return roundNum; }

    public Battle getBattle() { return battle; }

    public boolean isFirstTurnThisRound() { return firstTurnThisRound; }

}
