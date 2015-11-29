package com.mapdigit.game.tutorial.microedition.actor;


import com.guidebee.game.Input;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.microedition.Sprite;
import com.guidebee.math.Vector3;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;
import static com.guidebee.game.GameEngine.input;

public class Bucket extends Sprite {


    public Bucket() {
        super(assetManager.get("bucket.png", Texture.class));
        setName("Bucket");
        setBounds(getX(), getY(), getWidth(), getHeight());
        setPosition(800 / 2 - 64 / 2, 20);

    }


    @Override
    public void draw(Batch batch, float alpha) {
        paint(batch);
    }


    @Override
    public void act(float delta) {
        if (input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(input.getX(), input.getY(), 0);
            getStage().getCamera().unproject(touchPos);
            setX(touchPos.x - 64 / 2);
        }
        if (input.isKeyPressed(Input.Keys.LEFT)) {
            setX(getX() - 200 * graphics.getDeltaTime());
        }
        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            setX(getX() + 200 * graphics.getDeltaTime());
        }

        if (getX() < 0) setX(0);
        if (getX() > 800 - 64) setX(800 - 64);
    }
}