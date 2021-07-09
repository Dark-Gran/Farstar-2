package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.battle.players.Player;

public abstract class JunkableCard extends Card {

    public JunkableCard(CardInfo cardInfo, Player player) { super(cardInfo, player); }

    public JunkableCard() { super(); }

    public JunkableCard(int id) { super(id); }

    @Override
    public void death() {
        if (getToken() != null) {
            if (getCardInfo().getCardType() != CardType.YARDPRINT) {
                getToken().addCardToJunk();
            }
            getToken().destroy();
        }
    }

}
