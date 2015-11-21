package com.guidebee.game.tutorial.box2d.stage;


import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.tutorial.box2d.actor.AnimatedFaceGroup;
import com.guidebee.game.tutorial.box2d.actor.Face;
import com.guidebee.game.tutorial.box2d.actor.Ground;
import com.guidebee.game.ui.GameController;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;

import static com.guidebee.game.GameEngine.assetManager;

public class SelfControlStage extends Box2DGameStage{


    private final Ground ground;
    private final Ground secondLevelGround;
    private final Ground thirdLevelGround;
    private final Face face;
    private final AnimatedFaceGroup animatedFaceGroup;

    public SelfControlStage(){
        super();
        TextureAtlas textureAtlas=assetManager.get("box2d.atlas",TextureAtlas.class);
        GameController gameController
                = new GameController(new TextureRegionDrawable(textureAtlas.findRegion("Back")),
                new TextureRegionDrawable(textureAtlas.findRegion("Joystick")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Normal_Shoot")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Pressed_Shoot")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Normal_Virgin")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Pressed_Virgin"))
        );
        setGameController(gameController);

        ground=new Ground();
        addActor(ground);
        secondLevelGround=new Ground(300,112,500);
        addActor(secondLevelGround);
        thirdLevelGround=new Ground(400,112*2,200);
        addActor(thirdLevelGround);
        face=new Face();
        addActor(face);
        animatedFaceGroup=new AnimatedFaceGroup();
        addActor(animatedFaceGroup);
    }
}
