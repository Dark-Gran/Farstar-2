package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.tokens.TokenType;

import java.util.ArrayList;

/**
 * Handles all png-animations except for Attacks/Shots (see ShotManager).
 */
public class AnimationManager {
    private class DeathEffect {
        private final TextureRegion deathPic;
        private final float x;
        private final float y;
        private final float originX;
        private final float originY;
        private boolean finished = false;
        private float alpha = 1f;
        private float scale = 1f;
        public DeathEffect(TextureRegion deathPic, float x, float y) {
            this.deathPic = deathPic;
            this.x = x;
            this.y = y;
            this.originX = deathPic.getRegionWidth()/2f;
            this.originY = deathPic.getRegionHeight()/2f;
        }
        public void draw(Batch batch, float delta) {
            if (alpha > 0) {
                batch.setColor(1, 1, 1, alpha);
                batch.draw(deathPic, x, y, originX, originY, deathPic.getRegionWidth(), deathPic.getRegionHeight(), scale, scale, 0);
                batch.setColor(1, 1, 1, 1);
                alpha -= delta*2;
                scale += delta;
            } else {
                finished = true;
            }
        }
    }
    private ArrayList<DeathEffect> deathEffects = new ArrayList<>();

    public void newDeathEffect(float x, float y, TokenType tokenType) {
        Texture texture = Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("images/tokens/death_", tokenType, false)+".png");
        deathEffects.add(new DeathEffect(new TextureRegion(texture), x+tokenType.getWidth()/2f-texture.getWidth()/2f, y+tokenType.getHeight()/2f-texture.getHeight()/2f));
    }

    public void draw(Batch batch, float delta) {
        ArrayList<DeathEffect> forDeletion = new ArrayList<>();
        for (DeathEffect deathEffect : deathEffects) {
            if (!deathEffect.finished) {
                deathEffect.draw(batch, delta);
            } else {
                forDeletion.add(deathEffect);
            }
        }
        for (DeathEffect deathEffect : forDeletion) {
            deathEffects.remove(deathEffect);
        }
    }

    public void dispose() {
        deathEffects.clear();
    }

}
