package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;
import com.darkgran.farstar.battle.gui.DuelOK;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.DuelPlayer;
import com.darkgran.farstar.battle.players.Ship;
import com.darkgran.farstar.battle.players.TechType;
import com.darkgran.farstar.battle.players.abilities.EffectType;

public abstract class DuelManager {
    private CombatManager combatManager;
    private DuelMenu duelMenu;
    private boolean active = false;
    private boolean engaged = false;
    private Token attacker;
    private Token defender;
    private DuelPlayer[] playersA;
    private DuelPlayer[] playersD;
    private Card lastTactic;
    private DuelPlayer activePlayer;
    private Card strikePriority;

    public void launchDuel(CombatManager combatManager, Token attacker, Token defender, DuelPlayer[] playersA, DuelPlayer[] playersD) {
        if (!engaged && attacker != null && defender != null) {
            combatManager.getBattleStage().disableCombatEnd();
            active = true;
            this.combatManager = combatManager;
            this.attacker = attacker;
            this.defender = defender;
            attacker.setInDuel(true);
            defender.setInDuel(true);
            this.playersA = playersA;
            this.playersD = playersD;
            preparePlayers();
            iniStrikePriority(this.attacker.getCard(), this.defender.getCard());
            lastTactic = null;
            duelMenu.addCancel();
            duelMenu.addOK(this.playersA[0].getDuelButton());
            activePlayer = this.playersA[0];
        }
    }

    private void iniStrikePriority(Card att, Card def) {
        boolean attShootsFirst = AbilityManager.hasAttribute(att, EffectType.FIRST_STRIKE);
        boolean defShootsFirst = AbilityManager.hasAttribute(def, EffectType.FIRST_STRIKE);
        if (attShootsFirst != defShootsFirst) {
            if (attShootsFirst) { strikePriority = att; }
            else { strikePriority = def; }
        } else {
            strikePriority = null;
        }
    }

    public void saveTactic(Card card, Card target) {
        lastTactic = card;
        resetAllPlayersReady();
        if (AbilityManager.hasAttribute(target, EffectType.FIRST_STRIKE)) {
            strikePriority = target;
        }
    }

    public void preparePlayers() { resetAllPlayersReady(); }

    public void resetAllPlayersReady() {
        for (DuelPlayer player : playersA) { player.setReady(false); }
        for (DuelPlayer player : playersD) { player.setReady(false); }
    }

    public void OK(DuelOK duelOK) {
        duelMenu.removeCancel();
        nextOK(duelOK);
        if (areAllReady()) { engage(); }
    }

    private void engage() {
        if (!engaged) {
            engaged = true;
            if (attacker != null && defender != null) { exeDuel(attacker.getCard(), defender.getCard()); }
            endDuel();
        }
    }

    public void exeDuel(Card att, Card def) {
        if (strikePriority != null) {
            if (strikePriority == att) {
                if (!exeOneSide(att, def)) { def.death(); }
                else {
                    if (!exeOneSide(def, att)) { att.death(); }
                }
            } else {
                if (!exeOneSide(def, att)) { att.death(); }
                else {
                    if (!exeOneSide(att, def)) { def.death(); }
                }
            }
        } else {
            if (!exeOneSide(att, def)) { def.death(); }
            if (!exeOneSide(def, att)) { att.death(); }
        }
        if (att instanceof Ship) { ((Ship) att).setFought(true); }
    }

    public static boolean exeOneSide(Card att, Card def) { //returns survival
        int dmg = getDmgAgainstShields(att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), def.getCardInfo().getDefenseType());
        return def.receiveDMG(dmg);
    }

    public static int getDmgAgainstShields(int dmg, TechType dmgType, TechType shieldType) {
        dmgType = noneToInferior(dmgType);
        shieldType = noneToInferior(shieldType);
        if (dmg != 0 && ((shieldType == TechType.SUPERIOR && dmgType != TechType.SUPERIOR) || (shieldType != TechType.INFERIOR && (dmgType == TechType.INFERIOR || dmgType == shieldType)))) {
            return 1;
        }
        return dmg;
    }

    private static TechType noneToInferior(TechType techType) {
        if (techType == TechType.NONE) { techType = TechType.INFERIOR; }
        return techType;
    }

    public void cancelDuel() {
        duelMenu.removeCancel();
        endDuel();
    }

    public void endDuel() {
        engaged = false;
        duelMenu.removeAllOKs();
        attacker.setInDuel(false);
        defender.setInDuel(false);
        active = false;
        combatManager.afterDuel();
    }

    private void nextOK(DuelOK duelOK) {
        boolean next = false;
        int i;
        for (i = 0; i < playersA.length && !next; i++) {
            if (playersA[i] == activePlayer) {
                playersA[i].setReady(true);
                duelMenu.removeOK(duelOK);
                next = true;
            }
        }
        if (next) {
            if (i+1 < playersA.length) {
                duelMenu.addOK(playersA[i+1].getDuelButton());
                activePlayer = playersA[i+1];
            } else {
                duelMenu.addOK(playersD[0].getDuelButton());
                activePlayer = playersD[0];
            }
        } else {
            for (i = 0; i < playersD.length && !next; i++) {
                if (playersD[i] == activePlayer) {
                    playersD[i].setReady(true);
                    duelMenu.removeOK(duelOK);
                }
            }
            if (i+1 < playersD.length) {
                duelMenu.addOK(playersD[i+1].getDuelButton());
                activePlayer = playersD[i+1];
            } else {
                duelMenu.addOK(playersA[0].getDuelButton());
                activePlayer = playersA[0];
            }
        }
    }

    private boolean areAllReady() {
        for (DuelPlayer player : playersA) {
            if (!player.isReady()) { return false; }
        }
        for (DuelPlayer player : playersD) {
            if (!player.isReady()) { return false; }
        }
        return true;
    }

    public void setPlayersA_OK(int ix, DuelOK duelOK) {
        if (playersA[ix] != null) { playersA[ix].setDuelOK(duelOK); }
    }

    public void setPlayersD_OK(int ix, DuelOK duelOK) {
        if (playersA[ix] != null) { playersD[ix].setDuelOK(duelOK); }
    }

    public void receiveDuelMenu(DuelMenu duelMenu) { this.duelMenu = duelMenu; }

    public DuelMenu getDuelMenu() { return duelMenu; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public Token getAttacker() { return attacker; }

    public Token getDefender() { return defender; }

    public DuelPlayer[] getPlayersA() { return playersA; }

    public DuelPlayer[] getPlayersD() { return playersD; }

    public DuelPlayer getActivePlayer() { return activePlayer; }

    public Card getLastTactic() { return lastTactic; }


}
