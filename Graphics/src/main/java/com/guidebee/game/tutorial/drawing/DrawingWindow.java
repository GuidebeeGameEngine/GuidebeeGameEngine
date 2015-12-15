package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.camera.viewports.ScreenViewport;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.drawing.actor.BezierActor;
import com.guidebee.game.tutorial.drawing.actor.CircleActor;


public class DrawingWindow extends Scene {

    private CircleActor circleActor;

    private BezierActor bezierActor;
    
    public DrawingWindow() {
        super(new ScreenViewport());
        circleActor=new CircleActor();
        sceneStage.addActor(circleActor);
        bezierActor = new BezierActor();
        sceneStage.addActor(bezierActor);

    }
}
