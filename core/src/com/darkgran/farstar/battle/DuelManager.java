package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;
import com.darkgran.farstar.battle.gui.DuelOK;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.CardInfo;
import com.darkgran.farstar.battle.players.DuelPlayer;

public abstract class DuelManager {
    private CombatManager combatManager;
    private DuelMenu duelMenu;
    private boolean active = false;
    private boolean engaged = false;
    private Token attacker;
    private Token defender;
    private DuelPlayer[] playersA;
    private DuelPlayer[] playersD;

    public void launchDuel(CombatManager combatManager, Token attacker, Token defender, DuelPlayer[] playersA, DuelPlayer[] playersD) {
        if (!engaged) {
            active = true;
            this.combatManager = combatManager;
            this.attacker = attacker;
            this.defender = defender;
            this.playersA = playersA;
            this.playersD = playersD;
            preparePlayers();
            duelMenu.addCancel();
            duelMenu.addOK(this.playersA[0].getDuelButton());
        }
    }

    public void preparePlayers() {
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
            if (attacker != null && defender != null) {
                exeDuel(attacker.getCard().getCardInfo(), defender.getCard().getCardInfo());
            }
            endDuel();
        }
    }

    public void exeDuel(CardInfo att, CardInfo def) {
        att.setDefense(att.getDefense()-def.getOffense());
        def.setDefense(def.getDefense()-att.getOffense());
    }

    public void cancelDuel() {
        duelMenu.removeCancel();
        endDuel();
    }

    public void endDuel() {
        engaged = false;
        duelMenu.removeAllOKs();
        active = false;
        combatManager.afterDuel();
    }

    private void nextOK(DuelOK duelOK) {
        boolean next = false;
        boolean done = false;
        for (DuelPlayer player : playersA) {
            if (!player.isReady() && player == duelOK.getDuelPlayer() || next) {
                if (!next) {
                    player.setReady(true);
                    duelMenu.removeOK(duelOK);
                    next = true;
                } else {
                    duelMenu.addOK(player.getDuelButton());
                    done = true;
                    break;
                }
            }
        }
        if (!done) {
            for (DuelPlayer player : playersD) {
                if (!player.isReady() && player == duelOK.getDuelPlayer() || next) {
                    if (!next) {
                        player.setReady(true);
                        duelMenu.removeOK(duelOK);
                        next = true;
                    } else {
                        duelMenu.addOK(player.getDuelButton());
                        break;
                    }
                }
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

    private void changeReady(DuelPlayer whom, boolean toWhat) {
        for (DuelPlayer player : playersA) {
            if (player == whom) {
                player.setReady(toWhat);
                break;
            }
        }
        for (DuelPlayer player : playersD) {
            if (player == whom) {
                player.setReady(toWhat);
                break;
            }
        }
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


}
