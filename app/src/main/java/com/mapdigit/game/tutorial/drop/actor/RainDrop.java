package com.mapdigit.game.tutorial.drop.actor;

import com.guidebee.game.graphics.Texture;
import com.guidebee.game.scene.Actor;

import static com.guidebee.game.GameEngine.assetManager;

public class RainDrop extends Actor  {



    public RainDrop(){
        super("RainDrop");
        setTexture(assetManager.get("coin.png",Texture.class));

    }



}
