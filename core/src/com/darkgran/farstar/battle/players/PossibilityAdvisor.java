package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.BaseMenu;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.abilities.AbilityTargets;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;

import java.util.ArrayList;

public class PossibilityAdvisor {

    public ArrayList<PossibilityInfo> getPossibilities(Player player, Battle battle) { //also used by Automaton
        ArrayList<PossibilityInfo> possibilities = new ArrayList<>();
        boolean activeDuel = battle.getCombatManager().getDuelManager().isActive();
        Player whoseTurn = !activeDuel ? battle.getWhoseTurn() : battle.getCombatManager().getDuelManager().getActivePlayer().getPlayer();
        if (player == whoseTurn) {
            for (Card card : player.getSupports()) {
                if (!activeDuel && hasPossibleAbility(player, card)) {
                    possibilities.add(new PossibilityInfo(card, player.getSupports().getCardListMenu()));
                }
            }
            for (Card card : player.getHand()) {
                if ((!activeDuel || card.isTactic()) && isPossibleToDeploy(player, whoseTurn, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getHand().getCardListMenu()));
                }
            }
            for (Card card : player.getYard()) {
                if ((!activeDuel || card.isTactic()) && isPossibleToDeploy(player, whoseTurn, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getYard().getCardListMenu()));
                }
            }
            for (Card card : player.getFleet().getShips()) {
                if (!activeDuel && hasPossibleAbility(player, card)) {
                    possibilities.add(new PossibilityInfo(card, player.getFleet().getFleetMenu()));
                }
            }
            if (!activeDuel && hasPossibleAbility(player, player.getMs())) {
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

    public boolean isPossibleToDeploy(Player player, Player whoseTurn, Card card, boolean checkSpace, Battle battle) {
        if (player == whoseTurn && player.canAfford(card) && tierAllowed(card.getCardInfo().getTier(), battle) && allowedAoE(player, card, battle) && (!player.getFleet().isEmpty() || !card.isPurelyOffensiveChange())) {
            return !checkSpace || ((player.getSupports().hasSpace() || card.getCardInfo().getCardType() != CardType.SUPPORT) && (player.getFleet().hasSpace() || card.getCardInfo().getCardType() != CardType.YARDPRINT));
        }
        return false;
    }

    public boolean allowedAoE (Player player, Card card, Battle battle) {
        for (AbilityInfo ability : card.getCardInfo().getAbilities()) {
            if (AbilityTargets.isAoE(ability.getTargets())) {
                Player[] enemies = battle.getEnemies(player);
                for (Player enemy : enemies) {
                    if (!enemy.getFleet().isEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public boolean tierAllowed(int tier, Battle battle) {
        return tier <= battle.getRoundManager().getRoundNum();
    }

    public BaseMenu getTargetMenu(Card card, Player player) {
        if (card.getCardInfo().getCardType() == CardType.SUPPORT) {
            return player.getSupports().getCardListMenu();
        } else {
            return player.getFleet().getFleetMenu();
        }
    }

    //POSSIBILITY SETTERS FOR CARDS

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



}
