package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;

import java.util.ArrayList;

public class PossibilityAdvisor {

    public void markPossibilities(Player player, Battle battle) {
        ArrayList<PossibilityInfo> possibilities = getPossibilities(player, battle);
        for (PossibilityInfo possibility : possibilities) {
            possibility.getCard().setPossible(true);
        }
    }

    public void unmarkAll(Player player, Battle battle) {
        //TODO
    }

    public ArrayList<PossibilityInfo> getPossibilities(Player player, Battle battle) {
        ArrayList<PossibilityInfo> possibilities = new ArrayList<>();
        if (player == battle.getWhoseTurn()) {
            //Deployment
            for (Card card : player.getYard().getCards()) {
                if (isPossibleToDeploy(player, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getYard().getCardListMenu()));
                }
            }
            for (Card card : player.getHand().getCards()) {
                if (isPossibleToDeploy(player, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getHand().getCardListMenu()));
                }
            }
            //Abilities
            for (Card card : player.getFleet().getShips()) {
                if (hasPossibleAbility(player, card)) {
                    possibilities.add(new PossibilityInfo(card, player.getFleet().getFleetMenu()));
                }
            }
            for (Card card : player.getSupports().getCards()) {
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
        return false;
    }

    public boolean isPossibleToDeploy(Player player, Card card, boolean checkSpace, Battle battle) {
        if (player == battle.getWhoseTurn() && player.canAfford(card) && tierAllowed(card.getCardInfo().getTier(), battle)) {
            return !checkSpace || ((player.getSupports().hasSpace() || card.getCardInfo().getCardType() != CardType.SUPPORT) || (player.getFleet().hasSpace() || card.getCardInfo().getCardType() != CardType.YARDPRINT));
        }
        return false;
    }

    public boolean tierAllowed(int tier, Battle battle) { return tier <= battle.getRoundManager().getRoundNum(); }

}
