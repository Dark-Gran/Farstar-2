package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.gui.Notification;
import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.gui.battlegui.Menu;
import com.darkgran.farstar.gui.battlegui.YardMenu;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.cards.AbilityStarter;
import com.darkgran.farstar.cards.AbilityTargets;
import com.darkgran.farstar.cards.CardType;

import java.util.ArrayList;

public class PossibilityAdvisor {

    public ArrayList<PossibilityInfo> getPossibilities(BattlePlayer battlePlayer, Battle battle) { //also used by Automaton, who also cares about the order of priorities ("plays whatever comes first" by definition; the order does not matter for non-bot purposes (ie. gui)); in-future: bots should sort possibilities for themselves by whatever specific rules they have
        ArrayList<PossibilityInfo> possibilities = new ArrayList<>();
        boolean inCombat = battle.getCombatManager().isActive();
        boolean tacticalPhase = battle.getCombatManager().isTacticalPhase();
        BattlePlayer whoseTurn = battle.getWhoseTurn();
        if (battlePlayer == whoseTurn) {
            for (BattleCard battleCard : battlePlayer.getSupports()) {
                if (!inCombat && hasPossibleAbility(battlePlayer, battleCard, false)) {
                    possibilities.add(new PossibilityInfo(battleCard, battlePlayer.getSupports().getCardListMenu()));
                }
            }
            if (!inCombat && hasPossibleAbility(battlePlayer, battlePlayer.getMs(), false)) {
                possibilities.add(new PossibilityInfo(battlePlayer.getMs(), null));
            }
            for (BattleCard battleCard : battlePlayer.getHand()) {
                if ((!inCombat || (battleCard.isTactic() && tacticalPhase)) && isPossibleToDeploy(battlePlayer, whoseTurn, battleCard, true, battle, false)) {
                    possibilities.add(new PossibilityInfo(battleCard, battlePlayer.getHand().getCardListMenu()));
                }
            }
            for (int i = battlePlayer.getYard().size()-1; i >= 0; i--) {
                if ((!inCombat || (battlePlayer.getYard().get(i).isTactic() && tacticalPhase)) && isPossibleToDeploy(battlePlayer, whoseTurn, battlePlayer.getYard().get(i), true, battle, false)) {
                    possibilities.add(new PossibilityInfo(battlePlayer.getYard().get(i), battlePlayer.getYard().getCardListMenu()));
                }
            }
            for (Ship ship : battlePlayer.getFleet().getShips()) {
                if (ship != null && CombatManager.getDuel(ship.getToken(), battle.getCombatManager().getDuels()) == null) {
                    if ((!inCombat && hasPossibleAbility(battlePlayer, ship, false)) || (inCombat && !tacticalPhase && !ship.isUsed())) {
                        possibilities.add(new PossibilityInfo(ship, battlePlayer.getFleet().getFleetMenu()));
                    }
                }
            }
        }
        return possibilities;
    }

