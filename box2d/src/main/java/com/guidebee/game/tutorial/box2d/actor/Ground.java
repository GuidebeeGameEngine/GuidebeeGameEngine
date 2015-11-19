package com.guidebee.game.tutorial.box2d.actor;


import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.box2d.Configuration;

import static com.guidebee.game.GameEngine.*;

public class Ground extends Actor{

    private final TextureRegion groundTextRegion;

    public Ground(){
        super("Ground");
        TextureAtlas textureAtlas = assetManager.get("box2d.atlas",
                TextureAtlas.class);
        groundTextRegion = textureAtlas.findRegion("ground");
        setSize(Configuration.SCREEN_WIDTH,
                groundTextRegion.getRegionHeight());
        setPosition(0, 0);
        initEdgeBody(0,groundTextRegion.getRegionHeight(),
                Configuration.SCREEN_WIDTH,
                groundTextRegion.getRegionHeight(),1f,0.5f,0.3f);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*int backWidth = groundTextRegion.getRegionWidth();
        int size = Configuration.SCREEN_WIDTH / backWidth;
        if (size * backWidth < Configuration.SCREEN_WIDTH) size++;
        for (int i = -1; i < size; i++) {
            batch.draw(groundTextRegion,  i * backWidth, 0);
        }*/


    }
}
