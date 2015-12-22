package com.guidebee.game.tutorial.actions.actor;

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.actions.Configuration;

import static com.guidebee.game.GameEngine.assetManager;


public class Background extends Actor {

    private TextureRegion  textureRegion;

    public Background(){
        super("Platform");
        TextureAtlas textureAtlas=assetManager.get("actions.atlas",
                TextureAtlas.class);
        textureRegion=textureAtlas.findRegion("background");
        setSize(Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion,0,0,
                Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT);
    }
}
