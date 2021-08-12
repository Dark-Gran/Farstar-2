package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.cards.CardInfo;
import com.darkgran.farstar.cards.CardType;

public abstract class JunkableBattleCard extends BattleCard {

    public JunkableBattleCard(CardInfo cardInfo, CardInfo originalInfo, BattlePlayer battlePlayer) { super(cardInfo, originalInfo, battlePlayer); }

    public JunkableBattleCard(CardInfo cardInfo, BattlePlayer battlePlayer) { super(cardInfo, cardInfo, battlePlayer); }

    public JunkableBattleCard() { super(); }

    public JunkableBattleCard(int id) { super(id); }

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
