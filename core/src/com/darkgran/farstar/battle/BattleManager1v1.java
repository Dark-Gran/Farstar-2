package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.GUI;
import com.darkgran.farstar.battle.gui.GUI1v1;

public class BattleManager1v1 implements BattleManager, BattleSettings {
    private final GUI1v1 gui1v1;
    private final Player player1;
    private final Player player2;
    private int roundNum = 1;

    public BattleManager1v1() {
        System.out.println("Launching Battle.");
        player1 = new Player((byte) 1, STARTING_ENERGY, STARTING_MATTER);
        player2 = new Player((byte) 2, STARTING_ENERGY, STARTING_MATTER);
        gui1v1 = new GUI1v1(player1, player2);
    }

    @Override
    public GUI getGui() { return gui1v1; }

}
