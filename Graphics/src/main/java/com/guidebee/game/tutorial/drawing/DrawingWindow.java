package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.drawing.actor.CircleActor;


public class DrawingWindow extends Scene {

    private CircleActor circleActor;
    
    public DrawingWindow() {
        super(new ScalingViewport(800, 480));
        circleActor=new CircleActor();
        sceneStage.addActor(circleActor);

    }
}
