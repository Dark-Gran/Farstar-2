package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.cards.CardLibrary;
import com.darkgran.farstar.gui.ButtonWithExtraState;

public class Yard extends CardList {
    private ButtonWithExtraState yardButton;

    public Yard(int id) {
        super(id);
    }

    public Yard() { //default
        super();
        setupSize();
        clear();
        add(new BattleCard(CardLibrary.getInstance().getCard(1), null));
        add(new BattleCard(CardLibrary.getInstance().getCard(2), null));
        add(new BattleCard(CardLibrary.getInstance().getCard(3), null));
        add(new BattleCard(CardLibrary.getInstance().getCard(4), null));
        add(new BattleCard(CardLibrary.getInstance().getCard(5), null));
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.getInstance().YARD_SIZE);
    }

    public ButtonWithExtraState getYardButton() {
        return yardButton;
    }

    public void setYardButton(ButtonWithExtraState yardButton) {
        this.yardButton = yardButton;
    }
}
