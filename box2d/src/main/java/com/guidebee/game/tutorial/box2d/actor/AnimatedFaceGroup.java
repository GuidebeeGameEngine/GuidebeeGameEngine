package com.guidebee.game.tutorial.box2d.actor;

import com.guidebee.game.GameEngine;
import com.guidebee.game.physics.BodyDef;
import com.guidebee.game.physics.PolygonShape;
import com.guidebee.game.physics.Shape;
import com.guidebee.game.scene.Group;
import com.guidebee.game.tutorial.box2d.Configuration;
import com.guidebee.math.MathUtils;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.TimeUtils;


public class AnimatedFaceGroup extends Group {
    private long lastDropTime =0;

    private void spawnFace(){
        int type=(MathUtils.random(3) + 1) % 3;

        AnimatedFace.Type faceType=AnimatedFace.Type.values()[type];
        AnimatedFace animatedFace=new AnimatedFace(faceType);
        animatedFace.setPosition(MathUtils.random(0, Configuration.SCREEN_WIDTH - 64),
                Configuration.SCREEN_HEIGHT);

        Rectangle dropRect=new Rectangle(0,0,32,32);
        switch(faceType){
            case Box:
                animatedFace.initBody();
                break;
            case Circle:
                animatedFace.initBody(BodyDef.BodyType.DynamicBody,
                        Shape.Type.Circle,dropRect,1.0f,0,0.1f);

                break;
            case Triangle:
                PolygonShape shape=new PolygonShape();

                float [] vertices=getTriangleVertices();
                shape.set(vertices);
                animatedFace.initBody(BodyDef.BodyType.DynamicBody,
                        shape,dropRect,1.0f,0f,0.1f);
                break;
            case Hexagon:
                animatedFace.initBody();
                break;
        }


        addActor(animatedFace);
        lastDropTime= TimeUtils.nanoTime();
    }


    private float [] getTriangleVertices(){
        float[] vertices=new float[6];
        vertices[0]=0;
        vertices[1]=0;
        vertices[2]=32;
        vertices[3]=0;
        vertices[4]=16;
        vertices[5]=32;

        for(int i=0;i<vertices.length;i++){
            vertices[i]= GameEngine.toBox2D(vertices[i]-16);
        }


        return vertices;
    }

    @Override
    public void act(float delta) {
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnFace();
        }

        AnimatedFace [] animatedFaces=getChildren().toArray(AnimatedFace.class);
        for(AnimatedFace rainDrop: animatedFaces){
            float y = rainDrop.getY();
            if(y+64 <0){
                removeActor(rainDrop);
            }

        }
    }

}
