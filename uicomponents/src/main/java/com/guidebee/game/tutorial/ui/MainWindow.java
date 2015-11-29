package com.guidebee.game.tutorial.ui;


import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.ui.Button;
import com.guidebee.game.ui.Event;
import com.guidebee.game.ui.EventListener;
import com.guidebee.game.ui.Image;
import com.guidebee.game.ui.Table;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;

import static com.guidebee.game.GameEngine.*;

public class MainWindow extends BaseWindow{



    public MainWindow(final UIGamePlay gamePlay){
        super(gamePlay);

        Image image=new Image(uiskin,"background");
        image.setFillParent(true);
        stack.addComponent(image);

        Table table=new Table();
        table.setFillParent(true);
        stack.addComponent(table);

        TextureAtlas textureAtlas=assetManager.get("uidemo.atlas",TextureAtlas.class);
        TextureRegion playButton=textureAtlas.findRegion("mainmenu_button_play");
        TextureRegion playUpTextRegion=new TextureRegion(playButton,0,0,
                playButton.getRegionWidth()/2,
                playButton.getRegionHeight());
        TextureRegion playDownTextRegion=new TextureRegion(playButton,
                playButton.getRegionWidth()/2,0,
                playButton.getRegionWidth()/2,
                playButton.getRegionHeight());

        Button button=new Button(new TextureRegionDrawable(playUpTextRegion)
                ,new TextureRegionDrawable(playDownTextRegion));

        table.add(button);

        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                gamePlay.showSecondWindow();
                return true;
            }
        });


    }


}
