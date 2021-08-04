package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.gui.SimpleImage2;

/**
 *  "Game Rules Summary" (under F1), or the explanation of BattleStage (ie. must be different for each mode (eg. 1v1, 2v2)).
 *  Separate classes to support other features than just "a different image" for each mode.
 */
public abstract class BattleHelp {
    private SimpleImage2 image;
    private boolean enabled = false;

    public void draw(Batch batch) {
        if (enabled && image != null) {
            image.draw(batch);
        }
    }

    public SimpleImage2 getImage() {
        return image;
    }

    public void setImage(SimpleImage2 image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
