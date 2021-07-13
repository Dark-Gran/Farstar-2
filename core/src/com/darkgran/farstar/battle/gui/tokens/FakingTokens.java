package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public interface FakingTokens {
    enum FakeTokenType {
        DEPLOYMENT, TARGETING
    }
    BattleStage getBattleStage();
    CardListMenu getCardListMenu();
    Card getCard();
    float getX();
    float getY();

    default void newFake(InputEvent event, float x, float y, int pointer, int button, FakeTokenType fakeTokenType) {
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
            case DEPLOYMENT:
                return new DeploymentToken(getCard(), getX(), getY(), getBattleStage(), getCardListMenu());
            case TARGETING:
                return new TargetingToken(getCard(), getX(), getY(), getBattleStage(), getCardListMenu());
        }
        return null;
    }
}
