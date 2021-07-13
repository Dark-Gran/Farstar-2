package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.EffectType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.TechType;
import com.darkgran.farstar.util.Delayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DuelManager implements Delayer {
    public static class AttackInfo {
        private final Token defender;
        private Card upperStrike = null;
        private byte state = 0;
        public AttackInfo(Token defender) {
            this.defender = defender;
        }
        public AttackInfo(Token defender, Card upperStrike) {
            this.defender = defender;
            this.upperStrike = upperStrike;
        }
        public AttackInfo(Token defender, Card upperStrike, byte state) {
            this.defender = defender;
            this.upperStrike = upperStrike;
            this.state = state;
        }
        public Token getDefender() { return defender; }
        public Card getUpperStrike() { return upperStrike; }
        public void setUpperStrike(Card upperStrike) { this.upperStrike = upperStrike; }
        public byte getState() { return state; }
        public void setState(byte state) { this.state = state; }
    }
    private CombatManager combatManager;
    private boolean active = false;
    private Set<Map.Entry<Token, AttackInfo>> duelSet;
    private Iterator<Map.Entry<Token, AttackInfo>> it;
    private final float duelDelay = 0.4f;

    void launchDuels(CombatManager combatManager, HashMap<Token, AttackInfo> duels) {
        if (duels != null && duels.size() > 0) {
            this.combatManager = combatManager;
            this.active = true;
            duelSet = duels.entrySet();
            it = duelSet.iterator();
            System.out.println("Launching Duels.");
            delayAction(this::iterateDuels, duelDelay);
        } else {
            System.out.println("Invalid number of duels to launch (0 or null).");
            //delayAction(this::afterDuels, duelDelay);
        }
    }

    private void iterateDuels() { //loop
        if (it.hasNext()) {
            Map.Entry<Token, AttackInfo> duel = it.next();
            combatManager.setDuelState(duel, (byte) 1);
            delayAction(()->performDuel(duel), duelDelay);
        } else {
            afterDuels();
            //delayAction(this::afterDuels, duelDelay);
        }
    }

    private void performDuel(Map.Entry<Token, AttackInfo> duel) {
        exeDuel(duel.getKey().getCard(), duel.getValue());
        combatManager.setDuelState(duel, (byte) 2);
        delayAction(this::iterateDuels, duelDelay);
    }

    private void afterDuels() {
        active = false;
        combatManager.afterDuels();
    }

    private void exeDuel(Card att, AttackInfo attackInfo) {
        Card def = attackInfo.getDefender().getCard();
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
        if (attFS != defFS) {
            if (attFS) {
                if (exeOneSide(att, def)) {
                    if (!def.isMS()) { exeOneSide(def, att); }
                }
            } else {
                if (def.isMS() || exeOneSide(def, att)) {
                    exeOneSide(att, def);
                }
            }
        } else {
            exeOneSide(att, def);
            if (!def.isMS()) { exeOneSide(def, att); }
        }
    }

    private boolean exeOneSide(Card att, Card def) { //returns survival
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

    public boolean isActive() {
        return active;
    }

}
