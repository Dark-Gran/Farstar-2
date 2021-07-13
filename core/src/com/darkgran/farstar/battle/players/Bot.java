package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.*;
import com.darkgran.farstar.battle.gui.tokens.HandToken;
import com.darkgran.farstar.battle.gui.tokens.Token;
import com.darkgran.farstar.battle.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.abilities.AbilityInfo;
import com.darkgran.farstar.battle.players.abilities.AbilityStarter;
import com.darkgran.farstar.battle.players.abilities.EffectType;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.battle.players.cards.Mothership;
import com.darkgran.farstar.battle.players.cards.Ship;
import com.darkgran.farstar.util.Delayer;

import java.util.ArrayList;

public abstract class Bot extends Player implements BotSettings, Delayer {
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
        delayedTurn(false, null);
    }

    protected boolean turn(boolean combat, CombatOK combatOK) {
        setPickingTarget(false);
        setPickingAbility(false);
        return true;
    }

    public void chooseTargets(Token token, AbilityInfo ability) {
        setPickingTarget(true);
    }

    public void pickAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        setPickingAbility(true);
    }

    public void endTurn() {
        if (!disposed) {
            getBattle().getRoundManager().endTurn();
        }
    }

    public void newCombat() {
        report("Time for my attack!");
        delayedCombat();
    }

    protected void combat() {
        setPickingTarget(false);
        setPickingAbility(false);
    }

    public void newCombatOK(CombatOK combatOK) {
        delayedTactical(combatOK);
    }

    protected void tactical(CombatOK combatOK) {
        setPickingTarget(false);
        setPickingAbility(false);
        turn(true, combatOK);
    }

    protected Token getEnemyTarget(Token attacker, boolean checkReach) { return null; }

    protected Token getAlliedTarget(Token caster, EffectType effectType) { return null; }

    protected DropTarget getDropTarget(CardType cardType) { return null; }

    public void gameOver(int winnerID) { report("GG"); }

    //EXECUTIONS + UTILITIES (ie. no logic - no need to override)

    protected void cancelTurn() {
        setPickingAbility(false);
        getBattle().getRoundManager().tryCancel();
        getBattle().getRoundManager().endTurn();
    }

    protected void endCombat() {
        getBattle().getCombatManager().endCombat();
    }

    protected void delayedTurn(boolean combat, CombatOK combatOK) {
        delayAction(()->turn(combat, combatOK), timerDelay);
    }

    protected void delayedEndTurn() {
        delayAction(this::endTurn, timerDelay);
    }

    protected void delayedCombatEnd() {
        delayAction(this::endCombat, timerDelay);
    }

    protected void delayedCombat() {
        delayAction(this::combat, timerDelay);
    }

    protected void delayedTactical(CombatOK combatOK) {
        delayAction(()-> tactical(combatOK), timerDelay);
    }

    protected void delayedDuelReady(CombatOK combatOK) {
        delayAction(()-> combatReady(combatOK), timerDelay);
    }

    protected boolean deploy(Card card, Menu menu, int position) {
        DropTarget dropTarget = getDropTarget(card.getCardInfo().getCardType());
        Token token = cardToToken(card, menu);
        if (menu instanceof HandMenu) {
            for (Token tokenInHand : ((HandMenu) menu).getTokens()) {
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

    protected boolean useAbility(Card card, Menu menu) {
        Token token = cardToToken(card, menu);
        getBattle().getRoundManager().checkAllAbilities(token, null, AbilityStarter.USE, this, null);
        return true;
    }

    protected void combatReady(CombatOK combatOK) {
        getBattle().getCombatManager().tacticalOK(combatOK);
    }

    protected Token cardToToken(Card card, Menu menu) {
        return new Token(card, getFleet().getFleetMenu().getX(), getFleet().getFleetMenu().getY(), getHand().getCardListMenu().getBattleStage(), (menu instanceof CardListMenu) ? (CardListMenu) menu : null, TokenType.FLEET, false, true); //TokenType.FLEET = default
    }

    protected void report(String message) {
        System.out.println(botTier+"(PLR"+getBattleID()+"): "+message);
    }

    protected boolean isDeploymentMenu(Menu menu) {
        return (menu instanceof FleetMenu || menu instanceof SupportMenu);
    }

    public void dispose() {
        disposed = true;
    }

    public BotTier getBotTier() { return botTier; }

    public boolean isPickingAbility() { return pickingAbility; }

    public void setPickingAbility(boolean pickingAbility) { this.pickingAbility = pickingAbility; }

    public boolean isPickingTarget() { return pickingTarget; }

    public void setPickingTarget(boolean pickingTarget) { this.pickingTarget = pickingTarget; }

    public boolean isDisposed() { return disposed; }

}
