package com.mapdigit.game.tutorial.basics;

import com.guidebee.game.ScreenAdapter;
import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.camera.viewports.ScreenViewport;
import com.guidebee.game.scene.Stage;

import static com.guidebee.game.GameEngine.graphics;

/**
 * Created by root on 10/17/15.
 */
public class HelloWordScreen extends ScreenAdapter {

    private Stage stage=new Stage(new ScreenViewport());


    private HelloWorldActor actor=new HelloWorldActor();

    public HelloWordScreen(){
        stage.addActor(actor);
    }



    @Override
    public void render(float delta){
        graphics.clearScreen(0, 0, 0.2f, 1);
        stage.act();
        stage.draw();
    }
}
