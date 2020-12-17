package com.darkgran.farstar.battle.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Mothership;

import java.util.ArrayList;

public abstract class Bot extends Player implements BotSettings {
    private Battle battle;
    private final BotTier botTier;
    private final float timerDelay;
    private boolean pickingAbility = false;

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

    public void chooseTargets(Token token, AbilityInfo ability) { }

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

    public void cancelTurn() {
        setPickingAbility(false);
        getBattle().getRoundManager().tryCancel();
        getBattle().getRoundManager().endTurn();
    }

    public boolean deploy(Card card, BaseMenu baseMenu, int position) {
        CardType cardType = card.getCardInfo().getCardType();
        DropTarget dropTarget;
        if (cardType == CardType.SUPPORT) {
            dropTarget = (SupportMenu) getSupports().getCardListMenu();
        } else {
            dropTarget = getFleet().getFleetMenu() ;
        }
        Token token = cardToToken(card, baseMenu);
        if (baseMenu instanceof HandMenu) {
            for (Token tokenInHand : ((HandMenu) baseMenu).getTokens()) {
                if (tokenInHand instanceof HandToken && tokenInHand.getCard() == card) {
                    token = tokenInHand;
                    break;
                }
            }
            if (!(token instanceof HandToken)) {
                return false;
            }
        }
        return getBattle().getRoundManager().processDrop(token, dropTarget, position, false, true);
    }

    public boolean useAbility(Card card, BaseMenu baseMenu) {
        Token token = cardToToken(card, baseMenu);
        getBattle().getRoundManager().checkAllAbilities(token, null, AbilityStarter.USE, this, null);
        return true;
    }

    public Token cardToToken(Card card, BaseMenu baseMenu) {
        return new Token(card, getFleet().getFleetMenu().getX(), getFleet().getFleetMenu().getY(), getHand().getCardListMenu().getBattleStage(), (baseMenu instanceof CardListMenu) ? (CardListMenu) baseMenu : null);
    }

    public void report(String message) {
        System.out.println(botTier+"(PLR"+getBattleID()+"): "+message);
    }

    public boolean isDeploymentMenu(BaseMenu baseMenu) {
        return (baseMenu instanceof FleetMenu || baseMenu instanceof SupportMenu);
    }

    public void setBattle(Battle battle) { this.battle = battle; }

    public Battle getBattle() { return battle; }

    public BotTier getBotTier() { return botTier; }

    public boolean isPickingAbility() { return pickingAbility; }

    public void setPickingAbility(boolean pickingAbility) { this.pickingAbility = pickingAbility; }

}
