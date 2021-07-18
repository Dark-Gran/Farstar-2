package com.darkgran.farstar.battle.players;

public class Supports extends CardList implements BattleTicks {

    public Supports() {
        super();
        setupSize();
        clear();
    }

    @Override
    protected void setupSize() {
        setMaxSize(6);
    }

    @Override
    public boolean addCard(BattleCard battleCard) {
        if (getCardListMenu() != null && size() < getMaxSize()) {
            Support support = new Support(battleCard.getCardInfo(), battleCard.getPlayer());
            support.setUsed(true);
            add(support);
            getCardListMenu().generateNewToken(support);
            return true;
        }
        return false;
    }

    @Override
    public CardList getCardList() {
        return this;
    }

}
