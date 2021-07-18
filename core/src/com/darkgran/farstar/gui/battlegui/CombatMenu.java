package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.battle.CombatManager;
import com.darkgran.farstar.battle.DuelManager;
import com.darkgran.farstar.gui.tokens.TargetingToken;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;

import java.util.Map;

public abstract class CombatMenu {
    private final CombatManager combatManager;
    private BattleStage battleStage;  //must be set after ini - before RM.launch (see BattleScreen constructor)

    public CombatMenu(CombatManager combatManager) {
        this.combatManager = combatManager;
        combatManager.setCombatMenu(this);
    }

    public void drawDuels(Batch batch, ShapeRenderer shapeRenderer) {
        if (combatManager.isActive() && combatManager.getDuels().size() > 0) {
            batch.end();

            for (Map.Entry<Token, DuelManager.AttackInfo> entry : combatManager.getDuels().entrySet()) {
                if (entry.getValue().getState() != 2) {
                    shapeRenderer.setColor(entry.getValue().getState() == 1 ? Color.ORANGE : ColorPalette.LIGHT);
                    Vector2 start = new Vector2(entry.getKey().getX() + entry.getKey().getWidth() / 2, entry.getKey().getY() + entry.getKey().getHeight() / 2);
                    Vector2 end = new Vector2(entry.getValue().getDefender().getX() + entry.getValue().getDefender().getWidth() / 2, entry.getValue().getDefender().getY() + entry.getValue().getDefender().getHeight() / 2);
                    TargetingToken.drawConnection(shapeRenderer, start, end);
                }
            }

            batch.begin();
        }
    }

    public void addOK(CombatOK combatOK) {
        if (combatOK.getDuelPlayer().getPlayer() instanceof LocalBattlePlayer) {
            getBattleStage().addActor(combatOK);
            combatOK.setDisabled(false);
        }
    }

    public void removeAllOKs() { }

    public void removeOK(CombatOK combatOK) {
        combatOK.remove();
        combatOK.setDisabled(true);
    }

    public CombatManager getCombatManager() { return combatManager; }

    public BattleStage getBattleStage() { return battleStage; }

    public void setBattleStage(BattleStage battleStage) { this.battleStage = battleStage; }

}
