package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.drawing.Beziers;
import static com.guidebee.game.GameEngine.app;

public class BezierActor extends Actor {

    private final Beziers beziers;

    public BezierActor(){
        super("BezierActor");
        beziers =new Beziers();
        setTexture(beziers.getTexture());
        setPosition(400,100);
    }






}
