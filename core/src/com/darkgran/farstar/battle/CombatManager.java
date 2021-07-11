package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.FleetToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.battle.players.abilities.EffectType;

import java.util.HashMap;

public abstract class CombatManager {
    private BattleStage battleStage; //must be set before RoundManager.launch() (see BattleScreen constructor)
    private final Battle battle;
    private final DuelManager duelManager;
    private boolean active = false;
    private boolean tacticalPhase = false;
    private HashMap<Token, Token> duels = new HashMap<>();
    private Card lastTactic;
    private CombatPlayer[] playersA;
    private CombatPlayer[] playersD;
    private CombatPlayer activePlayer;
    private CombatMenu combatMenu;

    public CombatManager(Battle battle, DuelManager duelManager) {
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
        if (token instanceof FleetToken) {
            ((FleetToken) token).resetPosition();
        }
        if (active && !tacticalPhase && !duelManager.isActive()) {
            if (targetToken == null) {
                duels.remove(token);
            } else if (token != targetToken) {
                if (canReach(token, targetToken, targetToken.getCard().getPlayer().getFleet())) {
                    duels.remove(token);
                    duels.put(token, targetToken);
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
            lastTactic = null;
            playersA = playersToCombatPlayers(getBattle().getAllies(battle.getWhoseTurn()));
            playersD = playersToCombatPlayers(getBattle().getEnemies(battle.getWhoseTurn()));
            preparePlayers();
            tacticalPhase = true;
            System.out.println("Tactical Phase started.");
            if (!(activePlayer.getPlayer() instanceof Bot)) {
                combatMenu.addOK(activePlayer.getDuelButton());
                battle.getRoundManager().getPossibilityAdvisor().refresh(activePlayer.getPlayer(), battle);
            }/* else {
            ((Bot) this.playersA[0].getPlayer()).newDuelOK(this.playersA[0].getDuelButton());
        }*/
        } else {
            activePlayer = null;
            endCombat();
        }
    }

    protected void preparePlayers() {
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
        lastTactic = card;
        resetReadyStates(null);
    }

    public void tacticalOK(CombatOK combatOK) {
        combatOK.getDuelPlayer().setReady(true);
        combatMenu.removeOK(combatOK);
        if (areAllReady()) {
            engage();
        } else {
            switchActivePlayer();
            if (activePlayer.getPlayer() instanceof Bot) {
                //((Bot) activePlayer.getPlayer()).newDuelOK(activePlayer.getDuelButton());
            } else {
                combatMenu.addOK(activePlayer.getDuelButton());
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

    public void afterDuels() {
        for (CombatPlayer cp : playersA) {
            checkForAftermath(cp.getPlayer().getFleet().getShips());
        }
        for (CombatPlayer cp : playersD) {
            checkForAftermath(cp.getPlayer().getFleet().getShips());
        }
        endCombat();
    }

    private void checkForAftermath(Ship[] ships) {
        for (Ship ship : ships) {
            if (ship != null && ship.getHealth()<=0) {
                ship.deathInAfterMath();
            }
        }
    }

    public CombatPlayer playerToCombatPlayer(Player player) {
        return new CombatPlayer(player);
    }

    public CombatPlayer[] playersToCombatPlayers(Player[] players) {
        CombatPlayer[] cps = new CombatPlayer[players.length];
        for (int i = 0; i < cps.length; i++) {
            cps[i] = new CombatPlayer(players[i]);
        }
        return cps;
    }

    public void setPlayersA_OK(int ix, CombatOK combatOK) {
        if (playersA[ix] != null) { playersA[ix].setDuelOK(combatOK); }
    }

    public void setPlayersD_OK(int ix, CombatOK combatOK) {
        if (playersA[ix] != null) { playersD[ix].setDuelOK(combatOK); }
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

    public void setTacticalPhase(boolean tacticalPhase) {
        this.tacticalPhase = tacticalPhase;
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
}
