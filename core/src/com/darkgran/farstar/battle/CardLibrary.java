package com.darkgran.farstar.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class CardLibrary {
    private Array<CardInfo> cards;

    public void loadLocal(String path) {
        Json json = new Json();
        cards = json.fromJson(Array.class, CardInfo.class, Gdx.files.internal(path) );
    }

    public CardInfo getCard(int id) {
        return cards.get(id);
    }

}
