package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.tutorial.drawing.actor.CircleActor;
import com.guidebee.game.tutorial.drawing.actor.FontList;
import com.guidebee.game.tutorial.drawing.actor.GuidebeeIT;
import com.guidebee.game.tutorial.drawing.actor.GuidebeeWebsite;


public class DrawingWindow extends Scene {

    private CircleActor circleActor;



    private FontList fontList;

    private GuidebeeIT guidebeeIT;

    private GuidebeeWebsite guidebeeWebsite;

    public DrawingWindow() {
        super(new ScalingViewport(FontCanvas.WIDTH,
                FontCanvas.HEIGHT));
        circleActor=new CircleActor();
        sceneStage.addActor(circleActor);

        guidebeeIT =new GuidebeeIT();

        sceneStage.addActor(guidebeeIT);

        guidebeeWebsite =new GuidebeeWebsite();

        sceneStage.addActor(guidebeeWebsite);

        fontList=new FontList();
        sceneStage.addActor(fontList);

    }
}
