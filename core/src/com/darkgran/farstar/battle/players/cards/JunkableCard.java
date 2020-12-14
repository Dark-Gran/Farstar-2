package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.battle.players.Player;

public abstract class JunkableCard extends TokenizedCard {

    public JunkableCard(CardInfo cardInfo, Player player) { super(cardInfo, player); }

    public JunkableCard() { super(); }

    public JunkableCard(int id) { super(id); }

    @Override
    public void death() {
        if (getToken() != null) {
            if (getCardInfo().getCardType() != CardType.YARD) {
                getToken().addCardToJunk();
                getToken().destroy();
            }
        }
    }

}
