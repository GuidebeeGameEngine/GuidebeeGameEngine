package com.mapdigit.game.tutorial.coords;

import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.camera.viewports.ScreenViewport;
import com.guidebee.game.scene.Scene;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;


public class CoordinateScene extends Scene{

    CoordinateActor actor=new CoordinateActor();

    public CoordinateScene() {
        super(new ScreenViewport());
        sceneStage.addActor(actor);
    }

    @Override
    public void render(float delta) {
        graphics.clearScreen(1f, 1f, 1f, 1);
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }


}
