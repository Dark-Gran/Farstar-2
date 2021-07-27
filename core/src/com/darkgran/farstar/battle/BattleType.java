package com.darkgran.farstar.battle;

public enum BattleType {
    SKIRMISH("Skirmish"),
    SIMULATION("Simulation"),
    SOLITARY("Solitary");

    private final String name;

    BattleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
