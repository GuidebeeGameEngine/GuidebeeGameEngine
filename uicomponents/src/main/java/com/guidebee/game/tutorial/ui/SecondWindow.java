package com.guidebee.game.tutorial.ui;

import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.tutorial.ui.component.BackgroundImage;
import com.guidebee.game.ui.Button;
import com.guidebee.game.ui.Event;
import com.guidebee.game.ui.EventListener;
import com.guidebee.game.ui.HorizontalGroup;
import com.guidebee.game.ui.Table;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;
import com.guidebee.math.Interpolation;

import static com.guidebee.game.GameEngine.*;
import static com.guidebee.game.ui.actions.Actions.delay;
import static com.guidebee.game.ui.actions.Actions.moveBy;
import static com.guidebee.game.ui.actions.Actions.moveTo;
import static com.guidebee.game.ui.actions.Actions.parallel;
import static com.guidebee.game.ui.actions.Actions.rotateBy;
import static com.guidebee.game.ui.actions.Actions.rotateTo;
import static com.guidebee.game.ui.actions.Actions.sequence;


public class SecondWindow extends BaseWindow{

    public SecondWindow(final UIGamePlay gamePlay) {
        super(gamePlay);
        BackgroundImage backgroundImage=new BackgroundImage();
        backgroundImage.setFillParent(true);
        stack.add(backgroundImage);
        Table table=new Table();
        stack.add(table);
        HorizontalGroup horizontalGroup=new HorizontalGroup();

        Button scoreButton=new Button(uiskin,"score");
        Button shopButton=new Button(uiskin,"shop");

        table.padLeft(10);

        table.add(scoreButton);
        table.row();

        table.add(shopButton);
        table.pack();

        TextureAtlas textureAtlas=assetManager.get("uidemo.atlas",TextureAtlas.class);
        TextureRegion playButton=textureAtlas.findRegion("back");
        TextureRegion playUpTextRegion=new TextureRegion(playButton,0,0,
                playButton.getRegionWidth()/2,
                playButton.getRegionHeight());
        TextureRegion playDownTextRegion=new TextureRegion(playButton,
                playButton.getRegionWidth()/2,0,
                playButton.getRegionWidth()/2,
                playButton.getRegionHeight());

        Button button=new Button(new TextureRegionDrawable(playUpTextRegion)
                ,new TextureRegionDrawable(playDownTextRegion));


        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                gamePlay.setScreen(new MainWindow(gamePlay));
                return false;
            }
        });
        horizontalGroup.bottom();
        horizontalGroup.addComponent(button);

        stack.add(horizontalGroup);




    }

}
