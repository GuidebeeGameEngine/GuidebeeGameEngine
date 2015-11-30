package com.guidebee.game.tutorial.actions.actor;

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.physics.BodyDef;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.actions.Configuration;

import static com.guidebee.game.GameEngine.assetManager;

public class Ground extends Actor {

    private final TextureRegion faceTextRegion;

    public Ground(){
        super("Ground");
        TextureAtlas textureAtlas=assetManager.get("actions.atlas",
                TextureAtlas.class);
        faceTextRegion=textureAtlas.findRegion("objw1_plataforma");
        setTextureRegion(faceTextRegion);
        setPosition((Configuration.SCREEN_WIDTH
                -faceTextRegion.getRegionWidth())/2,0);

    }



}