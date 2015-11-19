package com.guidebee.game.tutorial.box2d.actor;

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.box2d.Configuration;

import static com.guidebee.game.GameEngine.*;

public class Face extends Actor {

    private final TextureRegion faceTextRegion;

    public  Face(){
        super("Face");
        TextureAtlas textureAtlas=assetManager.get("box2d.atlas",TextureAtlas.class);
        faceTextRegion=textureAtlas.findRegion("face_box");
        setTextureRegion(faceTextRegion);

        setPosition((Configuration.SCREEN_WIDTH-faceTextRegion.getRegionWidth())/2,
                (Configuration.SCREEN_HEIGHT-faceTextRegion.getRegionHeight())/2);
        initBody();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

    }
}
