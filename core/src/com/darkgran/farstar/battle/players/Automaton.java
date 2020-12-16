package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.DropTarget;
import com.darkgran.farstar.battle.gui.SupportMenu;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Mothership;

/**
 *  "Just Play Something":
 *  -- No sensors beyond PossibilityAdvisor
 *  -- No planning
 */
public class Automaton extends Bot {

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard, botTier);
    }

    @Override
    public void turn() {
        deploy(getYard().get(0), getYard().getCardListMenu());
        delayAction(getBattle().getRoundManager()::endTurn);
    }

    @Override
    public void deploy(Card card, CardListMenu cardListMenu) {
        Token token = new Token(card, getFleet().getFleetMenu().getX(), getFleet().getFleetMenu().getY(), getHand().getCardListMenu().getBattleStage(), cardListMenu);
        CardType cardType = token.getCard().getCardInfo().getCardType();
        DropTarget dropTarget;
        if (cardType == CardType.SUPPORT) {
            dropTarget = (SupportMenu) getSupports().getCardListMenu();
        } else {
            dropTarget = getFleet().getFleetMenu() ;
        }
        int position = getHand().getCardListMenu().getBattleStage().getRoundDropPosition(dropTarget.getSimpleBox2().getX()+dropTarget.getSimpleBox2().getWidth()/2-1, dropTarget.getSimpleBox2().getY()+1, dropTarget, cardType);
        getBattle().getRoundManager().processDrop(token, getFleet().getFleetMenu(), position, false, true);
    }

    @Override
    public void newCombat() {
        getBattle().getCombatManager().endCombat();
    }

}
