package com.guidebee.game.tutorial.actions;


import com.guidebee.game.camera.viewports.StretchViewport;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.actions.actor.Background;
import com.guidebee.game.tutorial.actions.actor.Bird;
import com.guidebee.game.tutorial.actions.actor.Ground;
import com.guidebee.game.tutorial.actions.actor.Platform;

import static com.guidebee.game.GameEngine.graphics;

public class ActionScene extends Scene {

    private final Ground platform;
    private final Background background;
    private final Bird bird1;
    private final Bird bird2;
    private final Bird bird3;

    private final Platform platform1;
    private final Platform platform2;


    public ActionScene() {
        super(new StretchViewport(Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT));


        background=new Background();
        sceneStage.addActor(background);
        platform=new Ground();
        sceneStage.addActor(platform);

        bird1 =new Bird(100,100);
        sceneStage.addActor(bird1);
        bird1.addAction(DemoActions.action1);

        bird2 =new Bird(200,200);
        sceneStage.addActor(bird2);
        bird2.addAction(DemoActions.action2);

        bird3 =new Bird(300,300);
        sceneStage.addActor(bird3);
        bird3.addAction(DemoActions.action3);

        platform1=new Platform(200,400);
        sceneStage.addActor(platform1);
        platform1.addAction(DemoActions.action4);

        platform2=new Platform(600,100);
        sceneStage.addActor(platform2);
        platform2.addAction(DemoActions.action5);

    }

    @Override
    public void render(float delta) {

        graphics.clearScreen(0, 0, 0.2f, 1);
        super.render(delta);


    }

}
