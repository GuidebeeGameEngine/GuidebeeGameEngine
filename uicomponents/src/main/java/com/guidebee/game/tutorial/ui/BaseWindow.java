package com.guidebee.game.tutorial.ui;

import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.ui.Skin;
import com.guidebee.game.ui.Stack;
import com.guidebee.game.ui.Window;

import static com.guidebee.game.GameEngine.files;


public abstract class BaseWindow extends Window {

    protected final Stack stack=new Stack();

    protected final Skin uiskin;

    protected final UIGamePlay gamePlay;

    public BaseWindow(UIGamePlay gamePlay) {
        super(Configuration.SCREEN_WIDTH,
                Configuration.SCREEN_HEIGHT);
        stack.setFillParent(true);
        addComponent(stack);
        uiskin = new Skin(files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
        this.gamePlay=gamePlay;
    }
}
