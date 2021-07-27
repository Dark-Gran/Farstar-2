package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

public interface FakingTokens {
    enum FakeTokenType {
        YARD, TARGET, HAND
    }
    BattleStage getBattleStage();
    CardListMenu getCardListMenu();
    BattleCard getCard();
    float getX();
    float getY();

    default void newFake(InputEvent event, float x, float y, int pointer, int button, FakeTokenType fakeTokenType) {
        getBattleStage().getBattleScreen().hideScreenConceder();
        FakeToken fakeToken = makeFake(fakeTokenType);
        if (fakeToken != null) {
            getBattleStage().setFakeToken(fakeToken);
            event.setRelatedActor(getBattleStage().getFakeToken());
            event.getStage().addTouchFocus(getBattleStage().getFakeToken().getDragger(), getBattleStage().getFakeToken(), getBattleStage().getFakeToken(), event.getPointer(), event.getButton());
            getBattleStage().getFakeToken().getDragger().touchDown(event, x, y, pointer, button);
        } else {
            System.out.println("Failed to make FakeToken.");
        }
    }

    default FakeToken makeFake(FakeTokenType fakeTokenType) {
        switch (fakeTokenType) {
            case YARD:
                return new DeploymentToken(getCard(), getX(), getY(), getBattleStage(), getCardListMenu());
            case TARGET:
                return new TargetingToken(getCard(), getX(), getY(), getBattleStage(), getCardListMenu());
            case HAND:
                return new DeploymentCard(getCard(), getX(), getY(), getBattleStage(), getCardListMenu());
        }
        return null;
    }
}
