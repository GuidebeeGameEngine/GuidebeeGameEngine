package com.guidebee.game.tutorial.ui;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureAtlas;

import static com.guidebee.game.GameEngine.assetManager;


public class UIGamePlay extends GamePlay {

    @Override
    public void create() {
        loadAssets();
        MainWindow screen=new MainWindow();
        setScreen(screen);
    }

    @Override
    public void dispose(){
        assetManager.dispose();

    }

    private void loadAssets(){

        assetManager.load("mainmenu_bkg.png",Texture.class);
        assetManager.load("skin/uidemo.atlas", TextureAtlas.class);
        assetManager.finishLoading();



    }
}