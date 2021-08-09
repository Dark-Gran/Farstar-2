package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.battlegui.FleetMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class FleetToken extends ClickToken implements DisableMark, FakingTokens, Comparable<FleetToken> {
    private final FleetMenu fleetMenu;
    private TextureRegion disableMark;

    public FleetToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, FleetMenu fleetMenu, boolean noPics, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.FLEET, noPics, connectCard);
        this.fleetMenu = fleetMenu;
        setMark(Farstar.ASSET_LIBRARY.getAtlasRegion("disable-F"));
        setZIndex(0);
        fleetMenu.setZIndex(0);
    }

    public FleetToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, FleetMenu fleetMenu, boolean noPics, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, tokenType, noPics, connectCard);
        this.fleetMenu = fleetMenu;
        setMark(Farstar.ASSET_LIBRARY.getAtlasRegion("disable-F"));
        setZIndex(0);
        fleetMenu.setZIndex(0);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        drawMark(batch, getX(), getY());
    }

    @Override
    boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0) {
            Battle battle = getBattleStage().getBattleScreen().getBattle();
            if (!isDisabled() && battle.getRoundManager().isCombatMoveEnabled(this)) {
                battle.getCombatManager().cancelDuel(this);
                newFake(event, x, y, pointer, button, FakeTokenType.TARGET);
                return false;
            }
            return true;
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

    @Override
    public TextureRegion getMark() {
        return disableMark;
    }

    @Override
    public boolean isDisabled() {
        if (isNoPics()) {
            return false;
        } else {
            return getCard().isUsed();
        }
    }

    @Override
    public void setMark(TextureRegion texture) {
        disableMark = texture;
    }

    //Tokens sorted from left to right (for the duels-execution)
    @Override
    public int compareTo(@NotNull FleetToken that) { //if movement becomes desirable for FleetTokens, anchors should be used instead of actual position (see AnchoredToken)
        if (this.fleetMenu != that.fleetMenu) {
            return 1;
        }
        return Float.compare(this.getX(), that.getX());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FleetToken that = (FleetToken) o;
        return this.getX()==that.getX() && this.fleetMenu.equals(that.fleetMenu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.fleetMenu);
    }
}
