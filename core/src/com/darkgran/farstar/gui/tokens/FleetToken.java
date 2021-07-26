package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.RoundManager;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.battlegui.FleetMenu;
import org.jetbrains.annotations.NotNull;

public class FleetToken extends ClickToken implements DisableMark, FakingTokens, Comparable<FleetToken> {
    private FleetMenu fleetMenu;
    private TextureRegion disableMark;

    public FleetToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, FleetMenu fleetMenu, boolean noPics, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.FLEET, noPics, connectCard);
        this.fleetMenu = fleetMenu;
        setMark(Farstar.ASSET_LIBRARY.getAtlasRegion("disable-F"));
    }

    public FleetToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, FleetMenu fleetMenu, boolean noPics, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, tokenType, noPics, connectCard);
        this.fleetMenu = fleetMenu;
        setMark(Farstar.ASSET_LIBRARY.getAtlasRegion("disable-F"));
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        drawMark(batch, getX(), getY());
    }

    @Override
    boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Battle battle = getBattleStage().getBattleScreen().getBattle();
        if (button == 0) {
            if (battle.getRoundManager().isCombatMoveEnabled(this)) {
                battle.getCombatManager().cancelDuel(this);
                newFake(event, x, y, pointer, button, FakeTokenType.TARGET);
            }
            return false;
        } else {
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    @Override
    void click(int button) {
        getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(this, getFleetMenu().getBattlePlayer());
    }

    @Override
    public void destroy() {
        remove();
        getFleetMenu().getBattleStage().getAnimationManager().newDeathEffect(getX(), getY(), TokenType.FLEET);
    }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public void setFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    @Override
    public TextureRegion getMark() {
        return disableMark;
    }

    @Override
    public void setMark(TextureRegion texture) {
        disableMark = texture;
    }

    @Override
    public boolean isDisabled() {
        if (isNoPics()) {
            return false;
        } else {
            return getCard().isUsed();
        }
    }

    //Tokens sorted from left to right (for the duels-execution)
    @Override
    public int compareTo(@NotNull FleetToken o) {
        return Float.compare(this.getX(), o.getX());
    }

}
