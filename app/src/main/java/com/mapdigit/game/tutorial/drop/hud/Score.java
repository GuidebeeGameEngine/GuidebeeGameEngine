package com.mapdigit.game.tutorial.drop.hud;

import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.ui.HorizontalGroup;
import com.guidebee.game.ui.Image;
import com.guidebee.game.ui.Table;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;
import com.mapdigit.game.tutorial.drop.camera.ViewPortConfiguration;

import static com.guidebee.game.GameEngine.assetManager;


public class Score extends Table{

    private final Image goldCoin;
    private final Image thousands;
    private final Image hundreds;
    private final Image tens;
    private final Image units;


    private final TextureRegionDrawable [] numberDrawables;


    public Score(){
        TextureAtlas textureAtlas=assetManager.get("raindrop.atlas",
                TextureAtlas.class);
        goldCoin=new Image(assetManager.get("coin.png", Texture.class));
        TextureRegion numbers=textureAtlas.findRegion("numbers");
        numberDrawables=new TextureRegionDrawable[11];
        for(int i=0;i<10;i++){
            numberDrawables[i]
                    =new TextureRegionDrawable(
                    new TextureRegion(numbers,i*14,0,14,14));
        }
        thousands=new Image(numberDrawables[0]);
        hundreds =new Image(numberDrawables[0]);
        tens=new Image(numberDrawables[0]);
        units=new Image(numberDrawables[0]);
        HorizontalGroup space=new HorizontalGroup();
        space.padLeft(10);

        add(goldCoin);
        add(space);
        add(thousands);
        add(hundreds);
        add(tens);
        add(units);
        setSize(200, 40);
        setPosition(ViewPortConfiguration.WIDTH-200,
                ViewPortConfiguration.HEIGHT-50);
    }


    public void setScore(int score){
        int newScore= score % 10000;
        int thousandsNum = newScore / 1000;

        int hundredsNum = (newScore % 1000) / 100;
        int tensNum =(newScore % 100) /10;
        int unitsNum = (newScore % 10);

        thousands.setDrawable(numberDrawables[thousandsNum]);
        hundreds.setDrawable(numberDrawables[hundredsNum]);
        tens.setDrawable(numberDrawables[tensNum]);
        units.setDrawable(numberDrawables[unitsNum]);
    }
}
