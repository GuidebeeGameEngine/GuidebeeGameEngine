package com.mapdigit.game.tutorial.drop;

import com.guidebee.game.GamePlay;
import com.guidebee.game.audio.Music;
import com.guidebee.game.audio.Sound;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.maps.tiled.TiledMap;

import static com.guidebee.game.GameEngine.assetManager;

public class DropGamePlay extends GamePlay{
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

        assetManager.load("raindrop.atlas", TextureAtlas.class);
        assetManager.load("tiledmap/forest.tmx", TiledMap.class);
        assetManager.load("coin.png", Texture.class);
        assetManager.load("drop.wav",Sound.class);
        assetManager.load("rain.mp3",Music.class);
        assetManager.finishLoading();
    }
}
