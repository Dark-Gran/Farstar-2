package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.gui.tokens.YardToken;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.battle.players.Yard;
import com.darkgran.farstar.gui.Notification;

public class YardMenu extends CardListMenu {
    private boolean open = false;

    public YardMenu(Yard yard, boolean onTop, float x, float y, BattleStage battleStage, BattlePlayer battlePlayer) {
        super(yard, x, y, 0, 0, onTop, battleStage, battlePlayer);
    }

    @Override
    public void setupOffset() {
        if (isNegativeOffset()) { setOffset(TokenType.YARD.getHeight()*-1); }
        else { setOffset(TokenType.YARD.getHeight()); }
    }

    @Override
    protected void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            getTokens().add(new YardToken(getCardList().get(i), getX(), getY()+ getOffset()*i, getBattleStage(), this));
        }
    }

    public void switchVisibility(boolean visible) {
        if (getPlayer() instanceof LocalBattlePlayer) {
            setOpen(!visible);
            switchVisibility();
        } else {
            if (visible) {
                accessDenied();
            }
        }
    }

    public void switchVisibility() {
        if (getPlayer() instanceof LocalBattlePlayer) {
            if (!getBattleStage().getAbilityPicker().isActive()) {
                open = !open;
                setTouchable(open);
            }
        } else {
            if (!open) { accessDenied(); }
        }
    }

    private void accessDenied() {
        getBattleStage().getBattleScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Access Denied.", 3);
    }

    private void setTouchable(boolean enable) {
        for (Token token : getTokens()) {
            token.setTouchable(enable ? Touchable.enabled : Touchable.disabled);
        }
    }

    public boolean isOpen() { return open; }

    public void setOpen(boolean open) { this.open = open; }

}
