package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.cards.*;
import com.darkgran.farstar.gui.tokens.Token;

import java.util.ArrayList;

public class BattleCard extends Card {
    private BattlePlayer battlePlayer;
    private ArrayList<Effect> effects = new ArrayList<>();
    private ArrayList<AbilityRecord> history = new ArrayList<>();
    private boolean used; //for AbilityStarter.USE
    private int damage = 0;
    private boolean killedByFirstStrike;
    private boolean killedByTopStrike;

    public BattleCard(CardInfo cardInfo, CardInfo originalInfo, BattlePlayer battlePlayer) {
        super(cardInfo, originalInfo);
        this.battlePlayer = battlePlayer;
    }

    public BattleCard(CardInfo cardInfo, BattlePlayer battlePlayer) {
        super(cardInfo);
        this.battlePlayer = battlePlayer;
    }

    public BattleCard() {
        super();
        battlePlayer = null;
    }

    public BattleCard(int id) {
        super(id);
        battlePlayer = null;
    }

    public void reset() {
        setDamage(0);
        setUsed(false);
        getEffects().clear();
        getHistory().clear();
        setKilledByTopStrike(false);
        setKilledByFirstStrike(false);
    }

    @Override
    public void refreshToken(boolean def, boolean off, boolean abi) {
        if (getToken() != null) {
            if (getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom() != null && this == getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().getCard()) {
                getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().setup(this, getToken().getTokenType(), getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().getTargetXY());
            }
            if (abi) { getToken().getTokenPrice().update(); }
            if (def) { getToken().getTokenDefense().update(); }
            if (off) { getToken().getTokenOffense().update(); }
        }
    }

    @Override
    public void setPossible(boolean possible) {
        super.setPossible(possible);
        if (getToken() != null && !getToken().isPicked()) { getToken().setGlowState(possible ? Token.GlowState.POSSIBLE : Token.GlowState.DIM); }
        if (getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom() != null && this == getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().getCard()) {
            getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().setGlowState(getToken().getGlowState());
        }
    }

    public boolean receiveDMG(int dmg) { //returns survival
        damage += dmg;
        refreshToken(true, false, false);
        return getHealth() > 0;
    }

    public void repairDMG(int dmg) {
        damage -= dmg;
        if (damage < 0) { damage = 0; }
        refreshToken(true, false, false);
    }

    public int getHealth() { return getCardInfo().getDefense()-damage; }

    public void tickEffects(AbilityManager abilityManager) { //does not remove
        for (Effect effect : effects) {
            if (effect.getDuration() > 0) {
                effect.setDuration(effect.getDuration() - 1);
            }
        }
    }

    public void checkEffects(AbilityManager abilityManager) { //does not tick
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect.getDuration() == 0) {
                if (abilityManager.executeEffect(this, effect, true, null)) {
                    effects.remove(effect);
                    i--;
                }
            }
        }
        refreshToken(false, false, true);
    }

    public void tacticTrigger(AbilityManager abilityManager) { //"Whenever You play Tactic"
        for (AbilityInfo abilityInfo : getCardInfo().getAbilities()) {
            if (abilityInfo.getStarter() == AbilityStarter.TACTIC) {
                abilityManager.playAbility(this.getToken(), null, abilityInfo, null);
            }
        }
    }

    public void death() { }

    public void addToHistory (BattleCard battleCard, AbilityInfo ability) {
        history.add(new AbilityRecord(battleCard, ability));
    }

    public void addToEffects (Effect effect) {
        effects.add(effect);
        refreshToken(true, true, true);
    }

    public boolean isDamaged() { return damage > 0; }

    public boolean hasDamagedWeapons() { return getCardInfo().getOffense() < getOriginalInfo().getOffense(); }

    public boolean hasUpgradedShields() { return getCardInfo().getDefense() > getOriginalInfo().getDefense(); }

    public boolean hasUpgradedWeapons() { return getCardInfo().getOffense() > getOriginalInfo().getOffense(); }

    public ArrayList<Effect> getEffects() { return effects; }

    public ArrayList<AbilityRecord> getHistory() { return history; }

    public void setEffects(ArrayList<Effect> effects) {
        this.effects = effects;
    }

    public void setHistory(ArrayList<AbilityRecord> history) {
        this.history = history;
    }

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }

    public int getDamage() { return damage; }

    public void setDamage(int damage) { this.damage = damage; }

    public BattlePlayer getBattlePlayer() { return battlePlayer; }

    public void setBattlePlayer(BattlePlayer battlePlayer) { this.battlePlayer = battlePlayer; }

    public boolean isKilledByFirstStrike() {
        return killedByFirstStrike;
    }

    public void setKilledByFirstStrike(boolean killedByFirstStrike) {
        this.killedByFirstStrike = killedByFirstStrike;
    }

    public boolean isKilledByTopStrike() {
        return killedByTopStrike;
    }

    public void setKilledByTopStrike(boolean killedByTopStrike) {
        this.killedByTopStrike = killedByTopStrike;
    }
}
