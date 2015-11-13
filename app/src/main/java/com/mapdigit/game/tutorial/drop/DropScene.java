package com.mapdigit.game.tutorial.drop;

import com.guidebee.game.Collidable;
import com.guidebee.game.GameEngine;
import com.guidebee.game.InputProcessor;
import com.guidebee.game.audio.Music;
import com.guidebee.game.camera.viewports.*;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.game.maps.MapLayer;
import com.guidebee.game.maps.MapObject;
import com.guidebee.game.maps.tiled.TiledMap;
import com.guidebee.game.physics.Box2DDebugRenderer;
import com.guidebee.game.scene.Scene;
import com.guidebee.game.scene.Scenery;
import com.guidebee.game.ui.GameController;
import com.guidebee.game.ui.drawable.TextureRegionDrawable;
import com.guidebee.math.Matrix4;

import com.guidebee.utils.collections.Array;
import com.mapdigit.game.tutorial.drop.actor.Mario;
import com.mapdigit.game.tutorial.drop.actor.StaticArea;
import com.mapdigit.game.tutorial.drop.actor.RainDropGroup;
import com.mapdigit.game.tutorial.drop.camera.ViewPortConfiguration;
import com.mapdigit.game.tutorial.drop.director.CollisionDirector;
import com.mapdigit.game.tutorial.drop.hud.Score;

import static com.guidebee.game.GameEngine.assetManager;
import static com.guidebee.game.GameEngine.graphics;

public class DropScene extends Scene  {

    private final Music rainMusic;
    private final TiledMap background;

    private final Mario mario ;
    private final RainDropGroup rainDropGroup ;
    private final Score score;

    private final Scenery scenery;

    private InputProcessor savedInputProcessor;

    private final Matrix4 debugMatrix;
    private final Box2DDebugRenderer debugRenderer;


    public DropScene() {
        //super(new ScalingViewport(ViewPortConfiguration.WIDTH,
         //       ViewPortConfiguration.HEIGHT));
        super(new ExtendViewport(ViewPortConfiguration.WIDTH,
                      ViewPortConfiguration.HEIGHT));

        rainMusic = assetManager.get("rain.mp3", Music.class);
        background= assetManager.get("tiledmap/forest.tmx", TiledMap.class);
        scenery=new Scenery(background);
        sceneStage.initWorld();
        mario = new Mario();
        rainDropGroup = new RainDropGroup();
        score =new Score();

        TextureAtlas textureAtlas=assetManager.get("raindrop.atlas",TextureAtlas.class);
        GameController gameController
                = new GameController(new TextureRegionDrawable(textureAtlas.findRegion("Back")),
                new TextureRegionDrawable(textureAtlas.findRegion("Joystick")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Normal_Shoot")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Pressed_Shoot")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Normal_Virgin")),
                new TextureRegionDrawable(textureAtlas.findRegion("Button_08_Pressed_Virgin"))
        );
        gameController.addGameControllerListener(mario);

        MapLayer mapLayer=background.getLayers().get("Collision");
        MapObject treeCollisionArea=mapLayer.getObjects().get("TreeCollisionArea");
        treeCollisionArea.setEnabled(true);

        MapObject boxCollisionArea=mapLayer.getObjects().get("BoxCollisionArea");
        boxCollisionArea.setEnabled(true);


        sceneStage.initWorld();
        Array<Collidable> restrictedAreas=new Array<Collidable>();
        restrictedAreas.add(treeCollisionArea);
        restrictedAreas.add(boxCollisionArea);
        mario.setRestrictedAreas(restrictedAreas);
        rainMusic.setLooping(true);
        scenery.setBackGroundLayers(new int[]{0,1,2});
        scenery.setForeGroundLayers(new int[]{3,4});

        sceneStage.addActor(mario);
        sceneStage.addActor(rainDropGroup);

        sceneStage.setScenery(scenery);
        sceneStage.addHUDComponent(score);

        sceneStage.setCollisionListener(new CollisionDirector(score));
        debugMatrix=new Matrix4(sceneStage.getCamera().combined);
        debugMatrix.scale(GameEngine.pixelToBox2DUnit, GameEngine.pixelToBox2DUnit, 0);
        StaticArea platform = new StaticArea();



        sceneStage.addActor(platform);
        sceneStage.setGameController(gameController);
        debugRenderer = new Box2DDebugRenderer();


    }



    @Override
    public void hide() {
        GameEngine.input.setInputProcessor(savedInputProcessor);
    }

    @Override
    public void render(float delta) {
        graphics.clearScreen(0, 0, 0.2f, 1);
        super.render(delta);
        //debugRenderer.render(GameEngine.world, debugMatrix);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    @Override
    public void pause() {
        rainMusic.stop();

    }

    @Override
    public void show() {
        savedInputProcessor = GameEngine.input.getInputProcessor();
        GameEngine.input.setInputProcessor(sceneStage);
        rainMusic.play();

    }


}
