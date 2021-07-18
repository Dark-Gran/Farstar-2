package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.Menu;
import com.darkgran.farstar.battle.gui.YardMenu;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.abilities.AbilityTargets;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Ship;

import java.util.ArrayList;

public class PossibilityAdvisor {

    public ArrayList<PossibilityInfo> getPossibilities(Player player, Battle battle) { //also used by Automaton
        ArrayList<PossibilityInfo> possibilities = new ArrayList<>();
        boolean inCombat = battle.getCombatManager().isActive();
        boolean tacticalPhase = battle.getCombatManager().isTacticalPhase();
        Player whoseTurn = !tacticalPhase ? battle.getWhoseTurn() : battle.getCombatManager().getActivePlayer().getPlayer();
        if (player == whoseTurn) {
            if (!inCombat && hasPossibleAbility(player, player.getMs())) {
                possibilities.add(new PossibilityInfo(player.getMs(), null));
            }
            for (Card card : player.getSupports()) {
                if (!inCombat && hasPossibleAbility(player, card)) {
                    possibilities.add(new PossibilityInfo(card, player.getSupports().getCardListMenu()));
                }
            }
            for (Card card : player.getHand()) {
                if ((!inCombat || (card.isTactic() && tacticalPhase && !player.getFleet().isEmpty())) && isPossibleToDeploy(player, whoseTurn, card, true, battle)) {
                    possibilities.add(new PossibilityInfo(card, player.getHand().getCardListMenu()));
                }
            }
            for (int i = player.getYard().size()-1; i >= 0; i--) {
                if ((!inCombat || (player.getYard().get(i).isTactic() && tacticalPhase)) && isPossibleToDeploy(player, whoseTurn, player.getYard().get(i), true, battle)) {
                    possibilities.add(new PossibilityInfo(player.getYard().get(i), player.getYard().getCardListMenu()));
                }
            }
            for (Ship ship : player.getFleet().getShips()) {
                if (ship != null) {
                    if ((!inCombat && hasPossibleAbility(player, ship)) || (inCombat && !tacticalPhase && !ship.isUsed())) {
                        possibilities.add(new PossibilityInfo(ship, player.getFleet().getFleetMenu()));
                    }
                }
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

    public boolean isPossibleToDeploy(Player player, Player whoseTurn, Card card, boolean checkSpace, Battle battle) { //in-future: "spread" or parametrize to be used with Notifications (eg. "Insufficient Resources.")... (see RoundManager's call)
        if (player == whoseTurn && player.canAfford(card) && tierAllowed(card.getCardInfo().getTier(), battle) && allowedAoE(player, card, battle) && (!player.getFleet().isEmpty() || !card.isPurelyOffensiveChange())) {
            return !checkSpace || ((player.getSupports().hasSpace() || card.getCardInfo().getCardType() != CardType.SUPPORT) && (player.getFleet().hasSpace() || (card.getCardInfo().getCardType() != CardType.YARDPRINT && card.getCardInfo().getCardType() != CardType.BLUEPRINT)));
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

    public Menu getTargetMenu(Card card, Player player) {
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
        if (possibilities.size() != 0) {
            boolean yardToo = false;
            for (PossibilityInfo possibility : possibilities) {
                possibility.getCard().setPossible(true);
                if (!yardToo && possibility.getMenu() instanceof YardMenu) {
                    yardToo = true;
                }
            }
            if (yardToo) {
                player.getYard().getYardButton().setExtraState(true);
            }
        } else if (!battle.getCombatManager().isActive()){
            battle.getBattleScreen().getBattleStage().getTurnButton().setExtraState(true);
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
        player.getYard().getYardButton().setExtraState(false);
        battle.getBattleScreen().getBattleStage().getTurnButton().setExtraState(false);
    }

}
