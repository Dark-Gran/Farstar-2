package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.gui.tokens.TokenType;

public class Mothership extends BattleCard {

    public Mothership(int id) { super(id); }

    @Override
    public void death() {
        if (getBattlePlayer() != null) {
            getToken().getBattleStage().getBattleScreen().getBattle().addGameOver(getBattlePlayer());
            getToken().getBattleStage().getAnimationManager().newDeathEffect(getToken().getX(), getToken().getY(), TokenType.MS);
        }
    }

}