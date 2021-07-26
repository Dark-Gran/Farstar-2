package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.cards.TechType;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/** Responsible for all shot animations. (see AnimationManager for other) */
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
            private class AniRecoil {
                private final SimpleVector2 position;
                private float scaleX;
                private float scaleY;
                private float alpha = 1f;
                public AniRecoil(SimpleVector2 position, float scale) {
                    this.scaleX = scale;
                    this.scaleY = scale;
                    this.position = new SimpleVector2(position.x-recoilPic.getRegionWidth() / 2f, position.y-recoilPic.getRegionHeight() / 2f);
                }
                private void update(float delta) {
                    alpha -= delta*3f;
                    scaleX += delta*10f;
                    scaleY += delta;
                    if (alpha <= 0) {
                        recoilFinished = true;
                    }
                }
                private void draw(Batch batch) {
                    batch.setColor(ColorPalette.changeAlpha(techType.getColor(), alpha));
                    batch.draw(recoilPic, position.x, position.y, recoilPic.getRegionWidth() / 2f, recoilPic.getRegionHeight() / 2f, recoilPic.getRegionWidth(), recoilPic.getRegionHeight(), scaleX, scaleY, 0);
                    batch.setColor(1, 1, 1, 1);
                }
            }
            private class AniShrapnel {
                private final SimpleVector2 speed;
                private final boolean negativeSpin;
                private SimpleVector2 position;
                private float rotation;
                private float alpha = 1f;
                private final float shrapnelSize;
                private AniShrapnel(SimpleVector2 origin, SimpleVector2 speed, float startingRotation, boolean negativeSpin, float shrapnelSize) {
                    this.shrapnelSize = shrapnelSize;
                    this.speed = speed;
                    this.negativeSpin = negativeSpin;
                    rotation = startingRotation;
                    position = new SimpleVector2(origin.x-shrapnelPic.getRegionWidth()/2f, origin.y-shrapnelPic.getRegionHeight()/2f);
                }
                private void update(float delta) {
                    alpha -= delta*4f;
                    position.x += speed.x * delta;
                    position.y += speed.y * delta;
                    rotation += (delta*500f) * (negativeSpin ? -1f : 1f);
                    if (alpha <= 0) {
                        exploded = true;
                    }
                }
                private void draw(Batch batch) {
                    batch.setColor(ColorPalette.changeAlpha(techType.getColor(), alpha));
                    batch.draw(shrapnelPic, position.x, position.y, shrapnelPic.getRegionWidth() / 2f, shrapnelPic.getRegionHeight() / 2f, shrapnelPic.getRegionWidth(), shrapnelPic.getRegionHeight(), shrapnelSize, shrapnelSize, rotation);
                    //batch.setColor(1, 1, 1, 1);
                }
            }
            private final AniAttack aniAttack;
            private final float scale;
            private final SimpleVector2 position;
            private float timer;
            private boolean active = false;
            private final AniRecoil aniRecoil;
            private ArrayList<AniShrapnel> shrapnels = null;
            private boolean exploded = false;
            private boolean recoilFinished = false;
            private AniShot(AniAttack aniAttack, SimpleVector2 start, float delay, float scale) {
                this.aniAttack = aniAttack;
                this.position = new SimpleVector2(start.x-shotPic.getRegionWidth()/2f, start.y-shotPic.getRegionHeight()/2f);
                this.scale = MathUtils.clamp(scale, 1f, 10f);
                timer = delay;
                aniRecoil = new AniRecoil(start, scale);
            }
            private void update(float delta) {
                if (!active) {
                    timer -= delta;
                    if (timer <= 0f) {
                        timer = 0f;
                        active = true;
                    }
                }
                if (active) {
                    if (!exploded) {
                        if (!recoilOnly) {
                            if (shrapnels == null) {
                                double distance = Math.sqrt(Math.pow(aniAttack.end.x - position.x, 2) + Math.pow(aniAttack.end.y - position.y, 2));
                                if (distance >= aniAttack.shotType.speed * delta) {
                                    position.x = position.x + ((aniAttack.shotType.speed * aniAttack.directionX) * delta);
                                    position.y = position.y + ((aniAttack.shotType.speed * aniAttack.directionY) * delta);
                                } else {
                                    shrapnels = new ArrayList<>();
                                    float rotationShift = 30f;
                                    float rotation = ThreadLocalRandom.current().nextInt(0, 360);
                                    for (int i = 0; i < 6; i++) {
                                        float shrapnelSpeed = 50f + ThreadLocalRandom.current().nextInt(0, 101);
                                        float velocityX = (float) (shrapnelSpeed * Math.cos(Math.toRadians(rotationShift + rotation)));
                                        float velocityY = (float) (shrapnelSpeed * Math.sin(Math.toRadians(rotationShift + rotation)));
                                        boolean spinAdjust = ThreadLocalRandom.current().nextInt(0, 2) == 0;
                                        shrapnels.add(new AniShrapnel(end, new SimpleVector2(velocityX, velocityY), ThreadLocalRandom.current().nextInt(0, 359), spinAdjust, scale*(ThreadLocalRandom.current().nextInt(5, 15)/10f)));
                                        rotationShift += 60f;
                                    }
                                }
                            } else {
                                for (AniShrapnel shrapnel : shrapnels) {
                                    shrapnel.update(delta);
                                }
                            }
                        }
                    }
                    if (!recoilFinished) {
                        aniRecoil.update(delta);
                    }
                }
            }
            private void drawAniShot(Batch batch) {
                batch.draw(shotPic, position.x, position.y, shotPic.getRegionWidth() / 2f, shotPic.getRegionHeight() / 2f, shotPic.getRegionWidth(), shotPic.getRegionHeight(), scale, scale, angle);
            }
            private void drawRecoil(Batch batch) {
                aniRecoil.draw(batch);
            }
            private void drawShrapnels(Batch batch) {
                if (shrapnels.size() > 0) {
                    for (AniShrapnel shrapnel : shrapnels) {
                        shrapnel.draw(batch);
                    }
                }
            }
        }
        private final ShotType shotType;
        private final TextureRegion shotPic;
        private final TextureRegion shrapnelPic = Farstar.ASSET_LIBRARY.getAtlasRegion("shrapnel");
        private final TextureRegion recoilPic = Farstar.ASSET_LIBRARY.getAtlasRegion("recoil");
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
        private final boolean recoilOnly;
        public AniAttack(ShotType shotType, Token att, Token def, int dmg, SimpleVector2 start, SimpleVector2 end, float directionX, float directionY, float angle, TechType techType, boolean recoilOnly) {
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
            this.recoilOnly = recoilOnly;
        }
        public AniShot createAniShot(float delay, float scale) {
            return new AniShot(this, start, delay, scale);
        }
        public void draw(Batch batch, float delta) {
            if (aniShots != null && aniShots.size() > 0) {
                ArrayList<AniAttack.AniShot> forDeletion = new ArrayList<>();
                batch.setColor(techType.getColor());
                for (AniAttack.AniShot aniShot : aniShots) {
                    if (aniShot.active) {
                        if (!recoilOnly) {
                            if (aniShot.shrapnels == null) {
                                aniShot.drawAniShot(batch);
                            } else if (!aniShot.exploded) {
                                aniShot.drawShrapnels(batch);
                            }
                        }
                        if ((aniShot.exploded || recoilOnly) && aniShot.recoilFinished) {
                            forDeletion.add(aniShot);
                        }
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
        SimpleVector2 start = new SimpleVector2(att.getX() + att.getTokenOffense().getPad().getRegionWidth() / 2f, att.getY() + att.getTokenOffense().getPad().getRegionHeight() / 2f);
        SimpleVector2 end = new SimpleVector2(def.getX() + def.getWidth() / 2f, def.getY() + def.getHeight() / 2f);
        float x = end.x - start.x;
        float y = end.y - start.y;
        double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double directionX = x / distance;
        double directionY = y / distance;
        float angle = (float) Math.toDegrees(Math.atan2(y, x)) + 90f;
        AniAttack aniAttack = new AniAttack(techType.getShotType(), att, def, dmg, start, end, (float) directionX, (float) directionY, angle, techType, att==def);
        ArrayList<AniAttack.AniShot> aniShots = new ArrayList<>();
        if (numberOfShots < 1) {
            numberOfShots = 1;
        }
        float zoomedDamage = dmg * 2f;
        float shotDamage = zoomedDamage / numberOfShots;
        if (shotDamage > 2f) {
            shotDamage = 2f + (shotDamage - 2f) / (techType.getShotType() == ShotType.BULLET ? 6f : 5f);
        }
        float scale = 0.5f + (shotDamage / 2f);
        for (int i = 0; i < numberOfShots; i++) {
            aniShots.add(aniAttack.createAniShot(i * 0.4f, scale));
        }
        aniAttack.aniShots = aniShots;
        aniAttacks.add(aniAttack);
    }

    public void drawAttacks(Batch batch, float delta, boolean bottom) {
        if (aniAttacks.size() > 0) {
            ArrayList<AniAttack> forDeletion = new ArrayList<>();
            for (AniAttack aniAttack : aniAttacks) {
                if (!aniAttack.done) {
                    aniAttack.draw(batch, delta);
                } else{
                    forDeletion.add(aniAttack);
                }
            }
            aniAttacks.removeAll(forDeletion);
        }
    }

    public void drawRecoil(Batch batch, Token token) {
        if (aniAttacks.size() > 0) {
            for (AniAttack aniAttack : aniAttacks) {
                if (aniAttack.att == token) {
                    if (!aniAttack.done) {
                        for (AniAttack.AniShot aniShot : aniAttack.aniShots) {
                            if (aniShot.active) {
                                if (!aniShot.recoilFinished) {
                                    aniShot.drawRecoil(batch);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void dispose() {
        aniAttacks.clear();
    }

}
