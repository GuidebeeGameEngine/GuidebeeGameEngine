package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.drawing.Colors;
import com.guidebee.game.tutorial.drawing.FontCanvas;


public class CircleActor extends Actor {

    private final Colors colors;

    public CircleActor(){
        super("CircleActor");
        colors=new Colors();
        setTexture(colors.getTexture());
        setPosition(FontCanvas.WIDTH-200,0);
    }


}
