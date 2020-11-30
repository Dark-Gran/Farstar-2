package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.battle.DuelManager;

public abstract class DuelMenu {
    private final Texture duel = new Texture("images/duel.png");
    private final DuelManager duelManager;
    private BattleStage battleStage;

    public DuelMenu(DuelManager duelManager) {
        this.duelManager = duelManager;
        duelManager.receiveDuelMenu(this);
    }

    //must be set before RoundManager.launch()
    public void setBattleStage(BattleStage battleStage) { this.battleStage = battleStage; }

    public void addActors() { }

    public void removeActors() { }

    public void dispose() {
        duel.dispose();
    }

    public Texture getDuel() { return duel; }

    public DuelManager getDuelManager() { return duelManager; }

    public BattleStage getBattleStage() { return battleStage; }


}
