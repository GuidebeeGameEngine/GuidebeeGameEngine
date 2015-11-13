package com.mapdigit.game.tutorial.coords;

import com.guidebee.game.GamePlay;
import com.guidebee.game.graphics.Texture;

import static com.guidebee.game.GameEngine.assetManager;


public class CoordinateGamePlay extends GamePlay {
    @Override
    public void create() {
        assetManager.load("coords.png",Texture.class);
        assetManager.finishLoading();
        CoordinateScene scene=new CoordinateScene();
        setScreen(scene);

    }
}
