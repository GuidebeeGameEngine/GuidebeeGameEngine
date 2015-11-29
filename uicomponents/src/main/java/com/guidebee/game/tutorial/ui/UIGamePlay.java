package com.guidebee.game.tutorial.ui;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureAtlas;

import static com.guidebee.game.GameEngine.assetManager;


public class UIGamePlay extends GamePlay {

    private MainWindow mainWindow;
    private  SecondWindow secondWindow;

    @Override
    public void create() {
        loadAssets();
        mainWindow=new MainWindow(this);
        secondWindow=new SecondWindow(this);
        setScreen(mainWindow);
    }

    @Override
    public void dispose(){
        assetManager.dispose();

    }

    public void showMainWindow(){
        setScreen(mainWindow);
    }

    public void showSecondWindow(){
        setScreen(secondWindow);
    }

    private void loadAssets(){

        assetManager.load("mainmenu_bkg.png",Texture.class);
        assetManager.load("uidemo.atlas", TextureAtlas.class);
        assetManager.finishLoading();



    }
}