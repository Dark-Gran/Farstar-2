package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

/** Responsible for all shot animations. */
public class ShotManager {
    private enum ShotType {
        BULLET("shot_kinetic", 200f); //5000f
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
            private SimpleVector2 position;
            private float timer;
            private boolean active = false;
            private boolean done = false;
            public AniShot(AniAttack aniAttack, SimpleVector2 start, float delay) {
                this.aniAttack = aniAttack;
                this.position = new SimpleVector2(start.getX(), start.getY());
                timer = delay;
            }
        }
        private final ShotType shotType;
        private final Texture shotPic;
        private final Token att;
        private final Token def;
        private final int dmg;
        private final SimpleVector2 start;
        private final SimpleVector2 end;
        private final float directionX;
        private final float directionY;
        private ArrayList<AniShot> aniShots;
        private boolean done = false;
        public AniAttack(ShotType shotType, Token att, Token def, int dmg, SimpleVector2 start, SimpleVector2 end, float directionX, float directionY) {
            this.shotType = shotType;
            this.att = att;
            this.def = def;
            this.dmg = dmg;
            this.start = start;
            this.end = end;
            shotPic = Farstar.ASSET_LIBRARY.get("images/"+shotType.shotPicName+".png");
            this.directionX = directionX;
            this.directionY = directionY;
        }
        public AniShot createAniShot(float delay) { return new AniShot(this, start, delay); }
    }
    private final ArrayList<AniAttack> aniAttacks = new ArrayList<>();


    public void newAttack(Token att, Token def, int dmg) {
        if (att != def) {
            SimpleVector2 start = new SimpleVector2(att.getX()+att.getTokenOffense().getPad().getWidth()/2f, att.getY()+att.getTokenOffense().getPad().getHeight()/2f);
            SimpleVector2 end = new SimpleVector2(def.getX()+def.getWidth()-def.getTokenDefense().getPad().getWidth()/2f, def.getY()+def.getTokenDefense().getPad().getHeight()/2f);
            double distance = Math.sqrt(Math.pow(end.getX()-start.getX(),2)+Math.pow(end.getY()-start.getY(),2));
            double directionX = (end.getX()-start.getX()) / distance;
            double directionY = (end.getY()-start.getY()) / distance;
            AniAttack aniAttack = new AniAttack(ShotType.BULLET, att, def, dmg, start, end, (float) directionX, (float) directionY);
            ArrayList<AniAttack.AniShot> aniShots = new ArrayList<>();
            for (int i = 0; i < att.getCard().getCardInfo().getAnimatedShots(); i++) {
                aniShots.add(aniAttack.createAniShot(i*0.4f));
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
            for (AniAttack.AniShot aniShot : aniAttack.aniShots) {
                 updateShot(aniShot, delta);
                if (aniShot.active && !aniShot.done) {
                    batch.draw(aniAttack.shotPic, aniShot.position.getX(), aniShot.position.getY());
                } else if (aniShot.done) {
                    forDeletion.add(aniShot);
                }
            }
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
            if (distance > 10f) {
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
