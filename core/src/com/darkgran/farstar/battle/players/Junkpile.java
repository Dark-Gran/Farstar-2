package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.gui.JunkButton;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.util.SimpleVector2;

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
        setMaxSize(BattleSettings.DECK_SIZE*2);
    }

    @Override
    public boolean addCard(Card card) {
        add(card);
        if (junkButton != null) {

            junkButton.setup(card, TokenType.JUNK, new SimpleVector2(junkButton.getX(), junkButton.getY()));
        }
        return true;
    }

    public JunkButton getJunkButton() {
        return junkButton;
    }

    public void setJunkButton(JunkButton junkButton) {
        this.junkButton = junkButton;
    }
}
