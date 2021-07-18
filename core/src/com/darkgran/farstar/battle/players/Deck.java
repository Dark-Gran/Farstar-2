package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends CardList {

    public Deck(ArrayList<BattleCard> battleCards) {
        super(battleCards);
        shuffle();
    }

    public Deck() {
        setupSize();
        clear();
        int[] ids = new int[]{
                6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
                6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20,
                19, 19
        };
        for (int i = 0; i < getMaxSize(); i++) {
            add(new BattleCard(Farstar.CARD_LIBRARY.getCard(ids[i]), null));
        }
        shuffle();
    }

    public Deck(int id) {
        super(id);
        shuffle();
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE);
    }

    public BattleCard drawCard() {
        if (size() == 0) {
            eatJunk();
            shuffle();
        }
        if (size() > 0) {
            BattleCard battleCard = get(0);
            remove(0);
            getPlayer().getBattle().getBattleScreen().getBattleStage().updateDeckInfos();
            return battleCard;
        } else { return null; }
    }

    public void eatJunk() {
        ArrayList<BattleCard> junkBattleCards = getPlayer().getJunkpile();
        for (BattleCard junk : junkBattleCards) { add(junk); }
        getPlayer().getJunkpile().clear();
    }

    private void shuffle() {
        ArrayList<BattleCard> list = (ArrayList<BattleCard>) this.clone();
        Collections.shuffle(list);
        clear();
        addAll(list);
    }

}
