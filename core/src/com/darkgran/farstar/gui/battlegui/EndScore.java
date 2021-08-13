package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.*;

import static com.darkgran.farstar.Farstar.STAGE_HEIGHT;
import static com.darkgran.farstar.Farstar.STAGE_WIDTH;

/**
 * "VICTORY"/"DEFEAT" on battleEnd()
 */
public class EndScore {
    private final Battle battle;
    private final TextInTheBox resultBox;
    private final ActorButton endButton = new ActorButton(AssetLibrary.getInstance().getAtlasRegion("continue"), AssetLibrary.getInstance().getAtlasRegion("continueO")){
        @Override
        public void clicked() {
            battle.getBattleScreen().escapeToMenu();
        }
    };

    public EndScore(Battle battle) {
        this.battle = battle;
        resultBox = new TextInTheBox(
                ColorPalette.LIGHT,
                ColorPalette.changeAlpha(ColorPalette.DARK, 0.6f),
                "fonts/"+Notification.NotificationType.MIDDLE.getFontName()+".fnt",
                battle.getWinner() instanceof LocalBattlePlayer ? "VICTORY" : "DEFEAT",
                Notification.NotificationType.MIDDLE.getX()-Notification.NotificationType.MIDDLE.getBoxWidth()/4,
                Notification.NotificationType.MIDDLE.getY()+Notification.NotificationType.MIDDLE.getBoxHeight()/4,
                Notification.NotificationType.MIDDLE.getBoxWidth(),
                Notification.NotificationType.MIDDLE.getBoxHeight()
        );
        endButton.setPosition(STAGE_WIDTH*0.57f, STAGE_HEIGHT*0.28f);
        this.battle.getBattleScreen().getBattleStage().addActor(endButton);
    }

    public void draw(float delta, Batch batch) {
        resultBox.draw(batch, battle.getBattleScreen().getShapeRenderer());
    }

    public void dispose() {
        endButton.dispose();
        endButton.remove();
    }
}
