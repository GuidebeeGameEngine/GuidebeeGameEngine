package com.mapdigit.game.tutorial.drop.actor;

import com.guidebee.game.Collidable;
import com.guidebee.game.GameEngine;
import com.guidebee.game.graphics.Animation;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.scene.Actor;
import com.guidebee.game.scene.Stage;
import com.guidebee.game.ui.GameControllerListener;
import com.guidebee.game.ui.Touchpad;
import com.guidebee.utils.collections.Array;
import com.mapdigit.game.tutorial.drop.camera.ViewPortConfiguration;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;


public class Mario extends Actor implements GameControllerListener {

    private final Animation forwardAnimation;
    private final Animation backwardAnimation;
    private final Animation leftAnimation;
    private final Animation rightAnimation;
    private final TextureRegion marioTextureRegion;

    private final int SPRITE_HEIGHT=64;
    private final int SPRITE_WIDTH=47;
    private final int SPRITE_FRAME_SIZE=6;
    private float elapsedTime = 0;
    private float tick=0.05f;
    private Direction currentDirection=Direction.NONE;

    private float oldX;
    private float oldY;

    private Array<Collidable> restrictedAreas =null;

    public Mario() {
        super("Mario");
        TextureAtlas textureAtlas=assetManager.get("raindrop.atlas",TextureAtlas.class);
        marioTextureRegion=textureAtlas.findRegion("mario2");
        Array<TextureRegion> keyFramesForward=new Array<TextureRegion>();
        Array<TextureRegion> keyFramesRight=new Array<TextureRegion>();
        Array<TextureRegion> keyFramesBackward=new Array<TextureRegion>();
        Array<TextureRegion> keyFramesLeft=new Array<TextureRegion>();
        int i=0;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(marioTextureRegion,
                    j*SPRITE_WIDTH,
                    i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesForward.add(textureRegion);

        }
        i++;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(marioTextureRegion,
                    j*SPRITE_WIDTH,i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesRight.add(textureRegion);

        }
        i++;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(marioTextureRegion,
                    j*SPRITE_WIDTH,i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesBackward.add(textureRegion);

        }
        i++;
        for(int j=0;j<SPRITE_FRAME_SIZE;j++){
            TextureRegion textureRegion=new TextureRegion(marioTextureRegion
                    ,j*SPRITE_WIDTH,i*SPRITE_HEIGHT,
                    SPRITE_WIDTH,SPRITE_HEIGHT );
            keyFramesLeft.add(textureRegion);

        }

        forwardAnimation=new Animation(tick,keyFramesForward);
        backwardAnimation=new Animation(tick,keyFramesBackward);
        rightAnimation=new Animation(tick,keyFramesRight);
        leftAnimation=new Animation(tick,keyFramesLeft);
        setTextureRegion(forwardAnimation.getKeyFrame(0));
        setPosition(ViewPortConfiguration.WIDTH/2-64/2,20);

    }


    public void setRestrictedAreas(Array<Collidable> treeArea){
        this.restrictedAreas =treeArea;
    }

    @Override
    public void KnobMoved(Touchpad touchpad, Direction direction) {

        currentDirection=direction;
        handleKeyPress();

    }

    @Override
    public void ButtonPressed(GameButton button) {

    }


    private boolean isCollideWithTree(){
        boolean collided=false;
        if(restrictedAreas !=null){

            for(Collidable collidable:restrictedAreas){
                collided |= Stage.collisionQuery(this, collidable);
            }

        }
        return collided;
    }

    public void stopMoving(){
        currentDirection=Direction.NONE;
        setX(oldX);
        setY(oldY);
    }


    private void handleKeyPress(){

        if(!isCollideWithTree()) {
            oldX=getX();
            oldY=getY();
            switch (currentDirection) {
                case NORTHWEST:
                    setTextureRegion(forwardAnimation.getKeyFrame(elapsedTime, true));
                    setY(getY() + 100 * graphics.getDeltaTime());
                    setX(getX() - 100 * graphics.getDeltaTime());
                    break;
                case NORTHEAST:
                    setTextureRegion(forwardAnimation.getKeyFrame(elapsedTime, true));
                    setY(getY() + 100 * graphics.getDeltaTime());
                    setX(getX() + 100 * graphics.getDeltaTime());
                    break;

                case SOUTHWEST:
                    setTextureRegion(backwardAnimation.getKeyFrame(elapsedTime, true));
                    setY(getY() - 100 * graphics.getDeltaTime());
                    setX(getX() - 100 * graphics.getDeltaTime());
                    break;
                case SOUTHEAST:
                    setTextureRegion(backwardAnimation.getKeyFrame(elapsedTime, true));
                    setY(getY() - 100 * graphics.getDeltaTime());
                    setX(getX() + 100 * graphics.getDeltaTime());
                    break;

                case WEST:
                    setTextureRegion(leftAnimation.getKeyFrame(elapsedTime, true));
                    setX(getX() - 200 * graphics.getDeltaTime());
                    break;
                case EAST:

                    setTextureRegion(rightAnimation.getKeyFrame(elapsedTime, true));
                    setX(getX() + 200 * graphics.getDeltaTime());
                    break;
                case NORTH:

                    setTextureRegion(forwardAnimation.getKeyFrame(elapsedTime, true));
                    setY(getY() + 200 * graphics.getDeltaTime());
                    break;
                case SOUTH:
                    setTextureRegion(backwardAnimation.getKeyFrame(elapsedTime, true));
                    setY(getY() - 200 * graphics.getDeltaTime());
                    break;

            }

            if (getX() < 0) {
                setX(0);
                currentDirection=Direction.NONE;
            }
            if (getY() < 0) {
                setY(0);
                currentDirection=Direction.NONE;
            }
            if (getX() > ViewPortConfiguration.WIDTH - 64) {
                setX(ViewPortConfiguration.WIDTH - 64);
                currentDirection=Direction.NONE;
            }
            if (getY() > ViewPortConfiguration.HEIGHT - 64) {
                setY(ViewPortConfiguration.HEIGHT - 64);
                currentDirection=Direction.NONE;
            }
        }else{

            stopMoving();
        }
    }

    @Override
    public void act(float delta){
        elapsedTime += GameEngine.graphics.getDeltaTime();
        handleKeyPress();

    }
}
