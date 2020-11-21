package com.darkgran.farstar.battle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class WorldManager implements WorldSettings {
    private float accumulator = 0;
    private World world;
    private ArrayList corpses = new ArrayList();
    //private CollisionListener collisionListener;

    public WorldManager() {
        world = new World(new Vector2(0, 0), true);
        //collisionListener = new CollisionListener();
        //world.setContactListener(collisionListener);
    }

    public World getWorld() { return world; }

    private void reap(World world) { //destroys all marked bodies
        for (int i = 0; i < corpses.size(); i++) {
            if (corpses.get(i) != null) {
                Object corpse = corpses.get(i);
                //world.destroyBody(item.body);
                corpses.remove(corpse);
            }
        }
    }

    //Stepping
    public void worldTimer(float delta) {
        accumulator += Math.min(delta, 0.25f);
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
