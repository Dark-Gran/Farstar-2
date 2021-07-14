package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.FleetMenu;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.battle.players.cards.Card;

public class FleetToken extends ClickToken implements DisableMark, FakingTokens {
    private FleetMenu fleetMenu;
    private Texture disableMark;

    public FleetToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, FleetMenu fleetMenu, boolean noPics, boolean connectCard) {
        super(card, x, y, battleStage, cardListMenu, TokenType.FLEET, noPics, connectCard);
        this.fleetMenu = fleetMenu;
        if (!noPics) { setMark(Farstar.ASSET_LIBRARY.get("images/tokens/disable_F.png")); }
    }

    public FleetToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, FleetMenu fleetMenu, boolean noPics, boolean connectCard) {
        super(card, x, y, battleStage, cardListMenu, tokenType, noPics, connectCard);
        this.fleetMenu = fleetMenu;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        drawMark(batch, getX(), getY());
    }

    @Override
    boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Battle battle = getBattleStage().getBattleScreen().getBattle();
        if (button == 0 && getCard().getPlayer() instanceof LocalPlayer && getCard().getPlayer() == battle.getWhoseTurn() && battle.getCombatManager().isActive() && !battle.getCombatManager().getDuelManager().isActive() && !battle.getCombatManager().isTacticalPhase() && !getBattleStage().getBattleScreen().getBattle().isEverythingDisabled()) {
            battle.getCombatManager().cancelDuel(this);
            newFake(event, x, y, pointer, button, FakeTokenType.TARGETING);
            return false;
        } else {
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    @Override
    void click(int button) {
        getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(this, getFleetMenu().getPlayer());
    }

    @Override
    public void destroy() {
        remove();
    }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public void setFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

    @Override
    public Texture getMark() {
        return disableMark;
    }

    @Override
    public void setMark(Texture texture) {
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

}
