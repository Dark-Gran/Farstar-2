package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.tokens.AnchoredToken;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.LocalPlayer;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.Hand;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.util.SimpleVector2;

public class HandMenu extends CardListMenu {
    private float actualX;
    private float actualY;
    private final ClickListener clickListener = new ClickListener(){
        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            //setHandState(HandState.UP);
            super.enter(event, x, y, pointer, fromActor);
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            //setHandState(HandState.IDLE);
            super.exit(event, x, y, pointer, toActor);
        }
    };
    private final Actor clickListenerActor = new Actor();
    public enum HandState {
        IDLE, UP;
    }
    private HandState handState = HandState.IDLE;
    private final SimpleVector2 basicPosition;

    public HandMenu(Hand hand, float x, float y, BattleStage battleStage, Player player, boolean onBottom) {
        super(hand, x, y, 0, 0, !onBottom, battleStage, player);
        basicPosition = new SimpleVector2(x, y);
        setWidth(Farstar.STAGE_WIDTH*0.57f);
        setHeight(Farstar.STAGE_HEIGHT*0.35f);
        actualX = x;
        actualY = y;
        clickListenerActor.addListener(clickListener);
        clickListenerActor.setBounds(getX()-getWidth()/2, getY(), getWidth(), getHeight());
        battleStage.addActor(clickListenerActor);
    }

    public void setHandState(HandState handState) {
        if (!Gdx.input.isButtonPressed(0) && !Gdx.input.isButtonPressed(0)) {
            if (getPlayer() instanceof LocalPlayer) {
                if (this.handState != handState) {
                    this.handState = handState;
                    checkVerticalShift();
                }
            }
        }
    }

    public void checkVerticalShift() {
        float shift = this.isNegativeOffset() ? -285f : 250f;
        switch (handState) {
            case IDLE:
                for (Token token : getTokens()) {
                    token.setY(token.getY() - shift);
                    ((AnchoredToken) token).setAnchorY(token.getY());
                }
                break;
            case UP:
                for (Token token : getTokens()) {
                    token.setY(token.getY() + shift);
                    ((AnchoredToken) token).setAnchorY(token.getY());
                }
                break;
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

    private float getYShift(int position, int cards) { //turns cards into a fan (if combined with rotation)
        float offset = 0f;
        switch (cards) {
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
    public void generateNewToken(Card card) {
        getTokens().add(new HandToken(card, actualX + getOffset()*getTokens().size(), actualY, getBattleStage(), this));
        centralize();
    }

    public Actor getClickListenerActor() {
        return clickListenerActor;
    }
}
