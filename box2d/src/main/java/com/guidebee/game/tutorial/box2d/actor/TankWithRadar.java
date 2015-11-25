package com.guidebee.game.tutorial.box2d.actor;


import com.guidebee.game.physics.BodyDef;
import com.guidebee.game.physics.CircleShape;
import com.guidebee.math.geometry.Rectangle;

public class TankWithRadar extends  Tank{

    public TankWithRadar(){
        setX(150);
        CircleShape radarArea=new CircleShape();
        radarArea.setRadius(200);
        Rectangle dropRect=new Rectangle(0,0,
                tankTextRegion.getRegionWidth(),
                tankTextRegion.getRegionHeight());



    }
}
