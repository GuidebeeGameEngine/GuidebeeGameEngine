package com.guidebee.game.tutorial.ui;


import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.ui.Button;
import com.guidebee.game.ui.Event;
import com.guidebee.game.ui.EventListener;
import com.guidebee.game.ui.Image;
import com.guidebee.game.ui.Table;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;
import com.guidebee.math.Interpolation;

import static com.guidebee.game.GameEngine.*;
import static com.guidebee.game.ui.actions.Actions.*;
public class MainWindow extends BaseWindow{




    public MainWindow(final UIGamePlay gamePlay){
        super(gamePlay);

        Image image=new Image(uiskin,"background");
        image.setFillParent(true);
        stack.addComponent(image);

        Table table=new Table();
        table.setFillParent(true);
        stack.addComponent(table);

        Image birdImage=new Image(uiskin,"bird1");
        table.add(birdImage);
        table.row();

        birdImage.addAction(
                sequence(
                        moveTo(100, 100),
                        delay(1.0f),
                        parallel(
                                moveBy(50.f, 9, 5.0f),
                                rotateBy(360f, 5f, Interpolation.swingIn)
                        )


                ));


        TextureAtlas textureAtlas=assetManager
                .get("uidemo.atlas",
                        TextureAtlas.class);
        TextureRegion playButton=textureAtlas
                .findRegion("mainmenu_button_play");
        TextureRegion playUpTextRegion=new TextureRegion(
                playButton,0,0,
                playButton.getRegionWidth()/2,
                playButton.getRegionHeight());
        TextureRegion playDownTextRegion=new TextureRegion(
                playButton,
                playButton.getRegionWidth()/2,0,
                playButton.getRegionWidth()/2,
                playButton.getRegionHeight());

        Button button=new Button(
                new TextureRegionDrawable(playUpTextRegion)
                ,new TextureRegionDrawable(playDownTextRegion));

        table.add(button);

        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                gamePlay.setScreen(new SecondWindow(gamePlay));
                return false;
            }
        });


    }


}
