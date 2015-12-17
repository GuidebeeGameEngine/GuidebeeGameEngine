package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.TrueTypeFont;

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


        assetManager.load("font/phillysans.fon", TrueTypeFont.class);
        assetManager.finishLoading();



    }
}