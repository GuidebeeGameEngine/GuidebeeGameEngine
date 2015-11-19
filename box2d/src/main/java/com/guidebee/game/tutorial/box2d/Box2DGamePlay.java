package com.guidebee.game.tutorial.box2d;

import com.guidebee.game.GamePlay;

import static com.guidebee.game.GameEngine.assetManager;


public class Box2DGamePlay extends GamePlay {

    @Override
    public void create() {
        loadAssets();
        Box2DGameScene screen=new Box2DGameScene();
        setScreen(screen);
    }

    @Override
    public void dispose(){
        assetManager.dispose();
    }

    private void loadAssets(){


        assetManager.finishLoading();
    }
}