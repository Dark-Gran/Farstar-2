package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.gui.tokens.YardToken;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.battle.players.Yard;
import com.darkgran.farstar.gui.Notification;

public class YardMenu extends CardListMenu implements DropTarget {
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
            getTokens().add(new YardToken(getCardList().get(i), x, y+ getOffset()*i, getBattleStage(), this));
        }
        refreshSimpleBox2();
    }

    @Override
    public void generateNewToken(BattleCard battleCard) {
        super.generateNewToken(battleCard);
        refreshSimpleBox2();
    }

    @Override
    public void removeToken(Token token) {
        super.removeToken(token);
        refreshSimpleBox2();
    }

    private void refreshSimpleBox2() {
        setupSimpleBox2(x, y, TokenType.YARD.getWidth(), TokenType.YARD.getHeight()*getCardList().size());
    }

    public void switchVisibility(boolean visible) {
        if (getBattlePlayer() instanceof LocalBattlePlayer) {
            open = !visible;
            switchVisibility();
        } else {
            if (visible) {
                accessDenied();
            }
        }
    }

    public void switchVisibility() {
        if (!open) {
             getBattleStage().getBattleScreen().hideScreenConceder();
        }
        if (getBattlePlayer() instanceof LocalBattlePlayer) {
            if (!getBattleStage().getAbilityPicker().isActive()) {
                open = !open;
                setTouchable(open);
            }
        } else {
            if (!open) { accessDenied(); }
        }
    }

    private void accessDenied() {
        NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Access Denied.", 3);
    }

    private void setTouchable(boolean enable) {
        for (Token token : getTokens()) {
            token.setTouchable(enable ? Touchable.enabled : Touchable.disabled);
        }
    }

    @Override
    public boolean isActive() {
        return isOpen();
    }

    public boolean isOpen() { return open; }

}
