package com.mapdigit.game.tutorial.microedition;

import com.guidebee.game.GameEngine;
import com.guidebee.game.ScreenAdapter;
import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.microedition.LayerManager;
import com.mapdigit.game.tutorial.microedition.actor.Background;
import com.mapdigit.game.tutorial.microedition.actor.Bucket;
import com.mapdigit.game.tutorial.microedition.actor.Fly;
import com.mapdigit.game.tutorial.microedition.actor.RainDrop;


public class DropScene extends ScreenAdapter {

    private Bucket bucket = new Bucket();
    private RainDrop rainDrop=new RainDrop();
    private Fly fly=new Fly();
    private Background background=new Background();


    private LayerManager layerManager=new LayerManager(new ScalingViewport(800,480));

    public DropScene(){
        layerManager.append(background);
        layerManager.append(bucket);
        layerManager.append(rainDrop);
        layerManager.append(fly);
    }

    @Override
    public void render(float delta){
        GameEngine.graphics.clearScreen(0,0,0.2f,1);
        layerManager.act();
        layerManager.draw();
    }

    @Override
    public void dispose(){
        layerManager.dispose();
    }

}