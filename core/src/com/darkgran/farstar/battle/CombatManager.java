package com.darkgran.farstar.battle;

import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.gui.battlegui.*;
import com.darkgran.farstar.gui.tokens.FleetToken;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.cards.EffectType;
import com.darkgran.farstar.gui.Notification;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public abstract class CombatManager {
    private BattleStage battleStage; //must be set before RoundManager.launch() (see BattleScreen constructor)
    private final Battle battle;
    private final DuelManager duelManager;
    private boolean active = false;
    private boolean tacticalPhase = false;
    private TreeMap<FleetToken, DuelManager.AttackInfo> duels = new TreeMap<>();
    private CombatPlayer[] playersA;
    private CombatPlayer[] playersD;
    private CombatPlayer activePlayer;
    private CombatMenu combatMenu;

    CombatManager(Battle battle, DuelManager duelManager) {
        this.battle = battle;
        this.duelManager = duelManager;
    }

    public void launchCombat()
    {
        duels.clear();
        tacticalPhase = false;
        active = true;
        System.out.println("Combat Phase started.");
        fleetCheck();
    }

    //TARGETING PHASE

    private void fleetCheck() {
        if (!battle.isEverythingDisabled()) {
            if (battle.getWhoseTurn().getFleet().noAttackers()) {
                endCombat();
            } else if (battle.getWhoseTurn() instanceof Bot) {
                ((Bot) battle.getWhoseTurn()).newCombat();
            } else {
                battleStage.enableCombatEnd();
                battleStage.getCombatEndButton().setState(duels.size()>0 ? 1 : 0);
                battle.getRoundManager().getPossibilityAdvisor().refresh(battle.getWhoseTurn(), battle);
                NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Choose Your Attacks.", 3);
                //getBattle().getBattleScreen().getNotificationManager().newNotification(Notification.NotificationType.MIDDLE, "ATTACK", 3);
            }
        }
    }

    public void processDrop(Token token, Token targetToken) {
        if (token instanceof FleetToken && active && !tacticalPhase && !duelManager.isActive() && token.getCard().getBattlePlayer() == battle.getWhoseTurn() && !token.getCard().isUsed()) {
            if (targetToken == null || targetToken.getCard().getBattlePlayer() == token.getCard().getBattlePlayer()) {
                duels.remove(token);
            } else if (token != targetToken) {
                if (canReach(token, targetToken, targetToken.getCard().getBattlePlayer().getFleet())) {
                    duels.remove(token);
                    duels.put((FleetToken) token, new DuelManager.AttackInfo(token, targetToken));
                }
            }
            battleStage.getCombatEndButton().setState(duels.size()>0 ? 1 : 0);
            if (token.getCard().getBattlePlayer() instanceof LocalBattlePlayer) { battle.getRoundManager().getPossibilityAdvisor().refresh(token.getCard().getBattlePlayer(), battle); }
        }
    }

    public void cancelDuel(Token token) {
        duels.remove((FleetToken) token);
    }

    public boolean canReach(Token attacker, Token targetToken, Fleet targetFleet) {
        if (targetToken instanceof FleetToken && AbilityManager.hasAttribute(targetToken.getCard(), EffectType.GUARD)) {
            return true;
        }
        int enemyGuards = 0;
        for (Ship ship : targetFleet.getShips()) { //in-future: rework for more than 1v1 (ie. consider all enemy fleets)
            if (ship != null && AbilityManager.hasAttribute(ship.getToken().getCard(), EffectType.GUARD)) {
                enemyGuards++;
            }
        }
        if (enemyGuards == 0) {
            return true;
        } else {
            int reach = AbilityManager.getReach(attacker.getCard());
            return reach >= enemyGuards;
        }
    }

    //TACTICAL PHASE

    public void startTacticalPhase() {
        getBattleStage().disableCombatEnd();
        battle.getRoundManager().getPossibilityAdvisor().unMarkAll(battle.getWhoseTurn(), battle);
        if (duels.size() > 0) {
            playersA = playersToCombatPlayers(getBattle().getAllies(battle.getWhoseTurn()));
            playersD = playersToCombatPlayers(getBattle().getEnemies(battle.getWhoseTurn()));
            preparePlayers();
            tacticalPhase = true;
            setFSGlows(false);
            markFSGlows();
            System.out.println("Tactical Phase started.");
            if (getBattle().getBattleScreen().getBattleType() != BattleType.SIMULATION) {
                NotificationManager.getInstance().newNotification(Notification.NotificationType.MIDDLE, "TACTICAL PHASE", 3);
            }
            if (!(activePlayer.getBattlePlayer() instanceof Bot)) {
                if (battle.getRoundManager().getPossibilityAdvisor().getPossibilities(activePlayer.getBattlePlayer(), battle).size() > 0) {
                    combatMenu.addOK(activePlayer.getCombatButton());
                    activePlayer.getCombatButton().setExtraState(false);
                    battle.getRoundManager().getPossibilityAdvisor().refresh(activePlayer.getBattlePlayer(), battle);
                } else {
                    tacticalOK(activePlayer.getCombatButton());
                }
            } else {
                ((Bot) this.playersA[0].getBattlePlayer()).newCombatOK(this.playersA[0].getCombatButton());
            }
        } else {
            activePlayer = null;
            endCombat();
        }
    }

    void preparePlayers() {
        resetReadyStates(null, true); //battle.getWhoseTurn()
        activePlayer = playersA[0];
    }

    private void resetReadyStates(BattlePlayer invertedBattlePlayer, boolean first) {
        for (CombatPlayer combatPlayer : playersA) {
            combatPlayer.setReady(invertedBattlePlayer != null && combatPlayer.getBattlePlayer() == invertedBattlePlayer);
        }
        for (CombatPlayer combatPlayer : playersD) {
            combatPlayer.setReady(invertedBattlePlayer != null && combatPlayer.getBattlePlayer() == invertedBattlePlayer);
        }
    }

    private void setFSGlows(boolean picked) {
        for (CombatPlayer cp : playersA) {
            setFSGlowOnPlayer(cp.getBattlePlayer(), picked);
        }
        for (CombatPlayer cp : playersD) {
            setFSGlowOnPlayer(cp.getBattlePlayer(), picked);
        }
    }

    private void setFSGlowOnPlayer(BattlePlayer battlePlayer, boolean picked) {
        setFSGlowOnTokens(battlePlayer.getFleet().getFleetMenu().getFleetTokens(), picked);
    }

    private void setFSGlowOnTokens(Token[] tokens, boolean picked) {
        for (Token token : tokens) {
            if (token != null) {
                token.setPicked(picked);
            }
        }
    }

    private void markFSGlows() {
        for (Map.Entry<FleetToken, DuelManager.AttackInfo> entry : duels.entrySet()) {
            if (entry.getValue().getUpperStrike() != null) {
                entry.getValue().getUpperStrike().getToken().setPicked(true);
                if (entry.getValue().getUpperStrike().getToken() == entry.getValue().getDefender()) {
                    entry.getKey().setPicked(false);
                } else {
                    entry.getValue().getDefender().setPicked(false);
                }
            }
            /*boolean attFS = AbilityManager.hasAttribute(entry.getKey().getCard(), EffectType.FIRST_STRIKE);
            boolean defFS = AbilityManager.hasAttribute(entry.getValue().getDefender().getCard(), EffectType.FIRST_STRIKE);
            if (attFS) {
                entry.getKey().getCard().setPossible(true);
            }
            if (defFS) {
                entry.getValue().getDefender().getCard().setPossible(true);
            }*/
        }
    }

    void saveTactic(BattleCard battleCard, BattleCard target) {
        if (AbilityManager.upgradesFirstStrike(battleCard)) {
            saveUpperStrike(target);
            markFSGlows();
        }
        resetReadyStates(null, false);
    }

    private void saveUpperStrike(BattleCard target) {
        if (target.getToken() instanceof FleetToken) {
            BattleCard countered = null;
            for (Map.Entry<FleetToken, DuelManager.AttackInfo> entry : duels.entrySet()) {
                if (entry.getKey().getCard() == target) {
                    DuelManager.AttackInfo attackInfo = new DuelManager.AttackInfo(entry.getKey(), entry.getValue().getDefender(), target);
                    duels.put((FleetToken) target.getToken(), attackInfo);
                    countered = entry.getValue().getDefender().getCard();
                } else if (entry.getValue().getDefender().getCard() == target) {
                    DuelManager.AttackInfo attackInfo = new DuelManager.AttackInfo(entry.getKey(), entry.getValue().getDefender(), target);
                    duels.put(entry.getKey(), attackInfo);
                    countered = entry.getKey().getCard();
                }
            }
            if (countered != null) {
                if (countered.getToken() != null) { countered.getToken().setPicked(false); }
                for (Map.Entry<FleetToken, DuelManager.AttackInfo> entry : duels.entrySet()) {
                    if (entry.getValue().getUpperStrike() == countered) {
                        DuelManager.AttackInfo attackInfo = new DuelManager.AttackInfo(entry.getKey(), entry.getValue().getDefender(), null);
                        duels.put(entry.getKey(), attackInfo);
                    }
                }
            }
        }
    }

    public void tacticalOK(CombatOK combatOK) {
        combatOK.getDuelPlayer().setReady(true);
        combatMenu.removeOK(combatOK);
        combatOK.setExtraState(true);
        if (areAllReady()) {
            battle.getRoundManager().getPossibilityAdvisor().unMarkAll(activePlayer.getBattlePlayer(), battle);
            engage();
        } else {
            switchActivePlayer();
            if (activePlayer.getBattlePlayer() instanceof Bot) {
                ((Bot) activePlayer.getBattlePlayer()).newCombatOK(activePlayer.getCombatButton());
            } else {
                if (battle.getRoundManager().getPossibilityAdvisor().getPossibilities(activePlayer.getBattlePlayer(), battle).size() > 0) {
                    combatMenu.addOK(activePlayer.getCombatButton());
                    battle.getRoundManager().getPossibilityAdvisor().refresh(activePlayer.getBattlePlayer(), battle);
                } else {
                    tacticalOK(activePlayer.getCombatButton());
                }
            }
        }
    }

    private void switchActivePlayer() {
        boolean found = false;
        boolean done = false;
        for (CombatPlayer combatPlayer : playersA) {
            if (combatPlayer == activePlayer) {
                found = true;
            }
            if (found && combatPlayer != activePlayer) {
                activePlayer = combatPlayer;
                done = true;
            }
        }
        if (!done) {
            for (CombatPlayer combatPlayer : playersD) {
                if (combatPlayer == activePlayer) {
                    found = true;
                }
                if (found && combatPlayer != activePlayer) {
                    activePlayer = combatPlayer;
                    done = true;
                }
            }
            if (!done) {
                activePlayer = playersA[0];
            }
        }
    }

    private boolean areAllReady() {
        if (!isTacticalPhase()) {
            return false;
        }
        for (CombatPlayer player : playersA) {
            if (!player.isReady()) { return false; }
        }
        for (CombatPlayer player : playersD) {
            if (!player.isReady()) { return false; }
        }
        return true;
    }

    private void engage() {
        activePlayer = null;
        tacticalPhase = false;
        duelManager.launchDuels(this, duels);
    }

    void setDuelState(Map.Entry<FleetToken, DuelManager.AttackInfo> duel, byte state) {
        if (duels.containsKey(duel.getKey())) {
            duels.put(duel.getKey(), new DuelManager.AttackInfo(duel.getKey(), duel.getValue().getDefender(), duel.getValue().getUpperStrike(), state));
        }
    }

    void afterDuels() {
        for (CombatPlayer cp : playersA) {
            checkPlayerForAftermath(cp.getBattlePlayer());
            setFSGlowOnPlayer(cp.getBattlePlayer(), false);
        }
        for (CombatPlayer cp : playersD) {
            checkPlayerForAftermath(cp.getBattlePlayer());
            setFSGlowOnPlayer(cp.getBattlePlayer(), false);
        }
        endCombat();
    }

    public void checkPlayerForAftermath(BattlePlayer battlePlayer) {
        checkShipsForAftermath(battlePlayer.getFleet().getShips());
        refreshFleets(battlePlayer);
        if (battlePlayer.getMs().getHealth()<=0) {
            battlePlayer.getMs().death();
        }
    }

    private void refreshFleets(BattlePlayer battlePlayer) {
        battlePlayer.getFleet().checkForBlanks();
        battlePlayer.getFleet().centralizeShips();
        battlePlayer.getFleet().getFleetMenu().updateCoordinates(battlePlayer.getFleet().getFleetMenu().getFleetTokens());
    }

    private void checkShipsForAftermath(Ship[] ships) {
        for (Ship ship : ships) {
            if (ship != null) {
                ship.setDmgDoneThisBattle(0);
                if (ship.getHealth() <= 0 && !ship.isDead()) {
                    ship.deathInAfterMath();
                }
            }
        }
    }

    CombatPlayer playerToCombatPlayer(BattlePlayer battlePlayer) {
        return new CombatPlayer(battlePlayer);
    }

    CombatPlayer[] playersToCombatPlayers(BattlePlayer[] battlePlayers) {
        CombatPlayer[] cps = new CombatPlayer[battlePlayers.length];
        for (int i = 0; i < cps.length; i++) {
            cps[i] = new CombatPlayer(battlePlayers[i]);
        }
        return cps;
    }

    public static Map.Entry<FleetToken, DuelManager.AttackInfo> getDuel(Token token, TreeMap<FleetToken, DuelManager.AttackInfo> duelMap) {
        if (token != null) {
            for (Map.Entry<FleetToken, DuelManager.AttackInfo> entry : duelMap.entrySet()) {
                if (entry.getKey() == token || entry.getValue().getDefender() == token) {
                    return entry;
                }
            }
        }
        return null;
    }

    public static ArrayList<Token> getDuelOpponent(Token token, TreeMap<FleetToken, DuelManager.AttackInfo> duelMap) {
        if (token != null) {
            ArrayList<Token> opponents = new ArrayList<>();
            if (token instanceof FleetToken && duelMap.containsKey(token)) {
                opponents.add(duelMap.get(token).getDefender());
            } else {
                for (Map.Entry<FleetToken, DuelManager.AttackInfo> entry : duelMap.entrySet()) {
                    if (entry.getValue().getDefender() == token) {
                        opponents.add(entry.getKey());
                    }
                }
            }
            return opponents;
        }
        return null;
    }

    void setPlayersA_OK(int ix, CombatOK combatOK) {
        if (playersA[ix] != null) { playersA[ix].setCombatOK(combatOK); }
    }

    void setPlayersD_OK(int ix, CombatOK combatOK) {
        if (playersD[ix] != null) { playersD[ix].setCombatOK(combatOK); }
    }

    //FINISH

    public void endCombat() {
        battleStage.disableCombatEnd();
        active = false;
        tacticalPhase = false;
        System.out.println("Combat Phase ended.");
        battle.getRoundManager().afterCombat();
    }

    //Misc

    public boolean isActive() { return active; }

    public Battle getBattle() { return battle; }

    public DuelManager getDuelManager() { return duelManager; }

    public BattleStage getBattleStage() { return battleStage; }

    public void setBattleStage(BattleStage battleStage) { this.battleStage = battleStage; }

    public boolean isTacticalPhase() {
        return tacticalPhase;
    }

    public CombatMenu getCombatMenu() {
        return combatMenu;
    }

    public void setCombatMenu(CombatMenu combatMenu) {
        this.combatMenu = combatMenu;
    }

    public CombatPlayer[] getPlayersA() {
        return playersA;
    }

    public CombatPlayer[] getPlayersD() {
        return playersD;
    }

    public CombatPlayer getActivePlayer() {
        return activePlayer;
    }

    public TreeMap<FleetToken, DuelManager.AttackInfo> getDuels() {
        return duels;
    }

    public void setDuels(TreeMap<FleetToken, DuelManager.AttackInfo> duels) {
        this.duels = new TreeMap<>(duels);
    }
}
