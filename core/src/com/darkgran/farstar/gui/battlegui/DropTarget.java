package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.gui.SimpleBox2;

public interface DropTarget {
    SimpleBox2 getSimpleBox2();
    default void setupSimpleBox2(float x, float y, float width, float height) {
        getSimpleBox2().setupBox(x, y, width, height);
    }
    default boolean isActive() {
        return true;
    }

}
