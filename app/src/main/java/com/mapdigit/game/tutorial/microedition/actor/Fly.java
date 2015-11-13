package com.mapdigit.game.tutorial.microedition.actor;


import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.microedition.Sprite;

import static com.guidebee.game.GameEngine.*;

public class Fly extends Sprite {

    public Fly() {
        super(assetManager.get("fly.png", Texture.class), 128, 64);
        setName("Fly");
        setBounds(getX(), getY(), getWidth(), getHeight());
        setPosition(128 / 2, 200);
        setFrameSequence(new int[]{0, 1, 2, 1, 0, 1, 2,
                1, 0, 1, 2, 1, 1, 1, 1, 1, 1});

    }

    @Override
    public void draw(Batch batch, float alpha) {
        paint(batch);
    }

    @Override
    public void act(float delta) {
        nextFrame();
    }
}
