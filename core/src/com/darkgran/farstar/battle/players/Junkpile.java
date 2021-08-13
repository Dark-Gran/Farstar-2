package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.gui.tokens.JunkButton;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.gui.SimpleVector2;

//"Junkyard"/"Scrapyard"
public class Junkpile extends CardList {
    private JunkButton junkButton;

    public Junkpile() {
        super();
        setupSize();
        clear();
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.getInstance().DECK_SIZE);
    }

    @Override
    public boolean addCard(BattleCard battleCard) {
        battleCard.reset();
        add(battleCard);
        refreshToken(battleCard);
        return true;
    }

    public void refreshToken(BattleCard battleCard) {
        if (junkButton != null) {
            junkButton.setup(battleCard, TokenType.JUNK, new SimpleVector2(junkButton.getX(), junkButton.getY()));
        }
    }

    @Override
    public void clear() {
        super.clear();
        refreshToken(null);
    }

    public JunkButton getJunkButton() {
        return junkButton;
    }

    public void setJunkButton(JunkButton junkButton) {
        this.junkButton = junkButton;
    }
}
