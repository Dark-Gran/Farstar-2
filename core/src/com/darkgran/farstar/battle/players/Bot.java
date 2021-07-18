package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.gui.battlegui.*;
import com.darkgran.farstar.gui.tokens.HandToken;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.cards.AbilityStarter;
import com.darkgran.farstar.cards.EffectType;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.util.Delayer;

import java.util.ArrayList;

public abstract class Bot extends BattlePlayer implements BotSettings, Delayer {
    private final BotTier botTier;
    private boolean pickingTarget = false;
    private boolean pickingAbility = false;
    private boolean disposed = false;

    public Bot(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard, BotTier botTier) {
        super(battleID, energy, matter, ms, deck, yard);
        this.botTier = botTier;
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

    public void gameOver(BattlePlayer winner) { report("GG"); }

    //EXECUTIONS + UTILITIES (ie. no logic - no need to override)

    protected void cancelTurn() {
        setPickingAbility(false);
        setPickingTarget(false);
        getBattle().getRoundManager().tryCancel();
        getBattle().getRoundManager().endTurn();
    }

    protected void cancelTactical(CombatOK combatOK) {
        setPickingTarget(false);
        setPickingAbility(false);
        getBattle().getRoundManager().tryCancel();
        combatReady(combatOK);
    }

    protected void endCombat() {
        getBattle().getCombatManager().endCombat();
    }

    protected void delayedTurn(boolean combat, CombatOK combatOK) {
        delayAction(()->turn(combat, combatOK), botTier.getTimerDelay());
    }

    protected void delayedEndTurn() {
        delayAction(this::endTurn, botTier.getTimerDelay());
    }

    protected void delayedCombatEnd() {
        delayAction(this::endCombat, botTier.getTimerDelay());
    }

    protected void delayedCombat() {
        delayAction(this::combat, botTier.getTimerDelay());
    }

    protected void delayedTactical(CombatOK combatOK) {
        delayAction(()-> tactical(combatOK), botTier.getTimerDelay());
    }

    protected void delayedDuelReady(CombatOK combatOK) {
        delayAction(()-> combatReady(combatOK), botTier.getTimerDelay());
    }

    protected boolean deploy(BattleCard battleCard, Menu menu, int position) {
        DropTarget dropTarget = getDropTarget(battleCard.getCardInfo().getCardType());
        Token token = cardToToken(battleCard, menu);
        if (menu instanceof HandMenu) {
            for (Token tokenInHand : ((HandMenu) menu).getTokens()) {
                if (tokenInHand instanceof HandToken && tokenInHand.getCard() == battleCard) {
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

    protected boolean useAbility(BattleCard battleCard, Menu menu) {
        Token token = cardToToken(battleCard, menu);
        getBattle().getRoundManager().checkAllAbilities(token, null, AbilityStarter.USE, this, null);
        return true;
    }

    protected void combatReady(CombatOK combatOK) {
        getBattle().getCombatManager().tacticalOK(combatOK);
    }

    protected Token cardToToken(BattleCard battleCard, Menu menu) {
        return new Token(
                battleCard,
                getFleet().getFleetMenu().getX(),
                getFleet().getFleetMenu().getY(),
                getHand().getCardListMenu().getBattleStage(),
                (menu instanceof CardListMenu) ? (CardListMenu) menu : null,
                TokenType.FLEET,
                false,
                false
        );
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
