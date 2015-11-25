package com.guidebee.game.tutorial.box2d.stage;


import com.guidebee.game.tutorial.box2d.actor.AnimatedFaceGroup;
import com.guidebee.game.tutorial.box2d.actor.Face;
import com.guidebee.game.tutorial.box2d.actor.Ground;
import com.guidebee.game.tutorial.box2d.actor.TankWithRadar;

public class SensorStage extends Box2DGameStage{

    private final TankWithRadar tank;

    public SensorStage(){
        tank=new TankWithRadar();
        addActor(tank);
    }
}