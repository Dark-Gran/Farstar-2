package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.util.SimpleBox2;

public interface DropTarget {
    SimpleBox2 getSimpleBox2();
    default void setupSimpleBox2(float x, float y, float width, float height) {
        getSimpleBox2().setupBox(x, y, width, height);
    }

}
