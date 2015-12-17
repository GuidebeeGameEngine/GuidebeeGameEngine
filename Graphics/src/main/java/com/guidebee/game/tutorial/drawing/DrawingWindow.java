package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.camera.viewports.ScreenViewport;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.drawing.actor.BezierActor;
import com.guidebee.game.tutorial.drawing.actor.CircleActor;
import com.guidebee.game.tutorial.drawing.actor.VectorFonts;


public class DrawingWindow extends Scene {

    private CircleActor circleActor;

    private BezierActor bezierActor;


    private VectorFonts vectorFonts;
    
    public DrawingWindow() {
        super(new ScreenViewport());
        circleActor=new CircleActor();
        sceneStage.addActor(circleActor);
        bezierActor = new BezierActor();
        sceneStage.addActor(bezierActor);
        vectorFonts=new VectorFonts();

        sceneStage.addActor(vectorFonts);

    }
}
