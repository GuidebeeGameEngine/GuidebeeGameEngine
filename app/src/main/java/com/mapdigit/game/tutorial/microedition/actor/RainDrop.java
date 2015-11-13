package com.mapdigit.game.tutorial.microedition.actor;

import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.microedition.Sprite;
import com.guidebee.math.MathUtils;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.Pool;
import com.guidebee.utils.Pools;
import com.guidebee.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;

import static com.guidebee.game.GameEngine.*;

public class RainDrop extends Sprite {

    private long lastDropTime = 0;
    private ArrayList<Rectangle> raindrops = new ArrayList<Rectangle>();

    private Rectangle bucketRect = new Rectangle(0, 0, 64, 64);
    Pool<Rectangle> rectPool = Pools.get(Rectangle.class);


    public RainDrop() {
        super(assetManager.get("droplet.png", Texture.class));
        setName("RainDrop");
        spawnRaindrop();


    }


    private void spawnRaindrop() {
        Rectangle raindrop = rectPool.obtain();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void draw(Batch batch, float alpha) {
        int len = raindrops.size();
        for (int index = 0; index < len; index++) {
            setPosition(raindrops.get(index).x, raindrops.get(index).y);
            paint(batch);
        }
    }

    @Override
    public void act(float delta) {
        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
        Bucket bucket = getStage().findActor("Bucket");


        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();

            raindrop.y -= 200 * graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) iter.remove();
            if (bucket != null) {
                bucketRect.x = bucket.getX();
                bucketRect.y = bucket.getY();
                if (raindrop.overlaps(bucketRect)) {

                    iter.remove();
                    rectPool.free(raindrop);

                }
            }

        }

    }


}