package com.guidebee.game.tutorial.box2d.actor;

import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.Animation;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.physics.BodyDef;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.tutorial.box2d.Configuration;
import com.guidebee.game.ui.GameControllerListener;
import com.guidebee.game.ui.GameControllerListener.Direction;
import com.guidebee.game.ui.Touchpad;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.collections.Array;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;


public class Player extends Actor implements GameControllerListener {
    private final Animation forwardAnimation;
    private final Animation backwardAnimation;
    private final Animation leftAnimation;
    private final Animation rightAnimation;
    private final TextureRegion playerTextureRegion;


    private final int SPRITE_HEIGHT=32;
    private final int SPRITE_WIDTH=24;
    private final int SPRITE_FRAME_SIZE=3;
    private float elapsedTime = 0;
    private float tick=0.05f;
    private float speed =5f;
    private Direction currentDirection= Direction.NONE;

    private boolean isSelfControl=true;
    private float oldX;
    private float oldY;
    private float scale=1.0f;

    public Player(){
        super("Player");

        TextureAtlas textureAtlas=assetManager.get("box2d.atlas",TextureAtlas.class);
        playerTextureRegion =textureAtlas.findRegion("player");
        Array<TextureRegion> keyFramesForward=new Array<TextureRegion>();
        Array<TextureRegion> keyFramesRight=new Array<TextureRegion>();
        Array<TextureRegion> keyFramesBackward=new Array<TextureRegion>();
        Array<TextureRegion> keyFramesLeft=new Array<TextureRegion>();
        int i=0;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(playerTextureRegion,
                    j*SPRITE_WIDTH,
                    i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesForward.add(textureRegion);

        }
        i++;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(playerTextureRegion,
                    j*SPRITE_WIDTH,i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesRight.add(textureRegion);

        }
        i++;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(playerTextureRegion,
                    j*SPRITE_WIDTH,i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesBackward.add(textureRegion);

        }
        i++;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(playerTextureRegion
                    ,j*SPRITE_WIDTH,i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesLeft.add(textureRegion);

        }

        forwardAnimation=new Animation(tick,keyFramesForward);
        backwardAnimation=new Animation(tick,keyFramesBackward);
        rightAnimation=new Animation(tick,keyFramesRight);
        leftAnimation=new Animation(tick,keyFramesLeft);
        setTextureRegion(forwardAnimation.getKeyFrame(0));
        setPosition(Configuration.SCREEN_WIDTH / 2 - 64 / 2, 300);
        Rectangle boundRect=new Rectangle(2,0,SPRITE_WIDTH-4,SPRITE_HEIGHT-6);

        scaleBy(scale);
        initBody(BodyDef.BodyType.DynamicBody, boundRect);
        resetBodyWithSprite();
        setSelfControl(isSelfControl);
        getBody().setFixedRotation(true);
        scale=getScaleX();


    }

    @Override
    public void KnobMoved(Touchpad touchpad, Direction direction) {
        currentDirection=direction;
        handleKeyPress();
    }

    @Override
    public void ButtonPressed(GameButton button) {

    }

    private void handleKeyPress(){

         {
            oldX = getX();
            oldY=getY();
            switch (currentDirection) {
                case NORTHWEST:
                    setTextureRegion(forwardAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl){
                        setY(getY() + 100 * graphics.getDeltaTime());
                        setX(getX() - 100 * graphics.getDeltaTime());
                    }else {

                        getBody().setLinearVelocity(-speed, speed);
                    }
                    break;
                case NORTHEAST:
                    setTextureRegion(forwardAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setY(getY() + 100 * graphics.getDeltaTime());
                        setX(getX() + 100 * graphics.getDeltaTime());
                    }else {
                        getBody().setLinearVelocity(speed, speed);
                    }
                    break;

                case SOUTHWEST:
                    setTextureRegion(backwardAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setY(getY() - 100 * graphics.getDeltaTime());
                        setX(getX() - 100 * graphics.getDeltaTime());
                    }else {
                        getBody().setLinearVelocity(-speed, -speed);
                    }
                    break;
                case SOUTHEAST:
                    setTextureRegion(backwardAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setY(getY() - 100 * graphics.getDeltaTime());
                        setX(getX() + 100 * graphics.getDeltaTime());
                    }else {
                        getBody().setLinearVelocity(speed, -speed);
                    }
                    break;

                case WEST:
                    setTextureRegion(leftAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setX(getX() - 200 * graphics.getDeltaTime());
                    }
                    else {
                        getBody().setLinearVelocity(-speed, 0);
                    }
                    break;
                case EAST:

                    setTextureRegion(rightAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setX(getX() + 200 * graphics.getDeltaTime());
                    }else {
                        getBody().setLinearVelocity(speed, 0);
                    }
                    break;
                case NORTH:

                    setTextureRegion(forwardAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setY(getY() + 200 * graphics.getDeltaTime());
                    }else {
                        getBody().setLinearVelocity(0, speed);
                    }
                    break;
                case SOUTH:
                    setTextureRegion(backwardAnimation.getKeyFrame(elapsedTime, true));
                    if(isSelfControl) {
                        setY(getY() - 200 * graphics.getDeltaTime());
                    }else {
                        getBody().setLinearVelocity(0, -speed);
                    }
                    break;

            }


            if (getX() < 0) {
                setX(0);
                if(!isSelfControl) {
                    resetBodyWithSprite();
                }
                currentDirection=Direction.NONE;
            }
            if (getY() < 0) {
                setY(0);
                if(!isSelfControl) {
                    resetBodyWithSprite();
                }
                currentDirection=Direction.NONE;
            }
            if (getX() > Configuration.SCREEN_WIDTH - SPRITE_WIDTH*scale) {
                setX(Configuration.SCREEN_WIDTH - SPRITE_WIDTH*scale);
                if(!isSelfControl) {
                    resetBodyWithSprite();
                }
                currentDirection=Direction.NONE;
            }
            if (getY() > Configuration.SCREEN_HEIGHT - SPRITE_HEIGHT*scale) {
                setY(Configuration.SCREEN_HEIGHT - SPRITE_HEIGHT*scale);
                if(!isSelfControl) {
                    resetBodyWithSprite();
                }
                currentDirection=Direction.NONE;
            }
        }
    }


    public void stopMoving(){
        currentDirection=Direction.NONE;
        //setX(oldX);
        //setY(oldY);
    }
    @Override
    public void act(float delta){
        super.act(delta);
        elapsedTime += GameEngine.graphics.getDeltaTime();
        handleKeyPress();


    }

}
