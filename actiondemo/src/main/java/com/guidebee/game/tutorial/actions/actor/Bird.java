package com.guidebee.game.tutorial.actions.actor;


import com.guidebee.game.graphics.Animation;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.utils.collections.Array;

import static com.guidebee.game.GameEngine.*;

public class Bird extends Actor {

    private float tick = 0.05f;
    private final Animation flyAnimation;
    private float elapsedTime = 0;



    public Bird(int x ,int y){
        super("Bird");
        TextureAtlas textureAtlas = assetManager.get("actions.atlas",
                TextureAtlas.class);
        TextureRegion bird1TextRegion = textureAtlas.findRegion("bird1");
        TextureRegion bird2TextRegion = textureAtlas.findRegion("bird2");
        TextureRegion bird3TextRegion = textureAtlas.findRegion("bird3");
        TextureRegion bird4TextRegion = textureAtlas.findRegion("bird4");

        Array<TextureRegion> keyFrames = new Array<TextureRegion>();
        keyFrames.add(bird1TextRegion);
        keyFrames.add(bird2TextRegion);
        keyFrames.add(bird3TextRegion);
        keyFrames.add(bird4TextRegion);

        flyAnimation = new Animation(tick, keyFrames);
        setTextureRegion(flyAnimation.getKeyFrame(0));
        setPosition(x,y);




    }

    @Override
    public void act(float delta) {
        elapsedTime += graphics.getDeltaTime();
        setTextureRegion(flyAnimation.getKeyFrame(elapsedTime, true));
    }
}
