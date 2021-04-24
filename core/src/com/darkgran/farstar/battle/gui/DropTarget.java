package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.util.SimpleBox2;

public interface DropTarget {
    SimpleBox2 getSimpleBox2();
    default void setupSimpleBox2(float x, float y, float height, float width) {
        getSimpleBox2().setup(x, y, height, width);
    }

}
