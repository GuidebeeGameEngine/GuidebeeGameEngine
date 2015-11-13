package com.mapdigit.game.tutorial.microedition;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.Texture;

import static com.guidebee.game.GameEngine.assetManager;

public class DropGamePlay extends GamePlay {
    @Override
    public void create() {
        loadAssets();
        DropScene screen=new DropScene();
        setScreen(screen);
    }

    @Override
    public void dispose(){
        assetManager.dispose();
    }

    private void loadAssets(){

        assetManager.load("droplet.png", Texture.class);
        assetManager.load("bucket.png", Texture.class);
        assetManager.load("tiles.png", Texture.class);
        assetManager.load("fly.png", Texture.class);
        assetManager.finishLoading();
    }
}
