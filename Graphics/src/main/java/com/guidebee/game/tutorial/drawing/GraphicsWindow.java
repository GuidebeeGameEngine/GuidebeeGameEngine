package com.guidebee.game.tutorial.drawing;

import com.guidebee.game.ui.Window;
import static com.guidebee.game.GameEngine.graphics;

public class GraphicsWindow extends Window {

    private final GraphicsGamePlay gamePlay;

    public GraphicsWindow(GraphicsGamePlay graphicsGamePlay){
        super(800,480);
        gamePlay=graphicsGamePlay;
    }

    @Override
    public void render(float delta){
        graphics.clearScreen(0.0f,0.1f,0.5f,1f);
        //super.render(delta);
    }

}
