package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.FleetMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.Ship;

public class FleetToken extends AnchoredToken implements DisableMark {
    private FleetMenu fleetMenu;
    private Texture disableMark;

    public FleetToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, FleetMenu fleetMenu, boolean noPics) {
        super(card, x, y, battleStage, cardListMenu, TokenType.FLEET, noPics, true);
        this.fleetMenu = fleetMenu;
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), true));
        this.addListener(getDragger());
        if (!noPics) { setMark(Farstar.ASSET_LIBRARY.get("images/tokens/disable_F.png")); }
    }

    public FleetToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, FleetMenu fleetMenu, boolean noPics) {
        super(card, x, y, battleStage, cardListMenu, tokenType, noPics, true);
        this.fleetMenu = fleetMenu;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        drawMark(batch, getX(), getY());
    }

    @Override
    public void click(int button) {
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
            return getCard().isUsed() || ((Ship) getCard()).haveFought();
        }
    }
}
