package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.cards.TechType;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

/** Responsible for all shot animations. */
public class ShotManager {
    public enum ShotType {
        BULLET("shot_bullet", 4000f),
        BLAST("shot_beam", 2000f);
        private final String shotPicName;
        private final float speed;
        ShotType(String shotPicName, float speed) {
            this.shotPicName = shotPicName;
            this.speed = speed;
        }
    }
    public class AniAttack {
        public class AniShot {
            private final AniAttack aniAttack;
            private final float scale;
            private final SimpleVector2 position;
            private float timer;
            private boolean active = false;
            private boolean done = false;
            public AniShot(AniAttack aniAttack, SimpleVector2 start, float delay, float scale) {
                this.aniAttack = aniAttack;
                this.position = new SimpleVector2(start.getX(), start.getY());
                this.scale = MathUtils.clamp(scale, 1f, 10f);
                timer = delay;
            }
        }
        private final ShotType shotType;
        private final TextureRegion shotPic;
        private final Token att;
        private final Token def;
        private final int dmg;
        private final SimpleVector2 start;
        private final SimpleVector2 end;
        private final float directionX;
        private final float directionY;
        private final float angle;
        private final TechType techType;
        private ArrayList<AniShot> aniShots;
        private boolean done = false;
        public AniAttack(ShotType shotType, Token att, Token def, int dmg, SimpleVector2 start, SimpleVector2 end, float directionX, float directionY, float angle, TechType techType) {
            this.shotType = shotType;
            this.att = att;
            this.def = def;
            this.dmg = dmg;
            this.start = start;
            this.end = end;
            shotPic = new TextureRegion((Texture) Farstar.ASSET_LIBRARY.get("images/"+shotType.shotPicName+".png"));
            this.directionX = directionX;
            this.directionY = directionY;
            this.angle = angle;
            this.techType = techType;
        }
        public AniShot createAniShot(float delay, float scale) { return new AniShot(this, start, delay, scale); }
    }
    private final ArrayList<AniAttack> aniAttacks = new ArrayList<>();


    public void newAttack(Token att, Token def, int dmg, TechType techType, int numberOfShots) {
        if (att != def) {
            SimpleVector2 start = new SimpleVector2(att.getX()+att.getTokenOffense().getPad().getWidth(), att.getY()+att.getTokenOffense().getPad().getHeight()/2f);
            SimpleVector2 end = new SimpleVector2(def.getX()+def.getWidth()/2f, def.getY()+def.getHeight()/2f);
            float x = end.getX()-start.getX();
            float y = end.getY()-start.getY();
            double distance = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
            double directionX = x / distance;
            double directionY = y / distance;
            float angle = (float) Math.toDegrees(Math.atan2(y, x))+90f;
            AniAttack aniAttack = new AniAttack(techType.getShotType(), att, def, dmg, start, end, (float) directionX, (float) directionY, angle, techType);
            ArrayList<AniAttack.AniShot> aniShots = new ArrayList<>();
            if (numberOfShots < 1) { numberOfShots = 1; }
            float zoomedDamage = dmg*2f;
            float shotDamage = zoomedDamage / numberOfShots;
            if (shotDamage > 2f) { shotDamage = 2f + (shotDamage-2f)/ (techType.getShotType() == ShotType.BULLET ? 6f : 5f); }
            float scale = 0.5f + (shotDamage / 2f);
            for (int i = 0; i < numberOfShots; i++) {
                aniShots.add(aniAttack.createAniShot(i*0.4f, scale));
            }
            aniAttack.aniShots = aniShots;
            aniAttacks.add(aniAttack);
        }
    }

    public void drawAttacks(Batch batch, float delta) {
        if (aniAttacks.size() > 0) {
            ArrayList<AniAttack> forDeletion = new ArrayList<>();
            for (AniAttack aniAttack : aniAttacks) {
                if (!aniAttack.done) {
                    drawAttack(batch, delta, aniAttack);
                } else {
                    forDeletion.add(aniAttack);
                }
            }
            aniAttacks.removeAll(forDeletion);
        }
    }

    private void drawAttack(Batch batch, float delta, AniAttack aniAttack) {
        if (aniAttack.aniShots != null && aniAttack.aniShots.size() > 0) {
            ArrayList<AniAttack.AniShot> forDeletion = new ArrayList<>();
            batch.setColor(aniAttack.techType.getColor());
            for (AniAttack.AniShot aniShot : aniAttack.aniShots) {
                 updateShot(aniShot, delta);
                if (aniShot.active && !aniShot.done) {
                    batch.draw(aniAttack.shotPic, aniShot.position.getX(), aniShot.position.getY(), aniAttack.shotPic.getRegionWidth()/2f, aniAttack.shotPic.getRegionHeight()/2f, aniAttack.shotPic.getRegionWidth(), aniAttack.shotPic.getRegionHeight(), aniShot.scale, aniShot.scale, aniAttack.angle);
                } else if (aniShot.done) {
                    forDeletion.add(aniShot);
                }
            }
            batch.setColor(1, 1, 1, 1);
            aniAttack.aniShots.removeAll(forDeletion);
        } else {
            aniAttack.done = true;
        }
    }

    private void updateShot(AniAttack.AniShot aniShot, float delta) {
        if (!aniShot.active) {
            aniShot.timer -= delta;
            if (aniShot.timer <= 0f) {
                aniShot.timer = 0f;
                aniShot.active = true;
            }
        }
        if (aniShot.active && !aniShot.done) {
            double distance = Math.sqrt(Math.pow(aniShot.aniAttack.end.getX()-aniShot.position.getX(),2)+Math.pow(aniShot.aniAttack.end.getY()-aniShot.position.getY(),2));
            if (distance >= aniShot.aniAttack.shotType.speed * delta) {
                aniShot.position.setX(aniShot.position.getX() + ((aniShot.aniAttack.shotType.speed * aniShot.aniAttack.directionX) * delta));
                aniShot.position.setY(aniShot.position.getY() + ((aniShot.aniAttack.shotType.speed * aniShot.aniAttack.directionY) * delta));
            } else {
                aniShot.done = true;
            }
        }
    }

    public void dispose() {
        aniAttacks.clear();
    }

}
