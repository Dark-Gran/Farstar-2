package com.darkgran.farstar.battle.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Mothership;

import java.util.ArrayList;

public abstract class Bot extends Player implements BotSettings {
    private Battle battle;
    private final BotTier botTier;
    private final float timerDelay;

    public Bot(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard);
        this.botTier = botTier;
        this.timerDelay = getTimerDelay(botTier);
        report("Hello Universe!");
    }

    public void newTurn() {
        report("My Turn Began!");
        delayAction(this::turn);
    }

    public void turn() { }

    public void chooseTargets(Token token, AbilityInfo ability, DropTarget dropTarget) { }

    public void pickAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) { }

    public void newCombat() { }

    public void newDuelOK() { }

    public void delayAction(Runnable runnable) {
        Timer.schedule(new Timer.Task() {
            public void run() {
                if (runnable != null) { Gdx.app.postRunnable(runnable); }
            }
        }, timerDelay);
    }

    public boolean deploy(Card card, BaseMenu baseMenu, int position) {
        CardType cardType = card.getCardInfo().getCardType();
        DropTarget dropTarget;
        if (cardType == CardType.SUPPORT) {
            dropTarget = (SupportMenu) getSupports().getCardListMenu();
        } else {
            dropTarget = getFleet().getFleetMenu() ;
        }
        if (baseMenu instanceof HandMenu) {
            HandToken handToken = new HandToken(card, getFleet().getFleetMenu().getX(), getFleet().getFleetMenu().getY(), getHand().getCardListMenu().getBattleStage(), (baseMenu instanceof CardListMenu) ? (CardListMenu) baseMenu : null);
            return getBattle().getRoundManager().processDrop(handToken, dropTarget, position, false, true);
        } else {
            Token token = new Token(card, getFleet().getFleetMenu().getX(), getFleet().getFleetMenu().getY(), getHand().getCardListMenu().getBattleStage(), (baseMenu instanceof CardListMenu) ? (CardListMenu) baseMenu : null);
            return getBattle().getRoundManager().processDrop(token, dropTarget, position, false, true);
        }

    }

    public void report(String message) {
        System.out.println(botTier+": "+message);
    }

    public void setBattle(Battle battle) { this.battle = battle; }

    public Battle getBattle() { return battle; }

    public BotTier getBotTier() { return botTier; }

}
