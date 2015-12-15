package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.drawing.Colors;


public class CircleActor extends Actor {

    private final Colors colors;

    public CircleActor(){
        super("CircleActor");
        colors=new Colors();
        setTexture(colors.getTexture());
    }


    @Override
    public void draw (Batch batch, float parentAlpha){
        batch.draw(colors.getTexture(),getX(),getY());
    }



}
