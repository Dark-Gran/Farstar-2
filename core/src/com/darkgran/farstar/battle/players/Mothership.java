package com.darkgran.farstar.battle.players;

public class Mothership extends TokenizedCard {
    private Player player;

    public Mothership(int id) { super(id); }

    @Override
    public void death() {
        if (player != null) {
            getToken().getBattleStage().getBattleScreen().getBattle().addGameOver(player);
        }
    }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}