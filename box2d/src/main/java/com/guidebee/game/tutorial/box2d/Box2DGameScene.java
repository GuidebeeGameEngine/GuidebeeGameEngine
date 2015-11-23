package com.guidebee.game.tutorial.box2d;

import com.guidebee.game.GameEngine;
import com.guidebee.game.InputProcessor;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.box2d.stage.BasicBox2DStage;
import com.guidebee.game.tutorial.box2d.stage.BodyTypeStage;
import com.guidebee.game.tutorial.box2d.stage.SelfControlStage;
import com.guidebee.game.tutorial.box2d.stage.ShapeTypeStage;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;

public class Box2DGameScene extends Scene {

    private InputProcessor savedInputProcessor;

    public Box2DGameScene(){
        super(new ShapeTypeStage());

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

    @Override
    public void hide() {
        GameEngine.input.setInputProcessor(savedInputProcessor);
    }

    @Override
    public void show() {
        savedInputProcessor = GameEngine.input.getInputProcessor();
        GameEngine.input.setInputProcessor(sceneStage);


    }

}
