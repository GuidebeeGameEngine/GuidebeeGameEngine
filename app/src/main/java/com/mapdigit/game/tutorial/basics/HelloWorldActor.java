package com.mapdigit.game.tutorial.basics;

import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.utils.Disposable;

/**
 * Created by root on 10/22/15.
 */
public class HelloWorldActor extends Actor implements Disposable {

    private Colors colors=new Colors();
    private TextureRegion textureRegion;

    public HelloWorldActor(){
        textureRegion=new TextureRegion(colors.getTexture());

    }

    @Override
    public void draw(Batch batch,float alpha){
        int screenWidth= GameEngine.graphics.getWidth();
        int screenHeight =GameEngine.graphics.getHeight();
        int i=0;int j=0;
        //for(int i=0;i<=screenWidth/200;i++){
           // for(int j=0;j<=screenHeight/200;j++){
                batch.draw(textureRegion,i*200,j*200);
           // }
       // }

    }

    @Override
    public void dispose() {
        colors.dispose();

    }
}

