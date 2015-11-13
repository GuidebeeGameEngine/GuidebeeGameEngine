package com.mapdigit.game.tutorial.microedition.actor;


import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.microedition.TiledLayer;

import static com.guidebee.game.GameEngine.*;


public class Background extends TiledLayer {

    private int[] cells = new int[]{
            0, 0, 1, 3, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 1,
            3, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 1, 4, 4, 3,
            0, 0, 0, 0, 1,
            2, 2, 0, 1, 4,
            4, 3, 0, 0, 0,
            0, 1, 2, 2, 2,
            1, 4, 4, 4, 4,
            3, 0, 0, 1, 4,
            4, 4, 1, 4, 4,
            4, 4, 3, 0, 0,
            1, 4, 4, 4, 4};

    private final int animatedIndex;

    private int animatedCount = 0;

    public Background() {
        super(25, 4, new TextureRegion(
                assetManager.get("tiles.png", Texture.class)),
                32, 32);
        setPosition(0, 0);
        for (int index = 0; index < cells.length; index++) {
            int c = index % 25;
            int r = 3 - index / 25;

            setCell(c, r, cells[index]);
        }
        animatedIndex = createAnimatedTile(5);

        for (int i = 0; i < 25; i++) {
            setCell(i, 0, animatedIndex);
        }

    }

    @Override
    public void draw(Batch batch, float alpha) {
        paint(batch);

    }

    @Override
    public void act(float delta) {
        animatedCount = animatedCount + 1;
        if (animatedCount > 10) {
            if (getAnimatedTile(animatedIndex) == 5) {
                setAnimatedTile(animatedIndex, 7);
            } else {
                setAnimatedTile(animatedIndex, 5);
            }

            animatedCount = 0;
        }

    }

}
