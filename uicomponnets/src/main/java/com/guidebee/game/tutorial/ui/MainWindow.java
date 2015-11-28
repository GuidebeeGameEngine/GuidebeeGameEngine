package com.guidebee.game.tutorial.ui;

import com.guidebee.game.tutorial.ui.component.BackgroundImage;
import com.guidebee.game.ui.Stack;
import com.guidebee.game.ui.Window;


public class MainWindow extends Window{

    private final BackgroundImage backgroundImage;
    private final Stack stack=new Stack();

    public MainWindow(){
        super(Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT);

        stack.setFillParent(true);
        backgroundImage=new BackgroundImage();
        addComponent(stack);
        backgroundImage.setFillParent(true);
        stack.addComponent(backgroundImage);
    }


}
