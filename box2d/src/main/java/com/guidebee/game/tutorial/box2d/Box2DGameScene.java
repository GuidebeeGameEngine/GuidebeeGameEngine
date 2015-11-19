package com.guidebee.game.tutorial.box2d;

import com.guidebee.game.GameEngine;
import com.guidebee.game.physics.Box2DDebugRenderer;
import com.guidebee.game.scene.Scene;
import com.guidebee.math.Matrix4;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;

public class Box2DGameScene extends Scene {


    private final Matrix4 debugMatrix;
    private final Box2DDebugRenderer debugRenderer;


    public Box2DGameScene(){

        sceneStage.initWorld();

        debugMatrix=new Matrix4(sceneStage.getCamera().combined);
        debugMatrix.scale(GameEngine.pixelToBox2DUnit, GameEngine.pixelToBox2DUnit, 0);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {
        graphics.clearScreen(0, 0, 0.2f, 1);
        super.render(delta);

        debugRenderer.render(GameEngine.world, debugMatrix);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

}
