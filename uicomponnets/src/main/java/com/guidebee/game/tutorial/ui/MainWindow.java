package com.guidebee.game.tutorial.ui;

import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.Color;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.tutorial.ui.component.BackgroundImage;
import com.guidebee.game.ui.Button;
import com.guidebee.game.ui.Label;
import com.guidebee.game.ui.Skin;
import com.guidebee.game.ui.Stack;
import com.guidebee.game.ui.Table;
import com.guidebee.game.ui.Window;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;

import static com.guidebee.game.GameEngine.*;


public class MainWindow extends Window{

    private final BackgroundImage backgroundImage;
    private final Stack stack=new Stack();
    private final Table table=new Table();



    public MainWindow(){
        super(Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT);

        stack.setFillParent(true);
        backgroundImage=new BackgroundImage();
        addComponent(stack);
        backgroundImage.setFillParent(true);
        stack.addComponent(backgroundImage);

        stack.addComponent(table);
        table.setFillParent(true);

        Label label=new Label("Sound",defaultSkin);
        label.setFontScale(2.0f);
        label.setColor(Color.BLUE);
        table.add(label);

        TextureAtlas textureAtlas=assetManager.get("skin/uidemo.atlas",TextureAtlas.class);
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





    }


}
