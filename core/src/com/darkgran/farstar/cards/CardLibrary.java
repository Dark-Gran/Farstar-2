package com.darkgran.farstar.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class CardLibrary {
    private static CardLibrary cardLibrary = null;
    private Array<CardInfo> cards = null;

    private CardLibrary() {}

    public static CardLibrary getInstance() {
        if (cardLibrary == null) {
            cardLibrary = new CardLibrary();
        }
        return cardLibrary;
    }

    /**
     * The given Json must contain CardInfos only (a set of Arrays, Floats and Strings).
     * (Also see: AbilityManager, Effect)
     */
    public void loadLocal(String path) {
        Json json = new Json();
        cards = json.fromJson(Array.class, CardInfo.class, Gdx.files.internal(path));
    }

    public CardInfo getCard(int id) { //throws null exception unless loadLocal() called before the first getCard()
        return cards.get(id);
    }

}
