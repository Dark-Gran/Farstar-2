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
    private final float duelDelay = 0.2f;
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

        //comparison used by Old Loop only (see iterateDuels())
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

    private ShotManager shotManager;
    private CombatManager combatManager;
    private boolean active = false;
    private Iterator<Map.Entry<FleetToken, AttackInfo>> it;
    private TreeMap<FleetToken, AttackInfo> duels;
    private final ArrayList<FleetToken> shooters = new ArrayList<>();
    private Iterator<FleetToken> shootIt;
    private Iterator<Token> opIt;

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


    void launchDuels(CombatManager combatManager, TreeMap<FleetToken, AttackInfo> duels, ShotManager shotManager) {
        this.shotManager = shotManager;
        this.duels = duels;
        shooters.clear();
        if (duels != null && duels.size() > 0) {
            this.combatManager = combatManager;
            this.active = true;
            System.out.println("Sorting and Launching Duels.");
            //Set<Map.Entry<FleetToken, AttackInfo>> duelSet = entriesSortedByValuesThenKeys(duels); //Old Loop ini (see iterateDuels())
            //it = duelSet.iterator();
            it = duels.entrySet().iterator();
            //delayAction(this::iterateDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay*2f);
            //New Loop
            while (it.hasNext()) { //Keys (attackers) first
                Map.Entry<FleetToken, AttackInfo> duel = it.next();
                if (!shooters.contains(duel.getKey())) {
                    shooters.add(duel.getKey());
                }
            }
            it = duels.entrySet().iterator();
            while (it.hasNext()) { //Defenders second
                Map.Entry<FleetToken, AttackInfo> duel = it.next();
                if (duel.getValue().getDefender() instanceof FleetToken && !shooters.contains((FleetToken) duel.getValue().getDefender())) {
                    shooters.add((FleetToken) duel.getValue().getDefender());
                }
            }
            //Sort based on first strikes etc
            shooters.sort((t1, t2) -> {
                Map.Entry<FleetToken, AttackInfo> duel1 = CombatManager.getDuel(t1, duels);
                Map.Entry<FleetToken, AttackInfo> duel2 = CombatManager.getDuel(t2, duels);
                boolean top1 = duel1.getValue().getUpperStrike() == t1.getCard();
                boolean top2 = duel2.getValue().getUpperStrike() == t2.getCard();
                boolean fs1 = AbilityManager.hasAttribute(t1.getCard(), EffectType.FIRST_STRIKE);
                boolean fs2 = AbilityManager.hasAttribute(t2.getCard(), EffectType.FIRST_STRIKE);
                if (top1 == top2 && fs1 == fs2) {
                    return t1.compareTo(t2);
                } else {
                    if (top1 || top2) {
                        return Boolean.compare(top2, top1);
                    } else {
                        return Boolean.compare(fs2, fs1);
                    }
                }
            });
            shootIt = shooters.iterator();
            delayAction(this::iterateShooters, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay*2f);
        } else {
            System.out.println("Invalid number of duels to launch (0 or null).");
            delayAction(this::afterDuels, duelDelay);
        }
    }

    private void afterDuels() {
        active = false;
        combatManager.afterDuels();
    }

    /**
     * OLD LOOP
     * - iterates through entire Duels instead of individual Shooters (see iterateShooters()): important for First Strike and similar (a matter of gameplay preferences)
     * - will be reused in the future or become Deprecated
     */
    private void iterateDuels() {
        if (it != null && it.hasNext()) {
            Map.Entry<FleetToken, AttackInfo> duel = it.next();
            combatManager.setDuelState(duel, (byte) 1);
            delayAction(() -> performDuel(duel), combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay);
        } else {
            //afterDuels();
            delayAction(this::afterDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay*2f);
        }
    }

    private void performDuel(Map.Entry<FleetToken, AttackInfo> duel) {
        exeBothSides(duel.getKey().getCard(), duel.getValue());
        combatManager.setDuelState(duel, (byte) 2);
        delayAction(this::iterateDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay);
    }

    private void exeBothSides(BattleCard att, AttackInfo attackInfo) {
        BattleCard def = attackInfo.getDefender().getCard();
        boolean attFS = !att.isMS() && AbilityManager.hasAttribute(att, EffectType.FIRST_STRIKE);
        boolean defFS = !def.isMS() && AbilityManager.hasAttribute(def, EffectType.FIRST_STRIKE);
        boolean attTop = attackInfo.getUpperStrike() == att;
        boolean defTop = attackInfo.getUpperStrike() == def;
        if (attTop || defTop) {
            if (exeOneSide(attTop ? att : def, attTop ? def : att, false)) {
                exeOneSide(attTop ? def : att, attTop ? att : def, true);
            }
        } else if (attFS != defFS) {
            if (attFS) {
                if (att.isKilledByFirstStrike() || exeOneSide(att, def, false)) {
                    if (!def.isKilledByFirstStrike()) { exeOneSide(def, att, true); }
                }
            } else {
                if (def.isKilledByFirstStrike() || exeOneSide(def, att, false)) {
                    if (!att.isKilledByFirstStrike()) { exeOneSide(att, def, true); }
                }
            }
        } else {
            if (!att.isKilledByFirstStrike()) { exeOneSide(att, def, false); }
            if (!def.isKilledByFirstStrike()) { exeOneSide(def, att, false); }
        }
        if (attFS || defFS || attTop || defTop) {
            if (att.getHealth()<=0) { att.setKilledByFirstStrike(true); }
            if (def.getHealth()<=0) { def.setKilledByFirstStrike(true); }
        }
    }

    /**
     * NEW LOOP
     * (iterates through all ships that have an opponent)
     */
    private void iterateShooters() {
        if (duels != null && duels.size() > 0 && shootIt != null && shootIt.hasNext()) {
            FleetToken shooter = shootIt.next();
            ArrayList<Token> opponents = CombatManager.getDuelOpponent(shooter, duels);
            opIt = opponents.iterator();
            delayAction(() -> iterateAllTargets(shooter), combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay);
        } else {
            //afterDuels();
            delayAction(this::afterDuels, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay*2f);
        }
    }

    private void iterateAllTargets(FleetToken shooter) {
        boolean sFS = AbilityManager.hasAttribute(shooter.getCard(), EffectType.FIRST_STRIKE);
        boolean sTop = CombatManager.getDuel(shooter, duels).getValue().getUpperStrike() == shooter.getCard();
        if (opIt.hasNext() && ((sTop || !shooter.getCard().isKilledByTopStrike()) && (sFS || !shooter.getCard().isKilledByFirstStrike()))) {
            Token opponent = opIt.next();
            shootOneTarget(shooter, opponent.getCard(), sTop, sFS);
        } else {
            delayAction(this::iterateShooters, combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay);
        }
    }

    private void shootOneTarget(FleetToken shooter, BattleCard def, boolean attTop, boolean attFS) {
        if (!exeOneSide(shooter.getCard(), def, false)) {
            if (attTop) {
                def.setKilledByTopStrike(true);
            } else if (attFS) {
                def.setKilledByFirstStrike(true);
            }
        }
        delayAction(() -> iterateAllTargets(shooter), combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay*1.5f);
    }

    //Shared (used by both loops)
    private boolean exeOneSide(BattleCard att, BattleCard def, boolean delayedAnimation) { //returns survival
        if (!att.isMS()) {
            int dmg = att.getCardInfo().getOffense();
            if (att instanceof Ship && BattleSettings.OUTNUMBERED_DEBUFF_ENABLED) {
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
                    shotManager.newAttack(att.getToken(), def.getToken(), att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), att.getCardInfo().getAnimatedShots());
                } else {
                    delayAction(() -> shotManager.newAttack(att.getToken(), def.getToken(), att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), att.getCardInfo().getAnimatedShots()), combatManager.getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION ? simDelay : duelDelay * 1.5f);
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
