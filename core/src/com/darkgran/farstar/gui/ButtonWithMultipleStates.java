package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class ButtonWithMultipleStates extends ActorButton {
    private final TextureRegion[] regions;
    private final boolean noPressedTexture;
    private int state = 0;

    public ButtonWithMultipleStates(ArrayList<TextureRegion> textureRegions, boolean noPressedTexture) {
        super(textureRegions.get(0), textureRegions.get(1));
        this.noPressedTexture = noPressedTexture;
        if (!noPressedTexture) {
            setImageDown(textureRegions.get(2));
        }
        regions = new TextureRegion[textureRegions.size()];
        for (int i = regions.length-1; i >= 0; i--) {
            regions[i] = textureRegions.get(i);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (state > 0) {
            int ix;
            if (getClickListener().isPressed()) {
                ix = state*(noPressedTexture ? 2 : 3)+(noPressedTexture ? 1 : 2);
            } else if (getClickListener().isOver()) {
                ix = state*(noPressedTexture ? 2 : 3)+1;
            } else {
                ix = state*(noPressedTexture ? 2 : 3);
            }
            if (regions[ix] != null) {
                batch.draw(regions[ix], getX(), getY());
            }
        } else {
            super.draw(batch, parentAlpha);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
