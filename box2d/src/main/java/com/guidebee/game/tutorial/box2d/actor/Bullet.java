package com.guidebee.game.tutorial.box2d.actor;

import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.box2d.Configuration;

import static com.guidebee.game.GameEngine.assetManager;


public class Bullet extends Actor {

    private final TextureRegion bulletTextRegion;

    public  Bullet(){
        super("Bullet");
        TextureAtlas textureAtlas=assetManager.get("box2d.atlas",
                TextureAtlas.class);
        bulletTextRegion =textureAtlas.findRegion("bullet");
        setTextureRegion(bulletTextRegion);

        setPosition((Configuration.SCREEN_WIDTH
                        - bulletTextRegion.getRegionWidth())/2,
                (Configuration.SCREEN_HEIGHT
                        - bulletTextRegion.getRegionHeight())/2);
        initBody();
        getBody().setBullet(true);

    }

}