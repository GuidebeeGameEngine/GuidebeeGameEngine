package com.guidebee.game.tutorial.drawing;

import com.guidebee.drawing.Color;
import com.guidebee.drawing.Pen;
import com.guidebee.drawing.SolidBrush;
import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.FontTexturePacker;
import com.guidebee.game.graphics.TrueTypeFont;
import com.guidebee.game.tutorial.drawing.actor.FontList;

/**
 * Created by James on 17/12/15.
 */
public class FontCanvas {

    public final static int WIDTH=480;
    public final static int HEIGHT=800;

    public static FontTexturePacker fontTexturePacker;



    static{
        TrueTypeFont vectorFont= GameEngine.assetManager.get("font/phillysans.fon",
                TrueTypeFont.class);
        fontTexturePacker=new FontTexturePacker(WIDTH,HEIGHT);
        Pen pen=new Pen(Color.GREEN);
        SolidBrush brush=new SolidBrush(Color.WHITE);
        FontCanvas.fontTexturePacker.drawChars(vectorFont, 36,
                "Guidebee IT Consulting Service".toCharArray(), pen, brush);

        pen=new Pen(Color.RED);
        brush=new SolidBrush(Color.GREEN);
        FontCanvas.fontTexturePacker.drawChars(vectorFont, 36,
                "http www guidebee com au".toCharArray(), pen, brush);

        pen=new Pen(Color.GREEN);
        brush=new SolidBrush(Color.WHITE);

        for(int i=0;i< FontList.fontNames.length;i++) {
            FontCanvas.fontTexturePacker.drawChars(
                    GameEngine.assetManager.get("font/"+FontList.fontNames[i]+".fon",
                            TrueTypeFont.class),
                    64,FontList.fontNames[i].toCharArray(),pen,brush);


        }
        FontCanvas.fontTexturePacker.renderTexture();
    }
}
