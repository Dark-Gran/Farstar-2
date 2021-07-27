package com.darkgran.farstar.battle;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.gui.tokens.*;
import com.darkgran.farstar.cards.AbilityInfo;
import com.darkgran.farstar.cards.AbilityStarter;
import com.darkgran.farstar.gui.battlegui.*;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.cards.CardInfo;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.gui.ActorButton;
import com.darkgran.farstar.gui.Notification;
import com.darkgran.farstar.gui.SimpleVector2;

import java.util.ArrayList;

import static com.darkgran.farstar.battle.BattleSettings.CARDS_PER_TURN;
import static com.darkgran.farstar.battle.BattleSettings.MAX_TECH_INCOME;

//in-future: depending on how other mods than 1v1 treat combat-phase, split into abstract+RoundManager1v1 might be required (probably not needed unless combat shared between allies)
public class RoundManager {
    private final Battle battle;
    private final PossibilityAdvisor possibilityAdvisor;
    private BattlePlayer startingBattlePlayer;
    private boolean launched = false;
    private boolean firstTurnThisRound;
    private int roundNum = 0;
    private boolean targetingActive;
    private final DeploymentInfo postponedDeploy = new DeploymentInfo(); //in-future: turn into a List to enable deployment-chains (eg. on-deploy summoning (targeted) that leads to another targeted on-deploy ability; atm there are no such battleCards)
    private AbilityPicker abilityPicker;
    private final ActorButton cancelButton = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("cancel"), Farstar.ASSET_LIBRARY.getAtlasRegion("cancelO")){
        @Override
        public void clicked() { tryCancel(); }
    };

    public RoundManager(Battle battle, PossibilityAdvisor possibilityAdvisor) {
        this.battle = battle;
        this.possibilityAdvisor = possibilityAdvisor;
    }

    //-------------//
    //-ROUND-FRAME-//
    //-------------//

    public void launch() {
        roundNum = 0;
        endTargeting();
        battle.startingCards();
        launched = true;
        newRound();
    }

    public void newRound() {
        roundNum++;
        getBattle().getBattleScreen().getBattleStage().getRoundCounter().update();
        System.out.println("R#"+roundNum);
        firstTurnThisRound = true;
        newTurn();
    }

    public void newTurn() {
        battle.getWhoseTurn().getHand().getNewCards(battle.getWhoseTurn().getDeck(), CARDS_PER_TURN);
        resourceIncomes(battle.getWhoseTurn());
        System.out.println("Player #"+battle.getWhoseTurn().getBattleID()+" may play his Cards.");
        if (battle.getWhoseTurn() instanceof LocalBattlePlayer) {
            possibilityAdvisor.markPossibilities(battle.getWhoseTurn(), battle);
            getBattle().getBattleScreen().getNotificationManager().newNotification(Notification.NotificationType.MIDDLE, "YOUR TURN", 3);
            getBattle().getBattleScreen().getBattleStage().getTurnButton().setDisabled(false);
        } else {
            if (battle.getWhoseTurn() instanceof Bot) {
                ((Bot) battle.getWhoseTurn()).newTurn();
            }
            if (getBattle().getBattleScreen().getBattleType() != BattleType.SIMULATION) {
                getBattle().getBattleScreen().getNotificationManager().newNotification(Notification.NotificationType.MIDDLE, "ENEMY TURN", 3);
            }
            getBattle().getBattleScreen().getBattleStage().getTurnButton().setDisabled(true);
        }
    }

    public void resourceIncomes(BattlePlayer battlePlayer) {
        int income = capIncome(getIncome());
        battlePlayer.setEnergy(income);
        battlePlayer.addMatter(income);
    }

    public int getIncome() {
        return roundNum;
    }

    public int capIncome(int income) {
        if (income > MAX_TECH_INCOME) {
            income = MAX_TECH_INCOME;
        }
        return income;
    }

    public void endTurn() {
        if (!battle.getCombatManager().isActive() && !battle.isEverythingDisabled() && !targetingActive) {
            battle.closeYards();
            getBattle().getBattleScreen().getBattleStage().getTurnButton().setDisabled(true);
            battle.getCombatManager().launchCombat();
            battle.refreshPossibilities();
        }
    }

    public void afterCombat() {
        battle.unMarkAllPossibilities();
        if (!battle.isEverythingDisabled()) {
            battle.tickEffects();
            battle.refreshUsedShips();
            battle.passTurn();
            if (firstTurnThisRound) {
                firstTurnThisRound = false;
                newTurn();
            } else {
                newRound();
            }
        }
    }

    //---------------//
    //-PLAYING-CARDS-//
    //---------------//

    public boolean processDrop(Token token, DropTarget dropTarget, int position, boolean postAbility, boolean payPrice) {
        boolean success = false;
        if (!targetingActive && !abilityPicker.isActive() && token.getCardListMenu() != null && !(dropTarget instanceof HandMenu)) {
            CardType cardType = token.getCard().getCardInfo().getCardType();
            if (!battle.activeCombatOrDuel() || cardType == CardType.TACTIC) {
                //OUTSIDE COMBAT OR TACTIC
                BattlePlayer whoseTurn = battle.getWhoseTurn();
                if (token.getCardListMenu().getBattlePlayer() == whoseTurn) {
                    if (possibilityAdvisor.isPossibleToDeploy(whoseTurn, whoseTurn, token.getCard(), false, battle)) {
                        //DEPLOYING ANYWHERE FOR SPELLS
                        if (CardType.isSpell(cardType) && !(dropTarget instanceof JunkButton)) {
                            if (!postAbility) {
                                success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                            }
                        //TARGETING SUPPORTS
                        } else if (dropTarget instanceof SupportMenu) {
                            if (((SupportMenu) dropTarget).getCardList() instanceof Supports && position != -1) {
                                Supports supports = (Supports) ((SupportMenu) dropTarget).getCardList();
                                if ((cardType == CardType.SUPPORT && supports == whoseTurn.getSupports()) || CardType.isSpell(cardType)) {
                                    if (!battle.activeCombatOrDuel()) {
                                        if (!postAbility) {
                                            success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                                        }
                                        if (cardType == CardType.SUPPORT) {
                                            if (postAbility || success) {
                                                success = supports.addCard(token.getCard());
                                            }
                                        }
                                    }
                                }
                            }
                            //TARGETING FLEET
                        } else if (dropTarget instanceof FleetMenu) {
                            if (cardType != CardType.SUPPORT) {
                                Fleet fleet = ((FleetMenu) dropTarget).getFleet();
                                if ((fleet == whoseTurn.getFleet() || !CardType.isShip(cardType)) && position != -1) {
                                    if (!battle.getCombatManager().isActive() || (!fleet.isEmpty() && battle.getCombatManager().isTacticalPhase())) { //(fleet.getShips()[position].getToken() != null && fleet.getShips()[position].isInDuel())
                                        //ABILITIES
                                        postponedDeploy.setPosition(position);
                                        if (!postAbility) {
                                            success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                                        }
                                        if (postAbility || success) {
                                            //DEPLOYMENT
                                            if (CardType.isShip(cardType)) {
                                                success = fleet.addShip(token, position);
                                            }
                                        }
                                    }
                                }
                            }
                            //TARGETING MS
                        } else if (dropTarget instanceof MothershipToken) {
                            if (!getBattle().getCombatManager().getDuelManager().isActive()) {
                                //ABILITIES
                                if (CardType.isSpell(cardType)) {
                                    if (!postAbility) {
                                        success = checkAllAbilities(token, null, AbilityStarter.DEPLOY, whoseTurn, dropTarget);
                                    }
                                }
                            }
                        }
                    }
                    //PAYMENT + DISCARD (incl. targeting discard)
                    if (success || postAbility) {
                        //System.out.println("Drop Success.");
                        if (payPrice) { whoseTurn.payday(token.getCard()); }
                        if (!CardType.isShip(cardType) && cardType != CardType.SUPPORT && cardType != CardType.MS) { token.getCard().getToken().addCardToJunk(); }
                    } else if (dropTarget instanceof JunkButton && (token instanceof DeploymentCard || token instanceof HandToken)) { //Target: Discard
                        Junkpile junkpile = ((JunkButton) dropTarget).getBattlePlayer().getJunkpile();
                        if (junkpile == whoseTurn.getJunkpile()) {
                            token.getCard().getToken().addCardToJunk();
                            success = true;
                        }
                    }
                }
            }
        }
        //HAND ONLY
        if (token instanceof DeploymentCard || token instanceof HandToken) {
            if (success || postAbility) {
                if (!(dropTarget instanceof JunkButton)) {
                    if (dropTarget != null) {
                        callHerald(token.getCard(), TokenType.HAND, new SimpleVector2(dropTarget.getSimpleBox2().x, dropTarget.getSimpleBox2().y));
                    } else {
                        callHerald(token.getCard(), TokenType.HAND, new SimpleVector2(0, 0));
                    }
                }
                token.getCard().getToken().destroy();
            }
        }
        //YARD-HERALD
        /*else if (token.getCard().getCardInfo().getCardType() == CardType.YARDPRINT) {
            if (success || postAbility) {
                callHerald(token.getCard(), token.getTokenType(), new SimpleVector2(token.getX(), token.getY()));
            }
        }*/
        battle.refreshPossibilities();
        return success;
    }

    public boolean checkAllAbilities(Token caster, Token target, AbilityStarter abilityStarter, BattlePlayer owner, DropTarget dropTarget) {
        if (abilityStarter != AbilityStarter.USE || !caster.getCard().isUsed()) {
            CardInfo cardInfo = caster.getCard().getCardInfo();
            ArrayList<AbilityInfo> options = new ArrayList<>();
            for (int i = 0; i < cardInfo.getAbilities().size(); i++) {
                if (cardInfo.getAbilities().get(i) != null) {
                    if (cardInfo.getAbilities().get(i).getStarter() == abilityStarter) {
                        AbilityInfo abilityInfo = cardInfo.getAbilities().get(i);
                        if (owner.canAfford(abilityInfo.getResourcePrice().getEnergy(), abilityInfo.getResourcePrice().getMatter())) {
                            options.add(abilityInfo);
                        }
                    }
                }
            }
            if (options.size() > 0) {
                if (options.size() == 1 && (!options.get(0).isPurelyOffensiveChange() || (target == null || !target.getCard().isMS()))) {
                    return playAbility(caster, (target != null) ? target.getCard() : null, abilityStarter, owner, dropTarget, options.get(0));
                } else if (options.size() > 1) {
                    if (target != null && target.getCard().isMS()) {
                        for (AbilityInfo option : options) {
                            if (option.isPurelyOffensiveChange()) {
                                return false;
                            }
                        }
                    }
                    askForAbility(caster, target, dropTarget, options);
                }
                return false;
            }
        }
        return true;
    }

    public void askForAbility(Token caster, Token target, DropTarget dropTarget, ArrayList<AbilityInfo> options) {
        postponedDeploy.saveInDeployment(caster, null, dropTarget, target);
        if (battle.getWhoseTurn() instanceof Bot) {
            ((Bot) battle.getWhoseTurn()).pickAbility(caster, target, dropTarget, options);
        } else if (abilityPicker != null) {
            caster.setPicked(true);
            abilityPicker.setAbilityInfos(new ArrayList<>());
            for (AbilityInfo option : options) {
                abilityPicker.getAbilityInfos().add(option);
            }
            abilityPicker.enable(caster.getCard());
            cancelButton.setPosition(Farstar.STAGE_WIDTH*0.69f, Farstar.STAGE_HEIGHT*0.28f);
            getBattle().getBattleScreen().getBattleStage().addActor(cancelButton);
            getBattle().getBattleScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Choose an Ability.", 3);
            ((YardMenu) caster.getCard().getBattlePlayer().getYard().getCardListMenu()).setOpen(false);
        }
    }

    public boolean playAbility(Token caster, BattleCard target, AbilityStarter abilityStarter, BattlePlayer owner, DropTarget dropTarget, AbilityInfo ability) {
        boolean success = battle.getAbilityManager().playAbility(caster, target, ability, dropTarget);
        if (success) {
            if (owner != null) { owner.payday(ability.getResourcePrice().getEnergy(), ability.getResourcePrice().getMatter()); }
            if (abilityStarter == AbilityStarter.USE) {
                caster.getCard().setUsed(true);
            }
        }
        return success;
    }

    public void processPick(AbilityInfo ability) {
        if (!battle.isEverythingDisabled() && postponedDeploy.getCaster() != null) {
            if (abilityPicker != null) {
                abilityPicker.disable();
                cancelButton.remove();
            }
            if (playAbility(postponedDeploy.getCaster(), (postponedDeploy.getTarget()!=null) ? postponedDeploy.getTarget().getCard() : null, ability.getStarter(), postponedDeploy.getCaster().getCard().getBattlePlayer(), postponedDeploy.getDrop(), ability)) {
                processDrop(postponedDeploy.getCaster(), postponedDeploy.getDrop(), postponedDeploy.getPosition(), true, ability.getStarter()==AbilityStarter.DEPLOY);
                postponedDeploy.resetInDeployment();
                battle.refreshPossibilities();
            }
        }
    }

    public void askForTargets(Token token, AbilityInfo ability, DropTarget dropTarget) {
        targetingActive = true;
        postponedDeploy.saveInDeployment(token, ability, dropTarget, null);
        System.out.println("Need a Target.");
        BattlePlayer whoseTurn = battle.getWhoseTurn();
        if (whoseTurn instanceof Bot) {
            ((Bot) whoseTurn).chooseTargets(token, ability);
        } else {
            token.setPicked(true);
            getBattle().getBattleScreen().switchCursor(SuperScreen.CursorType.AIM);
            cancelButton.setPosition(Farstar.STAGE_WIDTH*0.62f, Farstar.STAGE_HEIGHT*0.08f);
            getBattle().getBattleScreen().getBattleStage().addActor(cancelButton);
            getBattle().getBattleScreen().getNotificationManager().newNotification(Notification.NotificationType.BOT_LEFT, "Choose a Target.", 3);
            ((YardMenu) whoseTurn.getYard().getCardListMenu()).setOpen(false);
        }
    }

    public void processClick(Token token, BattlePlayer owner) {
        getBattle().getBattleScreen().hideScreenConceder();
        if (!abilityPicker.isActive() && (!battle.activeCombatOrDuel() || (battle.getCombatManager().isTacticalPhase() && targetingActive)) && !battle.isEverythingDisabled() && getBattle().getWhoseTurn() instanceof LocalBattlePlayer) {
            if (targetingActive) {
                processTarget(token);
            } else if (owner == battle.getWhoseTurn() && token != null && possibilityAdvisor.hasPossibleAbility(owner, token.getCard())) {
                checkAllAbilities(token, null, AbilityStarter.USE, owner, null);
                battle.refreshPossibilities();
            }
        }
    }

    public void processTarget(Token target) {
        if (targetingActive && postponedDeploy.getCaster() != null) {
            if (AbilityManager.validAbilityTarget(postponedDeploy.getAbility(), postponedDeploy.getCaster().getCard(), target.getCard())) {
                //System.out.println("Playing ability...");
                if (battle.getAbilityManager().playAbility(postponedDeploy.getCaster(), target.getCard(), postponedDeploy.getAbility(), postponedDeploy.getDrop())) {
                    System.out.println("Targeted-Ability Success!");
                    if (postponedDeploy.getAbility().getStarter() == AbilityStarter.USE) {
                        battle.getWhoseTurn().payday(postponedDeploy.getAbility().getResourcePrice().getEnergy(), postponedDeploy.getAbility().getResourcePrice().getMatter());
                        postponedDeploy.getCaster().getCard().setUsed(true);
                        //System.out.println("Ability price paid.");
                    } else {
                        //System.out.println("Reprocessing original drop...");
                        targetingActive = false;
                        getBattle().getBattleScreen().switchCursor(SuperScreen.CursorType.DEFAULT);
                        processDrop(postponedDeploy.getCaster(), postponedDeploy.getDrop(), postponedDeploy.getPosition(), true, postponedDeploy.getAbility().getStarter()==AbilityStarter.DEPLOY);
                        if (target.getCard() != null && battle.getCombatManager().isTacticalPhase() && postponedDeploy.getCaster().getCard().getCardInfo().getCardType() == CardType.TACTIC) {
                            battle.getCombatManager().saveTactic(postponedDeploy.getCaster().getCard(), target.getCard());
                        }
                    }
                    endTargeting();
                }
                battle.refreshPossibilities();
            } else {
                System.out.println("Invalid Target.");
            }
        }
    }

    public void tryCancel() {
        if (targetingActive) {
            endTargeting();
            System.out.println("Targeting Cancelled.");
        }
        if (abilityPicker.isActive()) {
            abilityPicker.disable();
            postponedDeploy.resetInDeployment();
            cancelButton.remove();
        }
        if (!getBattle().getCombatManager().getDuelManager().isActive()) {
            battle.refreshPossibilities();
        }
    }

    private void endTargeting() {
        targetingActive = false;
        getBattle().getBattleScreen().switchCursor(SuperScreen.CursorType.DEFAULT);
        postponedDeploy.resetInDeployment();
        cancelButton.remove();
    }

    private void callHerald(BattleCard battleCard, TokenType targetType, SimpleVector2 targetXY) {
        getBattle().getBattleScreen().getBattleStage().getHerald().enable(battleCard, targetType, targetXY);
    }

    //-----------//
    //-UTILITIES-//
    //-----------//

    public boolean isTokenMoveEnabled(Token token) { //Turn and Tactical Only
        if (token.getCard() != null && token.getCard().getBattlePlayer() instanceof LocalBattlePlayer && RoundManager.ownsToken(getBattle().getWhoseTurn(), token) && (!getBattle().getCombatManager().isActive() || (getBattle().getCombatManager().isTacticalPhase() && token.getCard().isTactic()))) {
            return areMovesEnabled();
        }
        return false;
    }

    public boolean isCombatMoveEnabled(Token token) { //Duel-Picking Only
        if (token.getCard() != null && token.getCard().getBattlePlayer() instanceof LocalBattlePlayer && RoundManager.ownsToken(getBattle().getWhoseTurn(), token) && getBattle().getCombatManager().isActive() && !getBattle().getCombatManager().isTacticalPhase()) {
            return areMovesEnabled();
        }
        return false;
    }

    public boolean areMovesEnabled() {
        return !getBattle().isEverythingDisabled() && !isTargetingActive() && !getAbilityPicker().isActive() && !getBattle().getCombatManager().getDuelManager().isActive();
    }

    public static boolean ownsToken(BattlePlayer battlePlayer, Token token) {
        if (token instanceof TargetingToken)  {
            return battlePlayer == ((FleetToken) token.getCard().getToken()).getFleetMenu().getBattlePlayer();
        } else if (token instanceof FleetToken) {
            return battlePlayer == ((FleetToken) token).getFleetMenu().getBattlePlayer();
        } else {
            return battlePlayer == token.getCardListMenu().getBattlePlayer();
        }
    }

    public int getRoundNum() { return roundNum; }

    public Battle getBattle() { return battle; }

    public boolean isFirstTurnThisRound() { return firstTurnThisRound; }

    public boolean isTargetingActive() { return targetingActive; }

    public boolean isLaunched() { return launched; }

    public AbilityPicker getAbilityPicker() { return abilityPicker; }

    public void setAbilityPicker(AbilityPicker abilityPicker) { this.abilityPicker = abilityPicker; }

    public PossibilityAdvisor getPossibilityAdvisor() { return possibilityAdvisor; }

    public BattlePlayer getStartingPlayer() { return startingBattlePlayer; }

    protected void setStartingPlayer(BattlePlayer startingBattlePlayer) { this.startingBattlePlayer = startingBattlePlayer; }
}
