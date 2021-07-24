package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.cards.TechType;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

/** Responsible for all shot animations. */
public class ShotManager {
    public enum ShotType {
        BULLET("shot-bullet", 4000f),
        BLAST("shot-beam", 2000f);
        private final String shotPicName;
        private final float speed;
        ShotType(String shotPicName, float speed) {
            this.shotPicName = shotPicName;
            this.speed = speed;
        }
    }
    public class AniAttack {
        public class AniShot {
            private class AniShrapnel {
                private final TextureRegion shrapnelPic = Farstar.ASSET_LIBRARY.getAtlasRegion("shrapnel");
                private final SimpleVector2 origin;
                private final SimpleVector2 speed;
                private SimpleVector2 position;
                private float rotation;
                private float alpha = 1f;
                private AniShrapnel(SimpleVector2 origin, SimpleVector2 speed, float startingRotation) {
                    this.origin = origin;
                    this.speed = speed;
                    rotation = startingRotation;
                    position = new SimpleVector2(origin.x, origin.y);
                }
                private void update(float delta) {
                    alpha -= delta*4;
                    position.x += speed.x * delta;
                    position.y += speed.y * delta;
                    speed.x -= delta;
                    speed.y -= delta;
                    rotation += delta;
                    if (alpha <= 0) {
                        done = true;
                    }
                }
                private void draw(Batch batch) {
                    batch.setColor(ColorPalette.changeAlpha(techType.getColor(), alpha));
                    batch.draw(shrapnelPic, position.x, position.y, shrapnelPic.getRegionWidth() / 2f, shrapnelPic.getRegionHeight() / 2f, shrapnelPic.getRegionWidth(), shrapnelPic.getRegionHeight(), scale, scale, angle);
                    //batch.setColor(1, 1, 1, 1);
                }
            }
            private final AniAttack aniAttack;
            private final float scale;
            private final SimpleVector2 position;
            private float timer;
            private boolean active = false;
            private ArrayList<AniShrapnel> shrapnels = null;
            private boolean done = false;
            private AniShot(AniAttack aniAttack, SimpleVector2 start, float delay, float scale) {
                this.aniAttack = aniAttack;
                this.position = new SimpleVector2(start.x, start.y);
                this.scale = MathUtils.clamp(scale, 1f, 10f);
                timer = delay;
            }
            private void update(float delta) {
                if (!active) {
                    timer -= delta;
                    if (timer <= 0f) {
                        timer = 0f;
                        active = true;
                    }
                }
                if (active && !done) {
                    if (shrapnels == null) {
                        double distance = Math.sqrt(Math.pow(aniAttack.end.x - position.x, 2) + Math.pow(aniAttack.end.y - position.y, 2));
                        if (distance >= aniAttack.shotType.speed * delta) {
                            position.x = position.x + ((aniAttack.shotType.speed * aniAttack.directionX) * delta);
                            position.y = position.y + ((aniAttack.shotType.speed * aniAttack.directionY) * delta);
                        } else {
                            shrapnels = new ArrayList<>();
                            shrapnels.add(new AniShrapnel(end, new SimpleVector2(100f, 100f), 0f));
                            shrapnels.add(new AniShrapnel(end, new SimpleVector2(-100f, 100f), 0f));
                            shrapnels.add(new AniShrapnel(end, new SimpleVector2(-100f, -100f), 0f));
                        }
                    } else {
                        for (AniShrapnel shrapnel : shrapnels) {
                            shrapnel.update(delta);
                        }
                    }
                }
            }
            private void draw(Batch batch) {
                batch.draw(shotPic, position.x, position.y, shotPic.getRegionWidth() / 2f, shotPic.getRegionHeight() / 2f, shotPic.getRegionWidth(), shotPic.getRegionHeight(), scale, scale, angle);
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
            shotPic = new TextureRegion(Farstar.ASSET_LIBRARY.getAtlasRegion(shotType.shotPicName));
            this.directionX = directionX;
            this.directionY = directionY;
            this.angle = angle;
            this.techType = techType;
        }
        public AniShot createAniShot(float delay, float scale) {
            return new AniShot(this, start, delay, scale);
        }
        public void draw(Batch batch, float delta) {
            if (aniShots != null && aniShots.size() > 0) {
                ArrayList<AniAttack.AniShot> forDeletion = new ArrayList<>();
                batch.setColor(techType.getColor());
                for (AniAttack.AniShot aniShot : aniShots) {
                    if (aniShot.active && aniShot.shrapnels==null) {
                        aniShot.draw(batch);
                    } else if (aniShot.shrapnels != null && !aniShot.done) {
                        for (AniShot.AniShrapnel shrapnel : aniShot.shrapnels) {
                            shrapnel.draw(batch);
                        }
                    } else if (aniShot.done) {
                        forDeletion.add(aniShot);
                    }
                    aniShot.update(delta);
                }
                batch.setColor(1, 1, 1, 1);
                aniShots.removeAll(forDeletion);
            } else {
                done = true;
            }
        }
    }
    private final ArrayList<AniAttack> aniAttacks = new ArrayList<>();


    public void newAttack(Token att, Token def, int dmg, TechType techType, int numberOfShots) {
        if (att != def) {
            SimpleVector2 start = new SimpleVector2(att.getX()+att.getTokenOffense().getPad().getRegionWidth(), att.getY()+att.getTokenOffense().getPad().getRegionHeight()/2f);
            SimpleVector2 end = new SimpleVector2(def.getX()+def.getWidth()/2f, def.getY()+def.getHeight()/2f);
            float x = end.x-start.x;
            float y = end.y-start.y;
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
                    aniAttack.draw(batch, delta);
                } else {
                    forDeletion.add(aniAttack);
                }
            }
            aniAttacks.removeAll(forDeletion);
        }
    }

    public void dispose() {
        aniAttacks.clear();
    }

}
