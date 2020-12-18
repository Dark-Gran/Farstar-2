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
import com.darkgran.farstar.battle.players.cards.Ship;

import java.util.ArrayList;

public abstract class Bot extends Player implements BotSettings {
    private Battle battle;
    private final BotTier botTier;
    private final float timerDelay;
    private boolean pickingTarget = false;
    private boolean pickingAbility = false;
    private boolean disposed = false;

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

    public void endTurn() {
        if (!disposed) {
            getBattle().getRoundManager().endTurn();
        }
    }

    public void turn() {
        setPickingTarget(false);
        setPickingAbility(false);
    }

    public void chooseTargets(Token token, AbilityInfo ability) {
        setPickingTarget(true);
    }

    public void pickAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        setPickingAbility(true);
    }

    public void newCombat() { }

    public void newDuelOK(DuelOK duelOK) {
        delayedDuelReady(duelOK);
    }

    public Token getEnemyTarget(Token attacker, boolean checkReach) { return null; }

    public Token getAlliedTarget(Token caster) { return null; }

    //EXECUTIONS + UTILITIES

    public void gameOver(int winnerID) { report("GG"); }

    public void cancelTurn() {
        setPickingAbility(false);
        getBattle().getRoundManager().tryCancel();
        getBattle().getRoundManager().endTurn();
    }

    public void endCombat() {
        getBattle().getCombatManager().endCombat();
    }

    public void delayedTurn() {
        delayAction(this::turn);
    }

    public void delayedEndTurn() {
        delayAction(this::endTurn);
    }

    public void delayedCombatEnd() {
        delayAction(this::endCombat);
    }

    public void delayedLaunchDuel(Ship ship) {
        delayAction(()->launchDuel(ship));
    }

    public void delayedDuelReady(DuelOK duelOK) {
        delayAction(()->duelReady(duelOK));
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

    public void launchDuel(Ship ship) {
        Token enemy = getEnemyTarget(ship.getToken(), true);
        if (enemy != null && getBattle().getCombatManager().canReach(ship.getToken(), enemy, this.getFleet())) {
            getBattle().getCombatManager().getDuelManager().launchDuel(getBattle().getCombatManager(), ship.getToken(), enemy, new DuelPlayer[]{getBattle().getCombatManager().playerToDuelPlayer(ship.getPlayer())}, new DuelPlayer[]{getBattle().getCombatManager().playerToDuelPlayer(enemy.getCard().getPlayer())});
        } else {
            report("getEnemyTarget() for Duel failed! (Probably Reach.) Ending Combat.");
            delayedCombatEnd();
        }
    }

    public void duelReady(DuelOK duelOK) {
        getBattle().getCombatManager().getDuelManager().OK(duelOK);
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

    public void delayAction(Runnable runnable) { //use delayedTurn() etc. (above) for "recommended delays"
        Timer.schedule(new Timer.Task() {
            public void run() {
                if (runnable != null) { Gdx.app.postRunnable(runnable); }
            }
        }, timerDelay);
    }

    public void dispose() {
        disposed = true;
    }

    public void setBattle(Battle battle) { this.battle = battle; }

    public Battle getBattle() { return battle; }

    public BotTier getBotTier() { return botTier; }

    public boolean isPickingAbility() { return pickingAbility; }

    public void setPickingAbility(boolean pickingAbility) { this.pickingAbility = pickingAbility; }

    public boolean isPickingTarget() { return pickingTarget; }

    public void setPickingTarget(boolean pickingTarget) { this.pickingTarget = pickingTarget; }

    public boolean isDisposed() { return disposed; }

}
