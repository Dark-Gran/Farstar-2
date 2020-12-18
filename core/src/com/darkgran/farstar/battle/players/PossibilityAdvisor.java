package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.BaseMenu;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;

import java.util.ArrayList;

public class PossibilityAdvisor {

    public void refresh(Player currentPlayer, Battle battle) {
        battle.unMarkAllPossibilities();
        if (!(currentPlayer instanceof Bot)) {
            markPossibilities(currentPlayer, battle);
        }
    }

    public void markPossibilities(Player player, Battle battle) {
        ArrayList<PossibilityInfo> possibilities = getPossibilities(player, battle);
        for (PossibilityInfo possibility : possibilities) {
            possibility.getCard().setPossible(true);
        }
    }

    public ArrayList<PossibilityInfo> getPossibilities(Player player, Battle battle) {
        ArrayList<PossibilityInfo> possibilities = new ArrayList<>();
        if (player == battle.getWhoseTurn()) {
            //Deployment
            for (Card card : player.getHand()) {
                if (isPossibleToDeploy(player, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getHand().getCardListMenu()));
                }
            }
            for (Card card : player.getYard()) {
                if (isPossibleToDeploy(player, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getYard().getCardListMenu()));
                }
            }
            //Abilities
            for (Card card : player.getFleet().getShips()) {
                if (hasPossibleAbility(player, card)) {
                    possibilities.add(new PossibilityInfo(card, player.getFleet().getFleetMenu()));
                }
            }
            for (Card card : player.getSupports()) {
                if (hasPossibleAbility(player, card)) {
                    possibilities.add(new PossibilityInfo(card, player.getSupports().getCardListMenu()));
                }
            }
            if (hasPossibleAbility(player, player.getMs())) {
                possibilities.add(new PossibilityInfo(player.getMs(), null));
            }
        }
        return possibilities;
    }

    public boolean hasPossibleAbility(Player player, Card card) {
        if (card != null && !card.isUsed()) {
            for (int i = 0; i < card.getCardInfo().getAbilities().size(); i++) {
                if (card.getCardInfo().getAbilities().get(i) != null) {
                    if (card.getCardInfo().getAbilities().get(i).getStarter() == AbilityStarter.USE) {
                        AbilityInfo abilityInfo = card.getCardInfo().getAbilities().get(i);
                        if (player.canAfford(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPossibleToDeploy(Player player, Card card, boolean checkSpace, Battle battle) { //TODO "Plasblast check"
        if (player == battle.getWhoseTurn() && player.canAfford(card) && tierAllowed(card.getCardInfo().getTier(), battle)) {
            return !checkSpace || ((player.getSupports().hasSpace() || card.getCardInfo().getCardType() != CardType.SUPPORT) && (player.getFleet().hasSpace() || card.getCardInfo().getCardType() != CardType.YARDPRINT));
        }
        return false;
    }

    public boolean tierAllowed(int tier, Battle battle) { return tier <= battle.getRoundManager().getRoundNum(); }

    public void unMarkAll(Player player, Battle battle) {
        //Deployment
        for (Card card : player.getYard()) {
            card.setPossible(false);
        }
        for (Card card : player.getHand()) {
            card.setPossible(false);
        }
        //Abilities
        for (Card card : player.getFleet().getShips()) {
            if (card != null) { card.setPossible(false); }
        }
        for (Card card : player.getSupports()) {
            card.setPossible(false);
        }
        player.getMs().setPossible(false);
    }

    public BaseMenu getTargetMenu(Card card, Player player) {
        if (card.getCardInfo().getCardType() == CardType.SUPPORT) {
            return player.getSupports().getCardListMenu();
        } else {
            return player.getFleet().getFleetMenu();
        }
    }

}
