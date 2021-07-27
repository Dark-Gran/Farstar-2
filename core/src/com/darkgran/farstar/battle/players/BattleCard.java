package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.cards.*;
import com.darkgran.farstar.gui.tokens.Token;

import java.util.ArrayList;

public class BattleCard extends Card {
    private BattlePlayer battlePlayer;
    private final ArrayList<Effect> effects = new ArrayList<>();
    private final ArrayList<AbilityRecord> history = new ArrayList<>();
    private boolean used; //for AbilityStarter.USE
    private int damage = 0;
    private boolean killedByFirstStrike;

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

    @Override
    public void refreshToken(boolean def, boolean off) {
        if (getToken() != null) {
            if (getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom() != null && this == getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().getCard()) {
                getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().setup(this, getToken().getTokenType(), getBattlePlayer().getBattle().getBattleScreen().getBattleStage().getCardZoom().getTargetXY());
            }
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
        refreshToken(true, false);
        return getHealth() > 0;
    }

    public void repairDMG(int dmg) {
        damage -= dmg;
        if (damage < 0) { damage = 0; }
        refreshToken(true, false);
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
    }

    public void death() { }

    public void addToHistory (BattleCard battleCard, AbilityInfo ability) { history.add(new AbilityRecord(battleCard, ability));  }

    public void addToEffects (Effect effect) {
        effects.add(effect);
        refreshToken(true, true);
    }

    public boolean isDamaged() { return damage > 0; }

    public boolean hasDamagedWeapons() { return getCardInfo().getOffense() < getOriginalInfo().getOffense(); }

    public boolean hasUpgradedShields() { return getCardInfo().getDefense() > getOriginalInfo().getDefense(); }

    public boolean hasUpgradedWeapons() { return getCardInfo().getOffense() > getOriginalInfo().getOffense(); }

    public ArrayList<Effect> getEffects() { return effects; }

    public ArrayList<AbilityRecord> getHistory() { return history; }

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
}
