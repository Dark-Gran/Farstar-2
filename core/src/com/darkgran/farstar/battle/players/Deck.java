package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.cards.CardLibrary;

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
            add(new BattleCard(CardLibrary.getInstance().getCard(ids[i]), null));
        }
        shuffle();
    }

    public Deck(boolean cheatEnabled) {
        this();
        if (cheatEnabled && Farstar.firstMatchThisLaunch) {
            firstMatchCheat();
        }
    }

    public Deck(int id) {
        super(id);
        shuffle();
    }

    private void firstMatchCheat() {
        int ix = -1;
        for (int i = 0; i < size(); i++) {
            if (get(i).getCardInfo().getName().equals("Upper Hand")) {
                ix = i;
                break;
            }
        }
        if (ix != -1 && ix >= BattleSettings.getInstance().STARTING_CARDS_DEF) {
            Collections.swap(this, 0, ix);
        }
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.getInstance().DECK_SIZE);
    }

    public BattleCard drawCard() {
        if (size() == 0) {
            eatJunk();
            shuffle();
        }
        if (size() > 0) {
            BattleCard battleCard = get(0);
            remove(0);
            getBattlePlayer().getBattle().getBattleScreen().getBattleStage().updateDeckInfos();
            return battleCard;
        } else { return null; }
    }

    public void eatJunk() {
        ArrayList<BattleCard> junkBattleCards = getBattlePlayer().getJunkpile();
        this.addAll(junkBattleCards);
        getBattlePlayer().getJunkpile().clear();
    }

    private void shuffle() {
        ArrayList<BattleCard> list = (CardList) this.clone();
        Collections.shuffle(list);
        clear();
        addAll(list);
    }

}
