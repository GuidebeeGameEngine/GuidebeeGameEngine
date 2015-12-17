package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.TrueTypeFont;
import com.guidebee.game.tutorial.drawing.actor.FontList;

import static com.guidebee.game.GameEngine.assetManager;
public class GraphicsGamePlay extends GamePlay {


    @Override
    public void create() {
        loadAssets();
        setScreen(new DrawingWindow());


    }

    @Override
    public void dispose(){
        assetManager.dispose();

    }


    private void loadAssets(){

        for(int i=0;i< FontList.fontNames.length;i++) {
            assetManager.load("font/"+FontList.fontNames[i]+".fon", TrueTypeFont.class);
        }
        assetManager.finishLoading();



    }
}