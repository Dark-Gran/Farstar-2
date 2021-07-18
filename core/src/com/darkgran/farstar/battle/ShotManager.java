package com.darkgran.farstar.battle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

/** Responsible for all shot animations. */
public class ShotManager {
    private enum ShotType {
        BULLET("shot_kinetic", 3000f);
        private final String shotPicName;
        private final float speed;
        ShotType(String shotPicName, float speed) {
            this.shotPicName = shotPicName;
            this.speed = speed;
        }
    }
    private class AniAttack {
        private final ShotType shotType;
        private final Texture shotPic;
        private final Token att;
        private final Token def;
        private final int dmg;
        private final SimpleVector2 start;
        private final SimpleVector2 end;
        private SimpleVector2 position;
        public AniAttack(ShotType shotType, Token att, Token def, int dmg, SimpleVector2 start, SimpleVector2 end) {
            this.shotType = shotType;
            this.att = att;
            this.def = def;
            this.dmg = dmg;
            this.start = start;
            this.end = end;
            shotPic = Farstar.ASSET_LIBRARY.get("images/"+shotType.shotPicName+".png");
            position = start;
        }
    }
    private final ArrayList<AniAttack> aniAttacks = new ArrayList<>();


    public void newAttack(Token att, Token def, int dmg) {
        aniAttacks.add(new AniAttack(
                ShotType.BULLET,
                att, def, dmg,
                new SimpleVector2(att.getX(), att.getY()),
                new SimpleVector2(def.getX(), def.getY())
        ));
    }

    public void drawAttacks(Batch batch, float delta) {
        for (AniAttack aniAttack : aniAttacks) {
            drawAttack(batch, delta, aniAttack);
        }
    }

    private void drawAttack(Batch batch, float delta, AniAttack aniAttack) { //todo
        //aniAttack.position.setX(aniAttack.position.getX()+aniAttack.shotType.speed*delta);
        aniAttack.position.setY(aniAttack.position.getY()+aniAttack.shotType.speed*delta);
        batch.draw(aniAttack.shotPic, aniAttack.position.getX(), aniAttack.position.getY());
    }

}
