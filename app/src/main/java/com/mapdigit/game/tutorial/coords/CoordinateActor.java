package com.mapdigit.game.tutorial.coords;

import com.guidebee.game.graphics.Texture;
import com.guidebee.game.scene.Actor;
import static com.guidebee.game.GameEngine.*;

public class CoordinateActor extends Actor {

    public CoordinateActor(){
        super("Coordinate");
        setTexture(assetManager.get("coords.png",Texture.class));
        setPosition(0,0);

    }
}
