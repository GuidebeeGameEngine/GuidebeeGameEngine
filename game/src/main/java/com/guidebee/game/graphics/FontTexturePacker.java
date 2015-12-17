package com.guidebee.game.graphics;

import com.guidebee.drawing.Brush;
import com.guidebee.drawing.Graphics2D;
import com.guidebee.drawing.Pen;
import com.guidebee.drawing.VectorFont;
import com.guidebee.math.BinSortPacker;
import com.guidebee.math.Vector2;
import com.guidebee.math.geometry.Rectangle;

import java.util.ArrayList;

/**
 * Dynamic Texture packer for vector font.
 */
public class FontTexturePacker {
    private final BinSortPacker binSortPacker;

    private Graphics2D graphics2D;

    private final ArrayList<TextInfo> textInfos;
    private final ArrayList<TextureRegion> textureRegions;

    private Texture texture;


    public FontTexturePacker(int width, int height){
        binSortPacker=new BinSortPacker(width,height);
        graphics2D=new Graphics2D(width,height);
        graphics2D.clear();
        textInfos=new ArrayList<TextInfo>();
        textureRegions=new ArrayList<TextureRegion>();

    }


    public int drawChars (VectorFont font, int fontSize, char[] data,Pen pen, Brush brush){
        int height=fontSize;
        int width=font.charsWidth(data,0,data.length,fontSize);
        TextInfo textInfo=new TextInfo();
        textInfo.brush=brush;
        textInfo.pen=pen;
        textInfo.font=font;
        textInfo.fontSize=fontSize;
        textInfo.data=data;
        textInfo.width=width;
        textInfo.height=height;
        Rectangle rect=binSortPacker.getDemensions();
        textInfos.add(textInfo);
        if(rect.width<width || rect.height<height){
            binSortPacker.reset(width,height);
            recalculateRectangle();
        }
        Vector2 pos=binSortPacker.addRectangle(width, height);


        if(pos!=null) {
            if (binSortPacker.isResized()) {

                recalculateRectangle();
            } else {
                textInfo.location = pos;
                graphics2D.setPenAndBrush(textInfo.pen, textInfo.brush);
                graphics2D.drawChars(textInfo.font,
                        textInfo.fontSize,
                        textInfo.data,
                        (int) pos.x,
                        (int) pos.y);

            }
            return textInfos.size() - 1;
        }
        return -1;
    }

    public TextureRegion getTextRegion(int id){
        if(id<textInfos.size()){
            return textureRegions.get(id);
        }
       return null;
    }

    public void renderTexture(){
        if(texture!=null){
            texture.dispose();
        }
        texture=new Texture(graphics2D);
        textureRegions.clear();
        for (int i=0;i<textInfos.size();i++) {
            TextInfo textInfo = textInfos.get(i);
            TextureRegion textureRegion=new TextureRegion(texture,(int)textInfo.location.x,
                    (int)textInfo.location.y,textInfo.width,textInfo.height);
            textureRegions.add(textureRegion);

        }


    }




    private void recalculateRectangle(){
        binSortPacker.reset();
        Rectangle rectangle=binSortPacker.getDemensions();
        graphics2D=new Graphics2D((int)rectangle.width,
                (int)rectangle.height);
        graphics2D.clear();

        for (int i=0;i<textInfos.size();i++) {
            TextInfo textInfo=textInfos.get(i);
            Vector2 pos = binSortPacker.addRectangle(textInfo.width, textInfo.height);
            graphics2D.setPenAndBrush(textInfo.pen,textInfo.brush);
            graphics2D.drawChars(textInfo.font,
                    textInfo.fontSize,
                    textInfo.data,
                    (int)pos.x,
                    (int)pos.y);
            textInfo.location=pos;
        }
    }




}


class TextInfo{
    public VectorFont font;
    public Pen pen;
    public Brush brush;
    public int fontSize;
    public char[]data;
    public Vector2 location;
    public int width;
    public int height;
}
