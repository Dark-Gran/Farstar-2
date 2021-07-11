package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.TechType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DuelManager {
    private CombatManager combatManager;
    private HashMap<Token, Token> duels;
    private boolean active = false;
    private Set<Map.Entry<Token, Token>> duelSet;
    private Iterator<Map.Entry<Token, Token>> it;

    public void launchDuels(CombatManager combatManager, HashMap<Token, Token> duels) {
        if (duels != null && duels.size() > 0) {
            this.combatManager = combatManager;
            this.duels = duels;
            this.active = true;
            duelSet = duels.entrySet();
            it = duelSet.iterator();
            System.out.println("Launching Duels.");
            iterateDuels();
        } else {
            System.out.println("Invalid number of duels to launch (0 or null).");
            afterDuels();
        }
    }

    private void iterateDuels() { //loop
        if (it.hasNext()) {
            Map.Entry<Token, Token> duel = it.next();
            exeDuel(duel.getKey().getCard(), duel.getValue().getCard());
            it.remove();
            iterateDuels();
        } else {
            afterDuels();
        }
    }


    private void afterDuels() {
        active = false;
        combatManager.afterDuels();
    }

    private void exeDuel(Card att, Card def) {
        /*if (strikePriority != null) {
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
        } else {*/
            if (!exeOneSide(att, def)) { def.death(); }
            if (!def.isMS()) {
                if (!exeOneSide(def, att)) {
                    att.death();
                }
            }
        //}
    }

    /*private void iniStrikePriority(Card att, Card def) {
        boolean attShootsFirst = combatManager.getBattle().getAbilityManager().hasAttribute(att, EffectType.FIRST_STRIKE);
        boolean defShootsFirst = combatManager.getBattle().getAbilityManager().hasAttribute(def, EffectType.FIRST_STRIKE);
        if (attShootsFirst != defShootsFirst) {
            if (attShootsFirst) { strikePriority = att; }
            else { strikePriority = def; }
        } else {
            strikePriority = null;
        }
    }*/

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
