package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.tokens.AnchoredToken;
import com.darkgran.farstar.gui.tokens.HandToken;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.Hand;

public class HandMenu extends CardListMenu {
    private float actualX;
    private float actualY;
    private final ClickListener clickListener = new ClickListener(){
        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            setHandState(HandMenuState.UP);
            super.enter(event, x, y, pointer, fromActor);
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            setHandState(HandMenuState.IDLE);
            super.exit(event, x, y, pointer, toActor);
        }
    };
    private final Actor clickListenerActor = new Actor();
    public enum HandMenuState {
        IDLE, UP;
    }
    private HandMenuState handMenuState = HandMenuState.IDLE;

    public HandMenu(Hand hand, float x, float y, BattleStage battleStage, BattlePlayer battlePlayer, boolean onBottom) {
        super(hand, x, y, 0, 0, !onBottom, battleStage, battlePlayer);
        setWidth(Farstar.STAGE_WIDTH*0.57f);
        setHeight(Farstar.STAGE_HEIGHT*0.35f);
        actualX = x;
        actualY = y;
        clickListenerActor.addListener(clickListener);
        clickListenerActor.setBounds(getX()-getWidth()/2, getY(), getWidth(), getHeight());
        battleStage.addActor(clickListenerActor);
    }

    public void setHandState(HandMenuState handMenuState) {
        if (getPlayer() instanceof LocalBattlePlayer) {
            if (this.handMenuState != handMenuState) {
                this.handMenuState = handMenuState;
                for (Token token : getTokens()) {
                    ((AnchoredToken) token).resetPosition();
                }
            }
        }
    }

    @Override
    public void removeToken(Token token) {
        boolean found = false;
        for (int i = 0; i < getTokens().size(); i++) {
            if (!found && getTokens().get(i) == token) {
                getTokens().remove(token);
                i--;
                found = true;
            } else {
                Token nextToken = getTokens().get(i);
                nextToken.setX(getX() + getOffset()*i);
                if (nextToken instanceof AnchoredToken) { ((AnchoredToken) nextToken).setAnchorX(nextToken.getX()); }
            }
        }
        centralize();
    }

    private void centralize() {
        float covering = 1f;
        if (getTokens().size() > 1) {
            switch (getTokens().size()) {
                case 2:
                    covering = 0.98f;
                    break;
                case 3:
                    covering = 0.97f;
                    break;
                case 4:
                    covering = 0.94f;
                    break;
                case 5:
                    covering = 0.89f;
                    break;
                case 6:
                    covering = 0.72f;
                    break;
                case 7:
                    covering = 0.61f;
                    break;
                case 8:
                    covering = 0.53f;
                    break;
                default:
                    covering = 1f - (getTokens().size()-4)*0.2f;
                    break;
            }
        }
        float offset = ((TokenType.HAND.getWidth() * getTokens().size())-TokenType.HAND.getWidth()*(1f-covering)*(getTokens().size()-1)) * 0.5f;
        actualX = getX()-offset;
        refreshTokenPositions(covering);
    }

    private void refreshTokenPositions(float covering) {
        float offsetY;
        for (int i = 0; i < getTokens().size(); i++) {
            offsetY = getYShift(i, getTokens().size());
            getTokens().get(i).setPosition(actualX + getOffset()*i*covering, actualY+offsetY);
            ((AnchoredToken) getTokens().get(i)).setNewAnchor(actualX + getOffset()*i*covering, actualY+offsetY);
            ((HandToken) getTokens().get(i)).refreshRotation(i, getTokens().size());
        }
    }

    private float getYShift(int position, int battleCards) { //turns battleCards into a fan (if combined with rotation)
        float offset = 0f;
        switch (battleCards) {
            case 2:
                offset = 1f;
                break;
            case 3:
                if (position != 1) { offset = 10f; }
                break;
            case 4:
                offset = ((position == 0 || position == 3) ? 15f : 1f);
                break;
            case 5:
                if (position != 2) {
                    offset = ((position == 0 || position == 4) ? 23f : 8f);
                }
                break;
            case 6:
                if (position == 0 || position == 5) {
                    offset = 24f;
                } else if (position == 1 || position == 4) {
                    offset = 10f;
                } else {
                    offset = 1f;
                }
                break;
            case 7:
                if (position != 3) {
                    if (position == 2 || position == 4) {
                        offset = 4f;
                    } else if (position == 1 || position == 5) {
                        offset = 12f;
                    } else {
                        offset = 25f;
                    }
                }
                break;
            case 8:
                if (position == 0 || position == 7) {
                    offset = 28f;
                } else if (position == 1 || position == 6) {
                    offset = 15f;
                } else if (position == 2 || position == 5) {
                    offset = 5f;
                } else {
                    offset = 1f;
                }
                break;
        }
        return offset * (isNegativeOffset() ? 1f : -1f);
    }

    @Override
    public void setupOffset() {
        setOffset(TokenType.HAND.getWidth());
    }

    @Override
    protected void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            getTokens().add(new HandToken(getCardList().get(i), actualX + getOffset()*i, actualY, getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(BattleCard battleCard) {
        getTokens().add(new HandToken(battleCard, actualX + getOffset()*getTokens().size(), actualY, getBattleStage(), this));
        centralize();
    }

    public Actor getClickListenerActor() {
        return clickListenerActor;
    }

    public HandMenuState getHandState() {
        return handMenuState;
    }
}
