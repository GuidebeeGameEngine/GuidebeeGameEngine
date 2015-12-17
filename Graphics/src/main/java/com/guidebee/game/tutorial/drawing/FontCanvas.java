package com.guidebee.game.tutorial.drawing;

import com.guidebee.drawing.Color;
import com.guidebee.drawing.Pen;
import com.guidebee.drawing.SolidBrush;
import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.FontTexturePacker;
import com.guidebee.game.graphics.TrueTypeFont;

/**
 * Created by James on 17/12/15.
 */
public class FontCanvas {

    public final static int WIDTH=800;
    public final static int HEIGHT=480;

    public static FontTexturePacker fontTexturePacker;

    static{
        TrueTypeFont vectorFont= GameEngine.assetManager.get("font/phillysans.fon",
                TrueTypeFont.class);
        fontTexturePacker=new FontTexturePacker(WIDTH,HEIGHT);
        Pen pen=new Pen(Color.GREEN);
        SolidBrush brush=new SolidBrush(Color.WHITE);
        FontCanvas.fontTexturePacker.drawChars(vectorFont, 48,
                "Guidebee IT Consulting Service".toCharArray(), pen, brush);

         pen=new Pen(Color.RED);
        brush=new SolidBrush(Color.GREEN);
        FontCanvas.fontTexturePacker.drawChars(vectorFont, 48,
                "http www guidebee com au".toCharArray(), pen, brush);
        FontCanvas.fontTexturePacker.renderTexture();
    }
}
