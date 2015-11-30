package com.guidebee.game.tutorial.actions;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.TextureAtlas;

import static com.guidebee.game.GameEngine.*;


public class ActionGamePlay extends GamePlay{
    @Override
    public void create() {
        loadAssets();
        ActionScene screen=new ActionScene();
        setScreen(screen);
    }

    @Override
    public void dispose(){
        assetManager.dispose();
    }

    private void loadAssets(){

        assetManager.load("actions.atlas", TextureAtlas.class);

        assetManager.finishLoading();
    }
}
