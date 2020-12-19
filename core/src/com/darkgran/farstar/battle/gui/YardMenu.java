package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.YardToken;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.Yard;

public class YardMenu extends CardListMenu {
    private boolean visible = false;

    public YardMenu(Yard yard, boolean onTop, float x, float y, BattleStage battleStage, Player player) {
        super(yard, x, y, onTop, battleStage, player);
    }

    @Override
    protected void setupOffset() {
        super.setupOffset();
        setOffset(getOffset()*3/4);
    }

    @Override
    protected void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            getTokens().add(new YardToken(getCardList().get(i), getX(), getY()+ getOffset()*(i+1), getBattleStage(), this));
        }
    }

    public void switchVisibility() {
        if (getPlayer() instanceof LocalPlayer) {
            visible = !visible;
        }
    }

    public boolean isVisible() { return visible; }

    public void setVisible(boolean visible) { this.visible = visible; }

}
