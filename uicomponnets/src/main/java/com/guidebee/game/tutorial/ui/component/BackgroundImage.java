package com.guidebee.game.tutorial.ui.component;


import com.guidebee.game.graphics.Texture;
import com.guidebee.game.ui.Image;

import static com.guidebee.game.GameEngine.assetManager;

public class BackgroundImage extends Image {

    public BackgroundImage(){
        super(assetManager.get("mainmenu_bkg.png",Texture.class));

    }

}
