package com.guidebee.game.tutorial.ui;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.Texture;

import static com.guidebee.game.GameEngine.*;


public class UIGamePlay extends GamePlay {

    @Override
    public void create() {
        loadAssets();
        MainWindow screen=new MainWindow();
        setScreen(screen);
    }

    @Override
    public void dispose(){

    }

    private void loadAssets(){

        assetManager.load("mainmenu_bkg.png",Texture.class);
        assetManager.finishLoading();



    }
}