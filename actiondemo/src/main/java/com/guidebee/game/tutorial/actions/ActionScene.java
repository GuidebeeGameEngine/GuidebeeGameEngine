package com.guidebee.game.tutorial.actions;


import com.guidebee.game.camera.viewports.ExtendViewport;
import com.guidebee.game.scene.Scene;

import static com.guidebee.game.GameEngine.graphics;

public class ActionScene extends Scene {

    public ActionScene() {
        super(new ExtendViewport(800, 480));
    }

    @Override
    public void render(float delta) {

        super.render(delta);
        graphics.clearScreen(0, 0, 0.2f, 1);

    }

}
