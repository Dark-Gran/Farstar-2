package com.darkgran.farstar.battle.players;

public class Mothership extends BattleCard {

    public Mothership(int id) { super(id); }

    @Override
    public void death() {
        if (getPlayer() != null) {
            getToken().getBattleStage().getBattleScreen().getBattle().addGameOver(getPlayer());
        }
    }

}