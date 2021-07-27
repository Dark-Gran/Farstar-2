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
    private final float duelDelay = 0.3f;

    public static class AttackInfo implements Comparable<AttackInfo> {
        private final Token defender;
        private BattleCard upperStrike = null;
        private byte state = 0; //1: right-about-to attack, 2: attack done
        public AttackInfo(Token defender) {
            this.defender = defender;
        }
        public AttackInfo(Token defender, BattleCard upperStrike) {
            this.defender = defender;
            this.upperStrike = upperStrike;
        }
        public AttackInfo(Token defender, BattleCard upperStrike, byte state) {
            this.defender = defender;
            this.upperStrike = upperStrike;
            this.state = state;
        }
        public Token getDefender() { return defender; }
        public BattleCard getUpperStrike() { return upperStrike; }
        public void setUpperStrike(BattleCard upperStrike) { this.upperStrike = upperStrike; }
        public byte getState() { return state; }
        public void setState(byte state) { this.state = state; }

        @Override
        public int compareTo(@NotNull DuelManager.AttackInfo o) { //todo "natural" FirstStrike
            boolean thisFS = upperStrike != null;
            boolean oFS = o.upperStrike != null;
            return Boolean.compare(oFS, thisFS);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AttackInfo)) {
                return super.equals(obj);
            }
            boolean thisFS = upperStrike != null;
            boolean oFS = ((AttackInfo) obj).upperStrike != null;
            return Boolean.compare(oFS, thisFS) == 0;
        }
    }

    private CombatManager combatManager;
    private boolean active = false;
    private Iterator<Map.Entry<FleetToken, AttackInfo>> it;
    private ShotManager shotManager;

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
        if (duels != null && duels.size() > 0) {
            this.combatManager = combatManager;
            this.active = true;
            Set<Map.Entry<FleetToken, AttackInfo>> duelSet = entriesSortedByValuesThenKeys(duels);
            it = duelSet.iterator();
            System.out.println("Launching Duels.");
            delayAction(this::iterateDuels, 0.5f);
        } else {
            System.out.println("Invalid number of duels to launch (0 or null).");
            //delayAction(this::afterDuels, duelDelay);
        }
    }

    private void iterateDuels() { //loop
        if (it.hasNext()) {
            Map.Entry<FleetToken, AttackInfo> duel = it.next();
            combatManager.setDuelState(duel, (byte) 1);
            delayAction(() -> performDuel(duel), duelDelay);
        } else {
            //afterDuels();
            delayAction(this::afterDuels, 0.5f);
        }
    }

    private void performDuel(Map.Entry<FleetToken, AttackInfo> duel) {
        exeDuel(duel.getKey().getCard(), duel.getValue());
        combatManager.setDuelState(duel, (byte) 2);
        delayAction(this::iterateDuels, duelDelay);
    }

    private void afterDuels() {
        active = false;
        combatManager.afterDuels();
    }

    private void exeDuel(BattleCard att, AttackInfo attackInfo) { //todo reconsider FS "phases"
        BattleCard def = attackInfo.getDefender().getCard();
        boolean attFS;
        boolean defFS;
        if (def.isMS()) {
            attFS = AbilityManager.hasAttribute(att, EffectType.FIRST_STRIKE);
            defFS = false;
        } else if (attackInfo.getUpperStrike() == null) {
            attFS = AbilityManager.hasAttribute(att, EffectType.FIRST_STRIKE);
            defFS = AbilityManager.hasAttribute(def, EffectType.FIRST_STRIKE);
        } else {
            attFS = attackInfo.getUpperStrike() == att;
            defFS = !attFS;
        }
        boolean FSDuel = attFS || defFS;
        if (attFS != defFS) {
            if (attFS) {
                if (exeOneSide(att, def, false)) {
                    if (!def.isMS() && !def.isKilledByFirstStrike()) { exeOneSide(def, att, true); }
                }
            } else {
                if (def.isMS() || exeOneSide(def, att, false)) {
                    if (!att.isKilledByFirstStrike()) { exeOneSide(att, def, true); }
                }
            }
        } else {
            if (FSDuel || !att.isKilledByFirstStrike()) { exeOneSide(att, def, false); }
            if ((FSDuel || !def.isKilledByFirstStrike()) && !def.isMS()) { exeOneSide(def, att, false); }
        }
        if (FSDuel) {
            if (att.getHealth()<=0) { att.setKilledByFirstStrike(true); }
            if (def.getHealth()<=0) { def.setKilledByFirstStrike(true); }
        }
    }

    private boolean exeOneSide(BattleCard att, BattleCard def, boolean delayedAnimation) { //returns survival
        int dmg = att.getCardInfo().getOffense();
        if (att instanceof Ship && BattleSettings.OUTNUMBERED_DEBUFF_ENABLED) {
            Ship ship = (Ship) att;
            dmg -= ship.getDmgDoneThisBattle();
            ship.setDmgDoneThisBattle(ship.getDmgDoneThisBattle()+att.getCardInfo().getOffense());
            if (att.getCardInfo().getOffense() > def.getHealth()) {
                ship.setDmgDoneThisBattle(ship.getDmgDoneThisBattle()-(att.getCardInfo().getOffense()-def.getHealth()));
            }
            if (dmg < 0) { dmg = 0; }
        }
        if (dmg > 0) {
            dmg = getDmgAgainstShields(dmg, def.getHealth(), att.getCardInfo().getOffenseType(), def.getCardInfo().getDefenseType());
            if (!delayedAnimation) {
                shotManager.newAttack(att.getToken(), def.getToken(), att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), att.getCardInfo().getAnimatedShots());
            } else {
                delayAction(() -> shotManager.newAttack(att.getToken(), def.getToken(), att.getCardInfo().getOffense(), att.getCardInfo().getOffenseType(), att.getCardInfo().getAnimatedShots()), duelDelay * 1.5f);
            }
        }
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

    public boolean isActive() {
        return active;
    }

}