    public void reportDeployability(Battle battle, boolean affordable, boolean learned, boolean targetsPresent, boolean space) {
        int duration = 3;
        if (!learned) {
            NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Insufficient Technological Tier.", duration, true);
        } else if (!affordable) {
            NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Insufficient Resources.", duration, true);
        } else if (!targetsPresent) {
            NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "No Possible Targets.", duration, true);
        } else if (!space) {
            NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Cannot Man More Ships.", duration, true);
        }
    }

    public boolean hasPossibleAbility(BattlePlayer battlePlayer, BattleCard battleCard, boolean signs) {
        if (battleCard != null && !battleCard.isUsed()) {
            for (int i = 0; i < battleCard.getCardInfo().getAbilities().size(); i++) {
                if (battleCard.getCardInfo().getAbilities().get(i) != null) {
                    if (battleCard.getCardInfo().getAbilities().get(i).getStarter() == AbilityStarter.USE) {
                        AbilityInfo abilityInfo = battleCard.getCardInfo().getAbilities().get(i);
                        if (battlePlayer.canAfford(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter())) {
                            return true;
                        } else if (signs) {
                            reportDeployability(battlePlayer.getBattle(), false, true, true, true);
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPossibleToDeploy(BattlePlayer battlePlayer, BattlePlayer whoseTurn, BattleCard battleCard, boolean checkSpace, Battle battle, boolean signs) { //in-future: "spread" or parametrize to be used with Notifications (eg. "Insufficient Resources.")... (see RoundManager's call)
        boolean affordable = battlePlayer.canAfford(battleCard);
        boolean learned = tierAllowed(battleCard.getCardInfo().getTier(), battle);
        boolean targetsPresent = allowedAoE(battlePlayer, battleCard, battle) && (!battlePlayer.getFleet().isEmpty() || !battleCard.isPurelyOffensiveChange());
        boolean space = !checkSpace || ((battlePlayer.getSupports().hasSpace() || battleCard.getCardInfo().getCardType() != CardType.SUPPORT) && (battlePlayer.getFleet().hasSpace() || (battleCard.getCardInfo().getCardType() != CardType.YARDPRINT && battleCard.getCardInfo().getCardType() != CardType.BLUEPRINT)));
        if (signs) { reportDeployability(battle, affordable, learned, targetsPresent, space); }
        if (battlePlayer == whoseTurn && affordable && learned && targetsPresent) {
            return space;
        }
        return false;
    }

    public boolean allowedAoE (BattlePlayer battlePlayer, BattleCard battleCard, Battle battle) {
        for (AbilityInfo ability : battleCard.getCardInfo().getAbilities()) {
            if (AbilityTargets.isAoE(ability.getTargets())) {
                BattlePlayer[] enemies = battle.getEnemies(battlePlayer);
                for (BattlePlayer enemy : enemies) {
                    if (!enemy.getFleet().isEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static boolean tierAllowed(int tier, Battle battle) {
        return tier <= battle.getRoundManager().getRoundNum();
    }

    public Menu getTargetMenu(BattleCard battleCard, BattlePlayer battlePlayer) {
        if (battleCard.getCardInfo().getCardType() == CardType.SUPPORT) {
            return battlePlayer.getSupports().getCardListMenu();
        } else {
            return battlePlayer.getFleet().getFleetMenu();
        }
    }

    //POSSIBILITY SETTERS FOR CARDS

    public void refresh(BattlePlayer currentBattlePlayer, Battle battle) {
        battle.unMarkAllPossibilities();
        if (!(currentBattlePlayer instanceof Bot)) {
            markPossibilities(currentBattlePlayer, battle);
        }
    }

    public void markPossibilities(BattlePlayer battlePlayer, Battle battle) {
        ArrayList<PossibilityInfo> possibilities = getPossibilities(battlePlayer, battle);
        if (possibilities.size() != 0) {
            boolean yardToo = false;
            for (PossibilityInfo possibility : possibilities) {
                possibility.getCard().setPossible(true);
                if (!yardToo && possibility.getMenu() instanceof YardMenu) {
                    yardToo = true;
                }
            }
            if (yardToo) {
                battlePlayer.getYard().getYardButton().setExtraState(true);
            }
        } else if (!battle.getCombatManager().isActive()){
            battle.getBattleScreen().getBattleStage().getTurnButton().setExtraState(true);
        }
    }

    public void unMarkAll(BattlePlayer battlePlayer, Battle battle) {
        //Deployment
        for (BattleCard battleCard : battlePlayer.getYard()) {
            battleCard.setPossible(false);
        }
        for (BattleCard battleCard : battlePlayer.getHand()) {
            battleCard.setPossible(false);
        }
        //Abilities
        if (!battle.getCombatManager().isTacticalPhase()) {
            for (BattleCard battleCard : battlePlayer.getFleet().getShips()) {
                if (battleCard != null) {
                    battleCard.setPossible(false);
                }
            }
        }
        for (BattleCard battleCard : battlePlayer.getSupports()) {
            battleCard.setPossible(false);
        }
        battlePlayer.getMs().setPossible(false);
        battlePlayer.getYard().getYardButton().setExtraState(false);
        battle.getBattleScreen().getBattleStage().getTurnButton().setExtraState(false);
    }

}
