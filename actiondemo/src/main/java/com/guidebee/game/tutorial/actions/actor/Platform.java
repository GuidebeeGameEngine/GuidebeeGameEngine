package com.guidebee.game.tutorial.actions.actor;

import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.actions.Configuration;

import static com.guidebee.game.GameEngine.assetManager;

/**
 * Created by James on 30/11/15.
 */
public class Platform extends Actor {

    private final TextureRegion faceTextRegion;

    public Platform(int x,int y){
        super("Platform");
        TextureAtlas textureAtlas=assetManager.get("actions.atlas",
                TextureAtlas.class);
        faceTextRegion=textureAtlas.findRegion("obj_obstacle");
        setTextureRegion(faceTextRegion);
        setPosition(x,y);

    }



}
