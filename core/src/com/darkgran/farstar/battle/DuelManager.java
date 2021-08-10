package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.battle.players.Ship;
import com.darkgran.farstar.gui.battlegui.ShotManager;
import com.darkgran.farstar.gui.tokens.FleetToken;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.cards.EffectType;
import com.darkgran.farstar.cards.TechType;
import com.darkgran.farstar.util.Delayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class DuelManager implements Delayer {
    private final float duelDelay = 0.5f;
    private final float simDelay = 0.1f;

    public static class AttackInfo implements Comparable<AttackInfo> {
        private final Token attacker;
        private final Token defender;
        private BattleCard upperStrike = null;
        private byte state = 0; //1: right-about-to attack, 2: attack done
        public AttackInfo(Token attacker, Token defender) {
            this.attacker = attacker;
            this.defender = defender;
        }
        public AttackInfo(Token attacker, Token defender, BattleCard upperStrike) {
            this.attacker = attacker;
            this.defender = defender;
            this.upperStrike = upperStrike;
        }
        public AttackInfo(Token attacker, Token defender, BattleCard upperStrike, byte state) {
            this.attacker = attacker;
            this.defender = defender;
            this.upperStrike = upperStrike;
            this.state = state;
        }
        public Token getAttacker() { return attacker; }
        public Token getDefender() { return defender; }
        public BattleCard getUpperStrike() { return upperStrike; }
        public void setUpperStrike(BattleCard upperStrike) { this.upperStrike = upperStrike; }
        public byte getState() { return state; }
        public void setState(byte state) { this.state = state; }

        @Override
        public int compareTo(@NotNull DuelManager.AttackInfo o) {
            boolean thisTopStrike = upperStrike != null;
            boolean oTopStrike = o.upperStrike != null;
            if (thisTopStrike != oTopStrike) {
                return Boolean.compare(oTopStrike, thisTopStrike);
            }
            boolean thisFS = AbilityManager.hasAttribute(this.attacker.getCard(), EffectType.FIRST_STRIKE) || AbilityManager.hasAttribute(this.defender.getCard(), EffectType.FIRST_STRIKE);
            boolean oFS = AbilityManager.hasAttribute(o.attacker.getCard(), EffectType.FIRST_STRIKE) || AbilityManager.hasAttribute(o.defender.getCard(), EffectType.FIRST_STRIKE);
            return Boolean.compare(oFS, thisFS);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AttackInfo)) {
                return super.equals(obj);
            }
            boolean thisTopStrike = upperStrike != null;
            boolean oTopStrike = ((AttackInfo) obj).upperStrike != null;
            boolean thisFS = AbilityManager.hasAttribute(this.attacker.getCard(), EffectType.FIRST_STRIKE) || AbilityManager.hasAttribute(this.defender.getCard(), EffectType.FIRST_STRIKE);
            boolean oFS = AbilityManager.hasAttribute(((AttackInfo) obj).attacker.getCard(), EffectType.FIRST_STRIKE) || AbilityManager.hasAttribute(((AttackInfo) obj).defender.getCard(), EffectType.FIRST_STRIKE);
            return Boolean.compare(oTopStrike, thisTopStrike) == 0 && Boolean.compare(oFS, thisFS) == 0;
        }

        @Override
        public int hashCode() {
            boolean thisTopStrike = upperStrike != null;
            boolean thisFS = AbilityManager.hasAttribute(this.attacker.getCard(), EffectType.FIRST_STRIKE) || AbilityManager.hasAttribute(this.defender.getCard(), EffectType.FIRST_STRIKE);
            return Objects.hash(thisTopStrike, thisFS);
        }
    }

    private CombatManager combatManager;
    private boolean active = false;
    private Iterator<Map.Entry<FleetToken, AttackInfo>> it;
    TreeMap<FleetToken, AttackInfo> duels;
    private byte phase; //0. Top-Strikes 1. First-Strikes 2. Regular

    //used by Old Loop (see iterateDuels())
    static <K extends Comparable<? super K>,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValuesThenKeys(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<>(
                (e1, e2) -> {
                    if (!e1.getValue().equals(e2.getValue())) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                    return e1.getKey().compareTo(e2.getKey());
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    //in-future: (only "possibly", depends on desired gameplay) if a ship is facing more attackers, allow attacking/defending player to decide order (matters in certain situations because of the colors; for now, priority is always left>right)
    void launchDuels(CombatManager combatManager, TreeMap<FleetToken, AttackInfo> duels) {
        this.duels = duels;
        if (duels != null && duels.size() > 0) {
            this.combatManager = combatManager;
            this.active = true;
            System.out.println("Sorting and Launching Duels.");
            //Set<Map.Entry<FleetToken, AttackInfo>> duelSet = entriesSortedByValuesThenKeys(duels);
            //it = duelSet.iterator();
            phase = 0;
            it = duels.entrySet().iterator();
            delayAction(this::iterateDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay*2);
        } else {
            System.out.println("Invalid number of duels to launch (0 or null).");
            delayAction(this::afterDuels, duelDelay);
        }
    }

    private void afterDuels() {
        active = false;
        combatManager.afterDuels();
    }

    private void iterateDuels() {
        if (it != null && it.hasNext()) {
            Map.Entry<FleetToken, AttackInfo> duel = it.next();
            combatManager.setDuelState(duel, (byte) 1);
            performDuel(duel, phase);
        } else {
            if (phase >= 2) {
                //afterDuels();
                delayAction(this::afterDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay);
            } else {
                phase++;
                it = duels.entrySet().iterator();
                iterateDuels();
            }
        }
    }

    private void performDuel(Map.Entry<FleetToken, AttackInfo> duel, byte phase) {
        BattleCard att = duel.getKey().getCard();
        BattleCard def = duel.getValue().getDefender().getCard();
        boolean attFS = AbilityManager.hasAttribute(att, EffectType.FIRST_STRIKE);
        boolean defFS = AbilityManager.hasAttribute(def, EffectType.FIRST_STRIKE);
        boolean attTop = duel.getValue().getUpperStrike() == att;
        boolean defTop = duel.getValue().getUpperStrike() == def;
        boolean madeShots = false;
        switch (phase) {
            case 0:
                if (attTop && !att.isMS()) {
                    madeShots = true;
                    if (!exeOneSide(att, def, false)) {
                        def.setKilledByTopStrike(true);
                    }
                }
                if (defTop && !def.isMS()) {
                    madeShots = true;
                    if (!exeOneSide(def, att, false)) {
                        att.setKilledByTopStrike(true);
                    }
                }
                break;
            case 1:
                if (!attTop && attFS && !att.isKilledByTopStrike() && !att.isMS()) {
                    madeShots = true;
                    if (!exeOneSide(att, def, false)) {
                        def.setKilledByFirstStrike(true);
                    }
                }
                if (!defTop && defFS && !def.isKilledByTopStrike() && !def.isMS()) {
                    madeShots = true;
                    if (!exeOneSide(def, att, false)) {
                        att.setKilledByFirstStrike(true);
                    }
                }
                break;
            case 2:
                if (!attTop && !attFS && !att.isKilledByTopStrike() && !att.isKilledByFirstStrike() && !att.isMS()) {
                    madeShots = true;
                    exeOneSide(att, def, false);
                }
                if (!defTop && !defFS && !def.isKilledByTopStrike() && !def.isKilledByFirstStrike() && !def.isMS()) {
                    madeShots = true;
                    exeOneSide(def, att, false);
                }
                break;
        }
        if (madeShots) {
            delayAction(this::iterateDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay);
        } else {
            iterateDuels();
        }
    }

    private boolean exeOneSide(BattleCard att, BattleCard def, boolean delayedAnimation) { //returns survival
        if (!att.isMS()) {
            int dmg = att.getCardInfo().getOffense();
            if (att instanceof Ship && BattleSettings.getInstance().OUTNUMBERED_DEBUFF_ENABLED) {
                Ship ship = (Ship) att;
                dmg -= ship.getDmgDoneThisBattle();
                ship.setDmgDoneThisBattle(ship.getDmgDoneThisBattle() + att.getCardInfo().getOffense());
                if (att.getCardInfo().getOffense() > def.getHealth()) {
                    ship.setDmgDoneThisBattle(ship.getDmgDoneThisBattle() - (att.getCardInfo().getOffense() - def.getHealth()));
                }
                if (dmg < 0) {
                    dmg = 0;
                }
            }
            if (dmg > 0) {
                dmg = getDmgAgainstShields(dmg, def.getHealth(), att.getCardInfo().getOffenseType(), def.getCardInfo().getDefenseType());
                if (!delayedAnimation) {
                    ShotManager.getInstance().newAttack(att.getToken(), def.getToken(), att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), att.getCardInfo().getAnimatedShots());
                } else {
                    delayAction(() -> ShotManager.getInstance().newAttack(att.getToken(), def.getToken(), att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), att.getCardInfo().getAnimatedShots()), combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay * 1.5f);
                }
            }
            return def.receiveDMG(dmg);
        }
        return true;
    }

    public static int getDmgAgainstShields(int dmg, int health, TechType dmgType, TechType shieldType) {
        if (dmg <= health) {
            dmgType = TechType.noneToInferior(dmgType);
            shieldType = TechType.noneToInferior(shieldType);
            if (dmg != 0 && ((shieldType == TechType.SUPERIOR && dmgType != TechType.SUPERIOR) || (shieldType != TechType.INFERIOR && (dmgType == TechType.INFERIOR || dmgType == shieldType)))) {
                return 1;
            }
        }
        return dmg;
    }

    public boolean isActive() {
        return active;
    }

}
