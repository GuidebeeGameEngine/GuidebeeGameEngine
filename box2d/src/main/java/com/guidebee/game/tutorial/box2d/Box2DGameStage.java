package com.guidebee.game.tutorial.box2d;


import com.guidebee.game.GameEngine;
import com.guidebee.game.camera.viewports.StretchViewport;
import com.guidebee.game.physics.Body;
import com.guidebee.game.physics.BodyDef;
import com.guidebee.game.physics.Box2DDebugRenderer;
import com.guidebee.game.physics.Fixture;
import com.guidebee.game.physics.QueryCallback;
import com.guidebee.game.physics.joints.MouseJoint;
import com.guidebee.game.physics.joints.MouseJointDef;
import com.guidebee.game.scene.Stage;
import com.guidebee.game.tutorial.box2d.actor.AnimatedFaceGroup;
import com.guidebee.game.tutorial.box2d.actor.Face;
import com.guidebee.game.tutorial.box2d.actor.Ground;
import com.guidebee.math.Matrix4;
import com.guidebee.math.Vector2;
import com.guidebee.math.Vector3;

import static com.guidebee.game.GameEngine.*;

public class Box2DGameStage extends Stage implements QueryCallback{

    private final Ground ground;
    private final Face face;
    private final AnimatedFaceGroup animatedFaceGroup;

    private final Matrix4 debugMatrix;
    private final Box2DDebugRenderer debugRenderer;

    private final Vector2 touchPoint;
    private Fixture touchFixture =null;



    public Box2DGameStage(){

        super(new StretchViewport(Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT));

        initWorld();
        BodyDef staticBodyDef=new BodyDef();
        staticBodyDef.position.set(toBox2D(400),toBox2D(400));


        touchPoint =new Vector2();

        ground=new Ground();
        addActor(ground);
        face=new Face();
        addActor(face);
        animatedFaceGroup=new AnimatedFaceGroup();
        addActor(animatedFaceGroup);

        debugMatrix=new Matrix4(getCamera().combined);
        debugMatrix.scale(GameEngine.pixelToBox2DUnit, GameEngine.pixelToBox2DUnit, 0);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(input.isTouched()){

            Vector3 touchPos=new Vector3();
            touchPos.set(input.getX(), input.getY(), 0);
            getCamera().unproject(touchPos);
            touchPoint.set(toBox2D(touchPos.x),
                    toBox2D(touchPos.y));


            getWorld().queryAABB(this, touchPoint.x-0.001f,touchPoint.y-0.001f,
                    touchPoint.x+0.001f,touchPoint.y+0.001f);
            if(touchFixture!=null){
                Body body = touchFixture.getBody();
                body.applyForce(0,500*body.getMass(),touchPoint.x,touchPoint.y,true);

            }


        }
    }

    @Override
    public void draw (){
        super.draw();
        debugRenderer.render(GameEngine.world, debugMatrix);
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        Body body = fixture.getBody();
        if(body.getType()== BodyDef.BodyType.DynamicBody){
            Boolean inside = fixture.testPoint(touchPoint);
            if(inside){
                this.touchFixture =fixture;
                return false;
            }
        }
        return true;
    }
}
