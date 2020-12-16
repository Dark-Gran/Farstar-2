package com.darkgran.farstar.battle.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.gui.DropTarget;
import com.darkgran.farstar.battle.gui.SupportMenu;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Mothership;

public class Automaton extends Player { //TODO "possibility-sensor" (for non-ai use too)
    private final BotTier botTier;
    private Battle battle;
    private float timerDelay = 1f;

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard);
        this.botTier = botTier;
        report("Hello Universe!");
    }

    public void newTurn() {
        report("My Turn Began!");
        Timer.schedule(new Timer.Task(){
            public void run () { Gdx.app.postRunnable(() -> { turn(); }); }
        }, timerDelay);
    }

    private void turn() {
        deploy(getYard().get(0), getYard().getCardListMenu());
        battle.getRoundManager().endTurn();
    }

    private void deploy(Card card, CardListMenu cardListMenu) {
        Token token = new Token(card, getFleet().getFleetMenu().getX(), getFleet().getFleetMenu().getY(), getHand().getCardListMenu().getBattleStage(), cardListMenu);
        CardType cardType = token.getCard().getCardInfo().getCardType();
        DropTarget dropTarget;
        if (cardType == CardType.SUPPORT) {
            dropTarget = (SupportMenu) getSupports().getCardListMenu();
        } else {
            dropTarget = getFleet().getFleetMenu() ;
        }
        int position = getHand().getCardListMenu().getBattleStage().getRoundDropPosition(dropTarget.getSimpleBox2().getX()+dropTarget.getSimpleBox2().getWidth()/2-1, dropTarget.getSimpleBox2().getY()+1, dropTarget, cardType);
        battle.getRoundManager().processDrop(token, getFleet().getFleetMenu(), position, false, true);
    }

    public void newCombat() {
        battle.getCombatManager().endCombat();
    }

    private void report(String message) { System.out.println(botTier+": "+message); }

    public void setBattle(Battle battle) { this.battle = battle; }

}
