package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface Draggable {
    Dragger getDragger();
    void setDragger(Dragger dragger);
    Actor getActor();
}
