package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.battle.players.abilities.EffectType;

import java.util.HashMap;
import java.util.Map;

public abstract class CombatManager {
    private BattleStage battleStage; //must be set before RoundManager.launch() (see BattleScreen constructor)
    private final Battle battle;
    private final DuelManager duelManager;
    private boolean active = false;
    private boolean tacticalPhase = false;
    private HashMap<Token, DuelManager.AttackInfo> duels = new HashMap<>();
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
            }
        }
    }

    public void processDrop(Token token, Token targetToken) {
        if (active && !tacticalPhase && !duelManager.isActive() && token.getCard().getPlayer() == battle.getWhoseTurn()) {
            if (targetToken == null) {
                duels.remove(token);
            } else if (token != targetToken) {
                if (canReach(token, targetToken, targetToken.getCard().getPlayer().getFleet())) {
                    duels.remove(token);
                    duels.put(token, new DuelManager.AttackInfo(targetToken));
                }
            }
        }
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
        if (duels.size() > 0) {
            playersA = playersToCombatPlayers(getBattle().getAllies(battle.getWhoseTurn()));
            playersD = playersToCombatPlayers(getBattle().getEnemies(battle.getWhoseTurn()));
            preparePlayers();
            tacticalPhase = true;
            System.out.println("Tactical Phase started.");
            if (!(activePlayer.getPlayer() instanceof Bot)) {
                combatMenu.addOK(activePlayer.getCombatButton());
                battle.getRoundManager().getPossibilityAdvisor().refresh(activePlayer.getPlayer(), battle);
            } else {
                ((Bot) this.playersA[0].getPlayer()).newCombatOK(this.playersA[0].getCombatButton());
            }
        } else {
            activePlayer = null;
            endCombat();
        }
    }

    void preparePlayers() {
        resetReadyStates(null); //battle.getWhoseTurn()
        activePlayer = playersA[0];
    }

    private void resetReadyStates(Player invertedPlayer) {
        for (CombatPlayer combatPlayer : playersA) {
            combatPlayer.setReady(invertedPlayer != null && combatPlayer.getPlayer() == invertedPlayer);
        }
        for (CombatPlayer combatPlayer : playersD) {
            combatPlayer.setReady(invertedPlayer != null && combatPlayer.getPlayer() == invertedPlayer);
        }
    }

    void saveTactic(Card card, Card target) {
        if (AbilityManager.upgradesFirstStrike(card)) {
            saveUpperStrike(target);
        }
        resetReadyStates(null);
    }

    private void saveUpperStrike(Card target) {
        if (duels.containsKey(target.getToken())) {
            DuelManager.AttackInfo attackInfo = new DuelManager.AttackInfo(duels.get(target.getToken()).getDefender(), target);
            duels.put(target.getToken(), attackInfo);
        } else {
            for (Map.Entry<Token, DuelManager.AttackInfo> entry : duels.entrySet()) {
                if (entry.getValue().getDefender().getCard() == target) {
                    Token att = entry.getKey();
                    DuelManager.AttackInfo attackInfo = new DuelManager.AttackInfo(entry.getValue().getDefender(), target);
                    duels.put(att, attackInfo);
                    break;
                }
            }
        }
    }

    public void tacticalOK(CombatOK combatOK) {
        combatOK.getDuelPlayer().setReady(true);
        combatMenu.removeOK(combatOK);
        if (areAllReady()) {
            engage();
        } else {
            switchActivePlayer();
            if (activePlayer.getPlayer() instanceof Bot) {
                ((Bot) activePlayer.getPlayer()).newCombatOK(activePlayer.getCombatButton());
            } else {
                combatMenu.addOK(activePlayer.getCombatButton());
                battle.getRoundManager().getPossibilityAdvisor().refresh(activePlayer.getPlayer(), battle);
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

    void setDuelState(Map.Entry<Token, DuelManager.AttackInfo> duel, byte state) {
        if (duels.containsKey(duel.getKey())) {
            duels.put(duel.getKey(), new DuelManager.AttackInfo(duel.getValue().getDefender(), duel.getValue().getUpperStrike(), state));
        }
    }

    void afterDuels() {
        for (CombatPlayer cp : playersA) {
            checkPlayerForAftermath(cp.getPlayer());
        }
        for (CombatPlayer cp : playersD) {
            checkPlayerForAftermath(cp.getPlayer());
        }
        endCombat();
    }

    private void checkPlayerForAftermath(Player player) {
        checkShipsForAftermath(player.getFleet().getShips());
        refreshFleets(player);
        if (player.getMs().getHealth()<=0) {
            player.getMs().death();
        }
    }

    private void refreshFleets(Player player) {
        player.getFleet().checkForBlanks();
        player.getFleet().centralizeShips();
        player.getFleet().getFleetMenu().updateCoordinates(player.getFleet().getFleetMenu().getFleetTokens());
    }

    private void checkShipsForAftermath(Ship[] ships) {
        for (Ship ship : ships) {
            if (ship != null && ship.getHealth()<=0) {
                ship.deathInAfterMath();
            }
        }
    }

    CombatPlayer playerToCombatPlayer(Player player) {
        return new CombatPlayer(player);
    }

    CombatPlayer[] playersToCombatPlayers(Player[] players) {
        CombatPlayer[] cps = new CombatPlayer[players.length];
        for (int i = 0; i < cps.length; i++) {
            cps[i] = new CombatPlayer(players[i]);
        }
        return cps;
    }

    public Map.Entry<Token, DuelManager.AttackInfo> getDuel(Token token) {
        if (token != null) {
            for (Map.Entry<Token, DuelManager.AttackInfo> entry : duels.entrySet()) {
                if (entry.getKey() == token || entry.getValue().getDefender() == token) {
                    return entry;
                }
            }
        }
        return null;
    }

    public Token getDuelOpponent(Token token) {
        if (token != null) {
            if (duels.containsKey(token)) {
                return duels.get(token).getDefender();
            } else {
                for (Map.Entry<Token, DuelManager.AttackInfo> entry : duels.entrySet()) {
                    if (entry.getValue().getDefender() == token) {
                        return entry.getKey();
                    }
                }
            }
        }
        return null;
    }

    void setPlayersA_OK(int ix, CombatOK combatOK) {
        if (playersA[ix] != null) { playersA[ix].setDuelOK(combatOK); }
    }

    void setPlayersD_OK(int ix, CombatOK combatOK) {
        if (playersD[ix] != null) { playersD[ix].setDuelOK(combatOK); }
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

    public HashMap<Token, DuelManager.AttackInfo> getDuels() {
        return duels;
    }

    public void setDuels(HashMap<Token, DuelManager.AttackInfo> duels) {
        this.duels = duels;
    }
}
