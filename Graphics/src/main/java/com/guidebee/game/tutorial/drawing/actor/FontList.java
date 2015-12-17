package com.guidebee.game.tutorial.drawing.actor;

import com.guidebee.drawing.Color;
import com.guidebee.drawing.Pen;
import com.guidebee.drawing.SolidBrush;
import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.graphics.TrueTypeFont;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.drawing.FontCanvas;

import java.util.ArrayList;

/**
 * Created by James on 17/12/15.
 */
public class FontList extends Actor{

    public static String [] fontNames=new String[]{
            "arial",
            "courier",
            "elephant",
            "georgia",
            "phillysans",
            "rockwell",
            "roman",
            "serif",
            "verdana",

    };

    private ArrayList<TextureRegion> textureRegions;


    public FontList(){
        super("FontList");

        textureRegions=new ArrayList<>();
        for(int i=0;i<fontNames.length;i++){
            textureRegions.add(FontCanvas.fontTexturePacker.getTextRegion(i+2));
        }

        setPosition(0,600);
    }

    @Override
    public void draw(Batch batch,float delta){

        for(int i=0;i<textureRegions.size();i++){
            batch.draw(textureRegions.get(i),getX()+20,getY()-i*70);
        }
    }
}
