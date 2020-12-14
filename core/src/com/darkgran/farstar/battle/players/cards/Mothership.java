package com.darkgran.farstar.battle.players.cards;

public class Mothership extends TokenizedCard {

    public Mothership(int id) { super(id); }

    @Override
    public void death() {
        if (getPlayer() != null) {
            getToken().getBattleStage().getBattleScreen().getBattle().addGameOver(getPlayer());
        }
    }

}