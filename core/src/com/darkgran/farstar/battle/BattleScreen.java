package com.darkgran.farstar.battle;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.ui.TableMenu;

public class BattleScreen extends SuperScreen {
    private final Box2DDebugRenderer debugRenderer;
    private final WorldManager worldManager;


    public BattleScreen(final Farstar game, TableMenu tableMenu)
    {
        super(game);
        setTableMenu(tableMenu);
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        worldManager = new WorldManager();
        addTestBody();
    }

    private void addTestBody() { //temp
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;

        Body dynamicBody = worldManager.getWorld().createBody(myBodyDef);
        dynamicBody.setTransform((float) (Farstar.STAGE_WIDTH/2-10), (float) (Farstar.STAGE_HEIGHT/2-10), 0f);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(20f,20f);

        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxShape;
        boxFixtureDef.density = 1;
        dynamicBody.createFixture(boxFixtureDef);
    }

    @Override
    public void drawScreen(float delta) {
        drawBox2DDebug();
        worldManager.worldTimer(delta); //world stepping

    }

    private void drawBox2DDebug() {
        Matrix4 debugMatrix = new Matrix4(getCamera().combined);
        debugMatrix.scale(1f, 1f, 1f);
        debugRenderer.setDrawBodies(true);
        debugRenderer.render(worldManager.getWorld(), debugMatrix);
    }

    @Override
    public void dispose() {
        worldManager.disposeWorld();
    }
}
