package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.drawing.Color;
import com.guidebee.drawing.Pen;
import com.guidebee.drawing.SolidBrush;
import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.FontTexturePacker;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.graphics.TrueTypeFont;
import com.guidebee.game.scene.Actor;

/**
 * Created by James on 17/12/15.
 */
public class VectorFonts extends Actor{

    private TrueTypeFont vectorFont;
    private final FontTexturePacker fontTexturePacker;

    public VectorFonts(){
        super("VectorFonts");
        vectorFont=GameEngine.assetManager.get("font/phillysans.fon",TrueTypeFont.class);
        fontTexturePacker=new FontTexturePacker(800,480);
        Pen pen=new Pen(Color.GREEN);
        SolidBrush brush=new SolidBrush(Color.WHITE);
        int id=fontTexturePacker.drawChars(vectorFont, 48, "Guidebee IT Consulting Service".toCharArray(), pen, brush);
        fontTexturePacker.renderTexture();
        TextureRegion textureRegion = fontTexturePacker.getTextRegion(id);
        setTextureRegion(textureRegion);
        setPosition(0,400);




    }
}
