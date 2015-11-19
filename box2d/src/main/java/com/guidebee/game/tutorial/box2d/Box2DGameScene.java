package com.guidebee.game.tutorial.box2d;

import com.guidebee.game.scene.Scene;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;

public class Box2DGameScene extends Scene {

    public Box2DGameScene(){
        super(new Box2DGameStage());
    }

    @Override
    public void render(float delta) {
        graphics.clearScreen(0, 0, 0.2f, 1);
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

}
