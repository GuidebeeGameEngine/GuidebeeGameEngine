package com.guidebee.game.graphics;

import android.util.Log;

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
    private  static int BUFFER_SIZE=4; //buffer area size


    /**
     * Constructor
     * @param width initial canvas size
     * @param height initial canvas height
     */
    public FontTexturePacker(int width, int height){
        this(width,height,4);
    }
    /**
     * Constructor
     * @param width initial canvas size
     * @param height initial canvas height
     */
    public FontTexturePacker(int width, int height,int bufferSize){
        int newWidth=(width/64+1)*64;
        int newHeight=(height/64+1)*64;
        binSortPacker=new BinSortPacker(newWidth,newHeight);
        textInfos=new ArrayList<TextInfo>();
        textureRegions=new ArrayList<TextureRegion>();
        BUFFER_SIZE=Math.max(bufferSize,4);

    }


    /**
     * draw text in give vector font.
     * @param font
     * @param fontSize
     * @param data
     * @param pen
     * @param brush
     * @return
     */
    public int drawChars (VectorFont font, int fontSize, char[] data,Pen pen, Brush brush){
        int height=fontSize+BUFFER_SIZE*2;
        int width=font.charsWidth(data,0,data.length,fontSize)+BUFFER_SIZE*2;
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

            float maxWidth=Math.max(rect.width, width);
            float maxHeight=Math.max(rect.height, height);
            int newWidth=(int)(maxWidth/64+1)*64;
            int newHeight=(int)(maxHeight/64+1)*64;
            binSortPacker.reset(newWidth,
                    newHeight);
        }
        Vector2 pos=binSortPacker.addRectangle(width, height);
        if(pos!=null) {
            return textInfos.size() - 1;
        }
        return -1;
    }

    /**
     * get text textregion
     * @param id
     * @return
     */
    public TextureRegion getTextRegion(int id){
        if(id<textInfos.size()){
            return textureRegions.get(id);
        }
       return null;
    }

    /**
     * render all text
     */
    public void renderTexture(){
        recalculateRectangle();
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
            Log.d("Text",new String(textInfo.data));
            Vector2 pos = binSortPacker.addRectangle(textInfo.width, textInfo.height);
            graphics2D.setPenAndBrush(textInfo.pen,textInfo.brush);
            graphics2D.drawChars(textInfo.font,
                    textInfo.fontSize,
                    textInfo.data,
                    (int)pos.x+BUFFER_SIZE,
                    (int)pos.y+BUFFER_SIZE);
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
