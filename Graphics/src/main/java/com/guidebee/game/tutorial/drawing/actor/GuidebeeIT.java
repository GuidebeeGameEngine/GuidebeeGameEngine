package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.drawing.FontCanvas;

/**
 * Created by James on 17/12/15.
 */
public class GuidebeeIT extends Actor{

    public GuidebeeIT(){
        super("GuidebeeIT");

        TextureRegion textureRegion
                = FontCanvas.fontTexturePacker.getTextRegion(0);
        setTextureRegion(textureRegion);
        setPosition(0,750);




    }
}
