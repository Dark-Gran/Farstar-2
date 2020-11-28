package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darkgran.farstar.battle.players.Shipyard;

public class YardMenu extends TokenMenu {
    private boolean visible = false;

    public YardMenu(Shipyard shipyard, boolean onTop, float x, float y, Stage stage) {
        super(shipyard, x, y, onTop, stage);
    }

    @Override
    public void setupOffset() {
        super.setupOffset();
        setOffset(getOffset()*3/4);
    }

    @Override
    public void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().getCards().size(); i++) {
            getTokens().add(new YardToken(getCardList().getCards().get(i), getX(), getY()+ getOffset()*(i+1), getStage()));
        }
    }

    public void switchVisibility() { visible = !visible; }

    public boolean isVisible() { return visible; }

    public void setVisible(boolean visibility) { this.visible = visible; }

}
