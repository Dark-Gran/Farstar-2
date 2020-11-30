package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.util.SimpleBox2;

public interface DropTarget {

    void setupSimpleBox2(float x, float y, float height, float width);
    SimpleBox2 getSimpleBox2();

}
