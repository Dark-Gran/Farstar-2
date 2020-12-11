package com.darkgran.farstar.battle.players;

import java.util.ArrayList;

public class Supports extends CardList {

    public Supports() {
        setupSize();
        setCards(new ArrayList<>());
    }

    @Override
    public void setupSize() {
        setMaxSize(6);
    }
}
