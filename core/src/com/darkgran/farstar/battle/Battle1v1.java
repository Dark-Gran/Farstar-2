package com.darkgran.farstar.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.gui.battlegui.*;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.gui.Notification;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class Battle1v1 extends Battle {
    private final BattlePlayer battlePlayer1;
    private final BattlePlayer battlePlayer2;

    public Battle1v1(@NotNull BattlePlayer battlePlayer1, @NotNull BattlePlayer battlePlayer2, @NotNull BattleType battleType) {
        super(battleType);
        this.battlePlayer1 = battlePlayer1;
        this.battlePlayer2 = battlePlayer2;
        this.battlePlayer1.setBattle(this);
        this.battlePlayer2.setBattle(this);
        this.battlePlayer1.getMs().setBattlePlayer(battlePlayer1);
        this.battlePlayer1.getDeck().setPlayerOnAll(battlePlayer1);
        this.battlePlayer1.getYard().setPlayerOnAll(battlePlayer1);
        this.battlePlayer2.getMs().setBattlePlayer(battlePlayer2);
        this.battlePlayer2.getDeck().setPlayerOnAll(battlePlayer2);
        this.battlePlayer2.getYard().setPlayerOnAll(battlePlayer2);
    }

    @Override
    public BattleStage createBattleStage(@NotNull Farstar game, @NotNull Viewport viewport, @NotNull BattleScreen battleScreen) {
        return new BattleStage1V1(game, viewport, battleScreen, new CombatMenu1V1(getCombatManager()), battlePlayer1, battlePlayer2);
    }

    @Override
    public CombatManager createCombatManager() {
        return new CombatManager1v1(this, new DuelManager());
    }

    @Override
    protected void coinToss() {
        if (Farstar.firstMatchThisLaunch && getBattleType() == BattleType.SKIRMISH) {
            setWhoseTurn(battlePlayer1);
            Farstar.firstMatchThisLaunch = false;
        } else {
            setWhoseTurn((ThreadLocalRandom.current().nextInt(0, 2) == 0) ? battlePlayer1 : battlePlayer2);
        }
    }

    @Override
    public void passTurn() {
        setWhoseTurn(battlePlayer1 == getWhoseTurn() ? battlePlayer2 : battlePlayer1);
    }

    @Override
    public boolean areYardsOpen() {
        return ((YardMenu) (battlePlayer1.getShipyard().getCardListMenu())).isOpen() || ((YardMenu) (battlePlayer2.getShipyard().getCardListMenu())).isOpen();
    }

    @Override
    public void closeYards() {
        ((YardMenu) (battlePlayer1.getShipyard().getCardListMenu())).switchVisibility(false);
        ((YardMenu) (battlePlayer2.getShipyard().getCardListMenu())).switchVisibility(false);
    }

    @Override
    public void unMarkAllPossibilities() {
        getRoundManager().getPossibilityAdvisor().unMarkAll(battlePlayer1, this);
        getRoundManager().getPossibilityAdvisor().unMarkAll(battlePlayer2, this);
    }

    @Override
    protected void startingCards() {
        if (getWhoseTurn() == battlePlayer1) {
            battlePlayer1.getHand().getNewCards(battlePlayer1.getDeck(), BattleSettings.getInstance().STARTING_CARDS_ATT);
            battlePlayer2.getHand().getNewCards(battlePlayer2.getDeck(), BattleSettings.getInstance().STARTING_CARDS_DEF);
            battlePlayer2.getHand().getNewCards(BattleSettings.getInstance().BONUS_CARD_ID, 1, battlePlayer2);
        } else {
            battlePlayer1.getHand().getNewCards(battlePlayer1.getDeck(), BattleSettings.getInstance().STARTING_CARDS_DEF);
            battlePlayer1.getHand().getNewCards(BattleSettings.getInstance().BONUS_CARD_ID, 1, battlePlayer1);
            battlePlayer2.getHand().getNewCards(battlePlayer2.getDeck(), BattleSettings.getInstance().STARTING_CARDS_ATT);
        }
    }

    @Override
    public void addGameOver(BattlePlayer battlePlayer) {
        getGameOvers().add(battlePlayer);
        battleEnd();
    }

    @Override
    public void battleEnd() {
        super.battleEnd();
        System.out.println("Player #" + getWinner().getBattleID() + " wins!");
        if (battlePlayer1 instanceof Bot) { ((Bot) battlePlayer1).gameOver(getWinner()); }
        else { battleEndNotification(battlePlayer1); }
        if (battlePlayer2 instanceof Bot) { ((Bot) battlePlayer2).gameOver(getWinner()); }
        else { battleEndNotification(battlePlayer2); }
    }

    protected void battleEndNotification(BattlePlayer battlePlayer) {
        if (battlePlayer == getWinner()) {
            NotificationManager.getInstance().newNotification(Notification.NotificationType.MIDDLE, "VICTORY", 4);
        } else {
            NotificationManager.getInstance().newNotification(Notification.NotificationType.MIDDLE, "DEFEAT", 4);
        }
    }

    private BattlePlayer getWinner() {
        if (getGameOvers().contains(battlePlayer1)) {
            return battlePlayer2;
        } else {
            return battlePlayer1;
        }
    }

    @Override
    public void tickEffects() {
        super.tickEffects();
        battlePlayer1.getMs().checkEffects(getAbilityManager());
        battlePlayer1.getFleet().checkEffectsOnAll(getAbilityManager());
        battlePlayer1.getSupports().checkEffectsOnAll(getAbilityManager());
        battlePlayer2.getMs().checkEffects(getAbilityManager());
        battlePlayer2.getFleet().checkEffectsOnAll(getAbilityManager());
        battlePlayer2.getSupports().checkEffectsOnAll(getAbilityManager());
    }

    @Override
    public BattlePlayer[] getEnemies(BattlePlayer battlePlayer) {
        if (battlePlayer == battlePlayer1) {
            return new BattlePlayer[]{battlePlayer2};
        } else {
            return new BattlePlayer[]{battlePlayer1};
        }
    }

    @Override
    public BattlePlayer[] getAllies(BattlePlayer battlePlayer) {
        if (battlePlayer == battlePlayer1) {
            return new BattlePlayer[]{battlePlayer1};
        } else {
            return new BattlePlayer[]{battlePlayer2};
        }
    }

    @Override
    public void dispose() {
        if (battlePlayer1 instanceof Bot) { ((Bot) battlePlayer1).dispose(); }
        if (battlePlayer2 instanceof Bot) { ((Bot) battlePlayer2).dispose(); }
    }
}
