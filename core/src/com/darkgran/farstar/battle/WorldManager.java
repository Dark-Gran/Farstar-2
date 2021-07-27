package com.darkgran.farstar.battle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;

import java.util.ArrayList;

/**
 *  This is only a draft setup with a test-body.
 *  Meant for future "graphical effects".
 *  UNUSED AT THE MOMENT.
 */

public class WorldManager {
    public static float FPS = 60.0f;
    public static float STEP_TIME = 1f / FPS;
    public static int VELOCITY_ITERATIONS = 15;
    public static int POSITION_ITERATIONS = 12;
    /*public static float WORLD_WIDTH = 9.6f;
    public static float WORLD_HEIGHT = 4.8f;
    public static float CAMERA_CLOSEUP_X = 0.2f;
    public static float CAMERA_CLOSEUP_Y = 0.1f;*/
    private float accumulator = 0;
    private final World world = new World(new Vector2(0, 0), true);
    private final ArrayList<Body> corpses = new ArrayList<>();
    //private final CollisionListener collisionListener = new CollisionListener();

    public WorldManager() {
        //world.setContactListener(collisionListener);
        //addTestBody();
    }

    @Deprecated
    private void addTestBody() { //temp
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;

        Body dynamicBody = world.createBody(myBodyDef);
        dynamicBody.setTransform((float) (Farstar.STAGE_WIDTH/2-10), (float) (Farstar.STAGE_HEIGHT/2-10), 0f);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(20f,20f);

        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxShape;
        boxFixtureDef.density = 1;
        dynamicBody.createFixture(boxFixtureDef);
    }

    public World getWorld() { return world; }

    private void reap(World world) { //destroys all marked bodies
        for (int i = 0; i < corpses.size(); i++) {
            if (corpses.get(i) != null) {
                Body corpse = corpses.get(i);
                world.destroyBody(corpse);
                corpses.remove(corpse);
            }
        }
    }

    //Stepping
    public void worldTimer(float delta) {
        accumulator += delta;
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            worldTick(world);
        }
    }

    private void worldTick(World world) {
        reap(world);
        world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    //Utilities
    public void disposeWorld() {
        world.dispose();
    }
}
