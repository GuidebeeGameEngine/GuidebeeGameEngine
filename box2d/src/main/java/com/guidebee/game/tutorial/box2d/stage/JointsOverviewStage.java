package com.guidebee.game.tutorial.box2d.stage;


import com.guidebee.game.GameEngine;
import com.guidebee.game.physics.joints.MotorJoint;
import com.guidebee.game.physics.joints.MotorJointDef;
import com.guidebee.game.tutorial.box2d.actor.BoxGround;
import com.guidebee.game.tutorial.box2d.actor.Face;
import com.guidebee.game.tutorial.box2d.actor.Ground;

public class JointsOverviewStage extends Box2DGameStage {



    private final BoxGround ground;
    private final Face face;
    private final MotorJoint motorJoint;

    public JointsOverviewStage(){
        ground=new BoxGround();
        addActor(ground);

        face=new Face();
        addActor(face);

        MotorJointDef motorJointDef=new MotorJointDef();
        motorJointDef.maxForce=150.0f;
        motorJointDef.maxTorque=150.f;


        motorJointDef.initialize(ground.getBody(),face.getBody());


        motorJoint=(MotorJoint)world.createJoint(motorJointDef);



    }
}
