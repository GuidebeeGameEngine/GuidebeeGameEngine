package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.camera.viewports.ScreenViewport;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.drawing.actor.BezierActor;
import com.guidebee.game.tutorial.drawing.actor.CircleActor;
import com.guidebee.game.tutorial.drawing.actor.GuidebeeIT;
import com.guidebee.game.tutorial.drawing.actor.GuidebeeWebsite;


public class DrawingWindow extends Scene {

    private CircleActor circleActor;

    private BezierActor bezierActor;


    private GuidebeeIT guidebeeIT;

    private GuidebeeWebsite guidebeeWebsite;

    public DrawingWindow() {
        super(new ScalingViewport(FontCanvas.WIDTH,
                FontCanvas.HEIGHT));
        circleActor=new CircleActor();
        sceneStage.addActor(circleActor);
        bezierActor = new BezierActor();
        sceneStage.addActor(bezierActor);
        guidebeeIT =new GuidebeeIT();

        sceneStage.addActor(guidebeeIT);

        guidebeeWebsite =new GuidebeeWebsite();

        sceneStage.addActor(guidebeeWebsite);

    }
}
