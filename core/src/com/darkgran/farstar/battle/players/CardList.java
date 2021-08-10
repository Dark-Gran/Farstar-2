package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.cards.CardLibrary;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

import java.util.ArrayList;

public abstract class CardList extends ArrayList<BattleCard> {
    private BattlePlayer battlePlayer;
    private int maxSize;
    private CardListMenu cardListMenu;

    protected void setupSize() {
        setMaxSize(0);
    }

    public CardList(ArrayList<BattleCard> battleCards) {
        super();
        setupSize();
        addAll(battleCards);
    }

    public CardList() {
        super();
        setupSize();
        clear();
        for (int i = 0; i < maxSize; i++) {
            add(new BattleCard(CardLibrary.getInstance().getCard(2), null));
        }
    }

    public CardList(int id) {
        super();
        setupSize();
        clear();
        for (int i = 0; i < maxSize; i++) {
            add(new BattleCard(CardLibrary.getInstance().getCard(id), null));
        }
    }

    public void setPlayerOnAll(BattlePlayer battlePlayer) {
        setBattlePlayer(battlePlayer);
        for (BattleCard battleCard : this) {
            battleCard.setBattlePlayer(battlePlayer);
        }
    }

    public boolean addCard(BattleCard battleCard) {
        if (cardListMenu != null && size() < maxSize) {
            add(battleCard);
            cardListMenu.generateNewToken(battleCard);
            return true;
        }
        return false;
    }

    public void receiveCardListMenu(CardListMenu cardListMenu) {
        this.cardListMenu = cardListMenu;
    }

    public boolean hasSpace() {
        return size() < getMaxSize();
    }

    public int getMaxSize() { return maxSize; }

    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }

    public CardListMenu getCardListMenu() { return cardListMenu; }

    public BattlePlayer getBattlePlayer() { return battlePlayer; }

    public void setBattlePlayer(BattlePlayer battlePlayer) { this.battlePlayer = battlePlayer; }

}
