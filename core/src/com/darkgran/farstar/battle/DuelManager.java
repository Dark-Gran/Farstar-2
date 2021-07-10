package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;
import com.darkgran.farstar.battle.gui.DuelOK;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.Bot;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.DuelPlayer;
import com.darkgran.farstar.battle.players.cards.Ship;
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
            attacker.getCard().setInDuel(true);
            defender.getCard().setInDuel(true);
            this.playersA = playersA;
            this.playersD = playersD;
            preparePlayers();
            iniStrikePriority(this.attacker.getCard(), this.defender.getCard());
            lastTactic = null;
            activePlayer = this.playersA[0];
            if (!(this.playersA[0].getPlayer() instanceof Bot)) {
                duelMenu.addCancel();
                duelMenu.addOK(this.playersA[0].getDuelButton());
                this.combatManager.getBattle().getRoundManager().getPossibilityAdvisor().refresh(this.combatManager.getDuelManager().getActivePlayer().getPlayer(), this.combatManager.getBattle());
                setDuelGlows(true);
            } else {
                ((Bot) this.playersA[0].getPlayer()).newDuelOK(this.playersA[0].getDuelButton());
            }
        } else {
            System.out.println("- launchDuel() Error! ("+!engaged+" : "+(attacker!=null)+" : "+(defender!=null)+")");
        }
    }

    protected void setDuelGlows(boolean enable) {
        this.attacker.setGlowState(enable ? Token.GlowState.PICKED : Token.GlowState.DIM);
        this.defender.setGlowState(enable ? Token.GlowState.PICKED : Token.GlowState.DIM);
    }

    private void iniStrikePriority(Card att, Card def) {
        boolean attShootsFirst = combatManager.getBattle().getAbilityManager().hasAttribute(att, EffectType.FIRST_STRIKE);
        boolean defShootsFirst = combatManager.getBattle().getAbilityManager().hasAttribute(def, EffectType.FIRST_STRIKE);
        if (attShootsFirst != defShootsFirst) {
            if (attShootsFirst) { strikePriority = att; }
            else { strikePriority = def; }
        } else {
            strikePriority = null;
        }
    }

    void saveTactic(Card card, Card target) {
        lastTactic = card;
        resetAllPlayersReady();
        if (combatManager.getBattle().getAbilityManager().hasAttribute(target, EffectType.FIRST_STRIKE)) {
            strikePriority = target;
        }
    }

    protected void preparePlayers() { resetAllPlayersReady(); }

    void resetAllPlayersReady() {
        for (DuelPlayer player : playersA) { player.setReady(false); }
        for (DuelPlayer player : playersD) { player.setReady(false); }
    }

    public void OK(DuelOK duelOK) {
        duelMenu.removeCancel();
        nextOK(duelOK);
        if (areAllReady()) {
            engage();
        } else if (activePlayer.getPlayer() instanceof Bot) {
            ((Bot) activePlayer.getPlayer()).newDuelOK(activePlayer.getDuelButton());
        } else {
            combatManager.getBattle().getRoundManager().getPossibilityAdvisor().refresh(combatManager.getDuelManager().getActivePlayer().getPlayer(), combatManager.getBattle());
            setDuelGlows(true);
        }
    }

    private void engage() {
        if (!engaged) {
            engaged = true;
            if (attacker != null && defender != null) { exeDuel(attacker.getCard(), defender.getCard()); }
            endDuel();
        }
    }

    protected void exeDuel(Card att, Card def) {
        if (strikePriority != null) {
            if (strikePriority == att || def.isMS()) {
                if (!exeOneSide(att, def)) { def.death(); }
                else {
                    if (!def.isMS()) {
                        if (!exeOneSide(def, att)) {
                            att.death();
                        }
                    }
                }
            } else {
                if (!exeOneSide(def, att)) { att.death(); }
                else {
                    if (!exeOneSide(att, def)) { def.death(); }
                }
            }
        } else {
            if (!exeOneSide(att, def)) { def.death(); }
            if (!def.isMS()) {
                if (!exeOneSide(def, att)) {
                    att.death();
                }
            }
        }
        if (att instanceof Ship) { ((Ship) att).setFought(true); }
    }

    protected boolean exeOneSide(Card att, Card def) { //returns survival
        int dmg = getDmgAgainstShields(att.getCardInfo().getOffense(), def.getHealth(), att.getCardInfo().getOffenseType(), def.getCardInfo().getDefenseType());
        return def.receiveDMG(dmg);
    }

    public static int getDmgAgainstShields(int dmg, int health, TechType dmgType, TechType shieldType) {
        if (dmg <= health) {
            dmgType = noneToInferior(dmgType);
            shieldType = noneToInferior(shieldType);
            if (dmg != 0 && ((shieldType == TechType.SUPERIOR && dmgType != TechType.SUPERIOR) || (shieldType != TechType.INFERIOR && (dmgType == TechType.INFERIOR || dmgType == shieldType)))) {
                return 1;
            }
        }
        return dmg;
    }

    public static TechType noneToInferior(TechType techType) {
        if (techType == TechType.NONE) { techType = TechType.INFERIOR; }
        return techType;
    }

    public void cancelDuel() {
        if (!duelMenu.getCancelButton().isDisabled()) {
            setDuelGlows(false);
            duelMenu.removeCancel();
            endDuel();
        }
    }

    public void endDuel() {
        engaged = false;
        duelMenu.removeAllOKs();
        attacker.getCard().setInDuel(false);
        defender.getCard().setInDuel(false);
        active = false;
        setDuelGlows(false);
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

    public Token getOpponent(Token token) {
        if (token == attacker) {
            return defender;
        } else {
            return attacker;
        }
    }

    public Token getOpponent(Card card) {
        if (card == attacker.getCard()) {
            return defender;
        } else {
            return attacker;
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

    public DuelPlayer getActivePlayer() { return activePlayer; }

    public Card getLastTactic() { return lastTactic; }

    public Card getStrikePriority() { return strikePriority; }

}
