package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.graphics.TrueTypeFont;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.drawing.FontCanvas;

/**
 * Created by James on 17/12/15.
 */
public class GuidebeeWebsite extends Actor{

    private TrueTypeFont vectorFont;


    public GuidebeeWebsite(){
        super("GuidebeeWebsite");

        TextureRegion textureRegion
                = FontCanvas.fontTexturePacker.getTextRegion(1);
        setTextureRegion(textureRegion);
        setPosition(30,350);




    }
}
