/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//--------------------------------- PACKAGE ------------------------------------
package com.guidebee.game.scene;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.drawing.geometry.Area;
import com.guidebee.game.Collidable;
import com.guidebee.game.GameEngine;
import com.guidebee.game.InputAdapter;
import com.guidebee.game.camera.Camera;
import com.guidebee.game.camera.OrthographicCamera;
import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.camera.viewports.Viewport;
import com.guidebee.game.engine.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.guidebee.game.engine.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.guidebee.game.ui.UIComponent;
import com.guidebee.game.ui.UIContainer;
import com.guidebee.game.ui.UIWindow;
import com.guidebee.game.entity.Entity;
import com.guidebee.game.entity.EntityEngine;
import com.guidebee.game.entity.Role;
import com.guidebee.game.entity.directors.Director;
import com.guidebee.game.entity.utils.ImmutableArray;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Color;
import com.guidebee.game.graphics.SpriteBatch;
import com.guidebee.game.physics.Body;
import com.guidebee.game.physics.Contact;
import com.guidebee.game.physics.ContactFilter;
import com.guidebee.game.physics.ContactImpulse;
import com.guidebee.game.physics.ContactListener;
import com.guidebee.game.physics.Filter;
import com.guidebee.game.physics.Fixture;
import com.guidebee.game.physics.Manifold;
import com.guidebee.game.physics.World;
import com.guidebee.game.scene.actions.Action;
import com.guidebee.game.scene.collision.Collision;
import com.guidebee.game.scene.collision.CollisionListener;
import com.guidebee.game.scene.collision.SensorListener;
import com.guidebee.game.ui.GameController;
import com.guidebee.game.ui.Stack;
import com.guidebee.game.ui.Table;
import com.guidebee.game.ui.Widget;
import com.guidebee.game.ui.WidgetGroup;
import com.guidebee.math.Matrix4;
import com.guidebee.math.Vector2;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.Disposable;
import com.guidebee.utils.Scaling;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.SnapshotArray;


class InternalStage extends UIWindow {


    private final Stage proxyStage;
    /**
     * Creates a stage with the specified viewport and batch. This can be used to
     * avoid creating a new batch (which can be somewhat
     * slow) if multiple stages are used during an application's life time.
     *
     * @param batch Will not be disposed if {@link #dispose()} is called, handle
     *              disposal yourself.
     */
    public InternalStage(Viewport viewport, Batch batch,Stage stage) {
        super(viewport, batch);
        proxyStage=stage;

    }


    @Override
    public void drawExtra(Batch batch){
        if(proxyStage!=null){
            proxyStage.drawExtra(batch);
        }
    }




}

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Container containing hierarchies of {@link Actor actors}. Stage handles
 * the viewport and distributes input events.
 * <p/>
 * {@link #setViewport(Viewport)} controls the coordinates used within the stage
 * and sets up the camera used to convert between
 * stage coordinates and screen coordinates.
 * <p/>
 * A stage must receive input events so it can distribute them to actors. This is
 * typically done by passing the stage to
 * {@link com.guidebee.game.Input#setInputProcessor(com.guidebee.game.InputProcessor)
 * GameEngine.input.setInputProcessor}. An {@link com.guidebee.game.InputMultiplexer}
 * may be
 * used to handle input events before or after the stage does. If an actor handles
 * an event by returning true from the input
 * method, then the stage's input method will also return true, causing subsequent
 * InputProcessors to not receive the event.
 * <p/>
 *
 * @author mzechner
 * @author Nathan Sweet
 * @author James Shen
 */
public class Stage extends InputAdapter implements Disposable {


    private final Stack stack =new Stack();
    private final Table tableGameControl = new Table();

    private class ContactMonitor implements ContactListener {

        @Override
        public void beginContact(Contact contact) {

            if(sensorListener!=null){
                sensorListener.beginContact(contact);
            }
        }

        @Override
        public void endContact(Contact contact) {

            if(sensorListener!=null){
                sensorListener.endContact(contact);
            }


        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            Fixture fixture1 = contact.getFixtureA();
            Fixture fixture2 = contact.getFixtureB();
            if (fixture1 != null && fixture2 != null
                    && contact.isTouching()) {
                Collision collision = new Collision(contact);
                if (collisionListener != null) {
                    collisionListener.collisionDetected(collision);
                }
            }


        }
    }

    protected Array<Body> bodiesTobeDeleted = new Array<Body>();

    /**
     * associated entity engine object.
     */
    protected EntityEngine entityEngine;

    /**
     * associated box2d world
     */
    protected World world;

    /**
     * internal stage as a bride to engine.scene.Stage.
     */
    protected InternalStage internalStage;


    /**
     * internal stage as a bride to engine.scene.Stage.
     */
    protected InternalStage internalStageHUD;


    /**
     * Collision listener
     */
    protected CollisionListener collisionListener = null;


    /**
     * Sensor listener
     */
    protected SensorListener sensorListener=null;


    protected ContactMonitor contactMonitor = new ContactMonitor();

    protected Scenery scenery;


    protected int collisionType=Collidable.BOUNDING_RECT;


    /**
     * Creates a stage with a {@link com.guidebee.game.camera.viewports.ScalingViewport}
     * set to {@link com.guidebee.utils.Scaling#fill}. The stage
     * will use its own {@link Batch} which
     * will be disposed when the stage is disposed.
     */
    public Stage() {
        this(new ScalingViewport(Scaling.stretch, GameEngine.graphics.getWidth(),
                        GameEngine.graphics.getHeight(), new OrthographicCamera()),
                new SpriteBatch());
    }


    /**
     * Creates a stage with the specified viewport. The stage will use its own
     * {@link com.guidebee.game.graphics.Batch} which will be disposed when the stage
     * is disposed.
     */
    public Stage(Viewport viewport) {
        this(viewport, new SpriteBatch());
    }

    /**
     * Creates a stage with the specified viewport and batch. This can be used to
     * avoid creating a new batch (which can be somewhat
     * slow) if multiple stages are used during an application's life time.
     *
     * @param batch Will not be disposed if {@link #dispose()} is called, handle
     *              disposal yourself.
     */
    public Stage(Viewport viewport, Batch batch) {
        internalStage = new InternalStage(viewport, batch,this);
        internalStageHUD = new InternalStage(viewport, batch,null);
        entityEngine = new EntityEngine();
        internalStage.setUserObject(this);
        entityEngine.setUserObject(this);
        stack.setFillParent(true);
        internalStageHUD.addActor(stack);
        tableGameControl.setFillParent(true);
        stack.add(tableGameControl);
        tableGameControl.toFront();

    }

    public void drawExtra(Batch batch){

    }

    public void setGameController(GameController gameController){
        tableGameControl.addActor(gameController);
    }

    public void removeGameController(){
        tableGameControl.clear();
    }



    protected void performNoneBox2DCollisionChecking(){
        if(collisionListener!=null) {
            int allOtherTypes = Collidable.BOUNDING_AREA |
                    Collidable.BOUNDING_CIRCLE |
                    Collidable.BOUNDING_RECT;
            if ((collisionType & allOtherTypes) !=0) {
                Array<Collidable> collidables=getAllCollidables();
                collisionQuery((Collidable[])collidables.toArray(Collidable.class)
                        ,collisionType,collisionListener);

            }
        }
    }


    public void setSensorListener(SensorListener listener){
        boolean existingMonitor=sensorListener!=null || collisionListener!=null;
        sensorListener=listener;
        if(!existingMonitor && listener!=null){
            world.setContactListener(contactMonitor);
        }

    }


    public void setCollisionListener(CollisionListener listener) {
        setCollisionListener(listener, Collidable.BOUNDING_CIRCLE);
    }

    public void setCollisionListener(CollisionListener listener,int type) {
        if(listener==null){
            this.collisionListener = null;
            if(world!=null) {
                world.setContactListener(null);
            }
        }
        if((type & Collidable.BOX2D_CONTACT)==Collidable.BOX2D_CONTACT) {
            if (world != null) {
                world.setContactListener(contactMonitor);
                world.setContactFilter(new ContactFilter() {
                    @Override
                    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
                        Filter filterA = fixtureA.getFilterData();
                        Filter filterB = fixtureB.getFilterData();

                        if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
                            return filterA.groupIndex > 0;
                        }

                        boolean collide = (filterA.maskBits & filterB.categoryBits) != 0
                                && (filterA.categoryBits & filterB.maskBits) != 0;
                        return collide;
                    }
                });

            }
        }
        this.collisionListener=listener;
        this.collisionType=type;
    }

    /**
     * Returns the first actor found with the specified name. Note this
     * recursively compares the name of every actor in the group.
     */
    public <T extends Actor> T findActor(String name) {
        UIComponent actor = internalStage.findActor(name);
        if (actor != null) {
            return (T) actor.getUserObject();
        } else {
            return null;
        }
    }

    /**
     * Add UI component to stage.
     * @param widget
     */
    public void addHUDComponent(Widget widget){
        internalStageHUD.addActor(widget);
    }

    /**
     * add UI componet to stage.
     * @param widgetGroup
     */
    public void addHUDComponent(WidgetGroup widgetGroup){
        internalStageHUD.addActor(widgetGroup);
    }


    public void setScenery(Scenery scenery) {
        this.scenery = scenery;
        if (scenery.mapType == Scenery.MapType.OrthogonalTiled) {

            scenery.tiledMapRenderer = new OrthogonalTiledMapRenderer(scenery.tiledMap,getBatch());

            scenery.tiledMapRenderer.setView((OrthographicCamera)getCamera());
        } else if (scenery.mapType == Scenery.MapType.IsometricTiled) {
            scenery.tiledMapRenderer = new IsometricTiledMapRenderer(scenery.tiledMap, getBatch());
        }


    }


    /**
     * Get all collidable object in this stage.
     * @return
     */
    public Array<Collidable>  getAllCollidables(){
        Array<Collidable> collidables=new Array<Collidable>(false,32);
        Array<Actor> actors=getActors();
        for(Actor actor:actors){
            if(actor.isEnabled()){
                if(actor instanceof Group){
                    getAllCollidables((Group)actor,collidables);

                }else{
                    collidables.add(actor);
                }
            }
        }

        Array<Collidable> tiles=getAllMapCollidables();
        collidables.addAll(tiles);
        return collidables;
    }


    protected Array<Collidable> getAllMapCollidables(){
        Array<Collidable> collidables=new Array<Collidable>(false,32);
        if(this.scenery!=null){
            Array<Collidable> tiles=scenery.getAllCollidables();
            collidables.addAll(tiles);

        }
        return collidables;
    }

    protected void getAllCollidables(Group group,Array<Collidable> collidables){
        SnapshotArray<Actor> actors=group.getChildren();
        for(Actor actor:actors){
            if(actor.isEnabled()){
                if(actor instanceof Group){
                    getAllCollidables((Group)actor,collidables);

                }else{
                    collidables.add(actor);
                }
            }
        }

    }


    /**
     * Calls {@link #act(float)} with {@link com.guidebee.game.Graphics#getDeltaTime()}.
     */
    public void act() {
        act(Math.min(GameEngine.graphics.getDeltaTime(), 1 / 30f));
    }

    /**
     * Calls the {@link Actor#act(float)} method on each actor in the stage.
     * Typically called each frame. This method also fires
     * enter and exit events.
     *
     * @param delta Time in seconds since the last frame.
     */
    public void act(float delta) {

        if (world != null) {
            world.step(delta, GameEngine.VelocityIterations,
                    GameEngine.positionIterations);
            for (Body body : bodiesTobeDeleted) {
                world.destroyBody(body);
            }
            bodiesTobeDeleted.clear();

        }

        internalStage.act(delta);
        performNoneBox2DCollisionChecking();
        entityEngine.update(delta);


    }

    /**
     * Applies a touch down event to the stage and returns true if an
     * actor in the scene  handled} the event.
     */
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean ret=internalStageHUD.touchDown(screenX, screenY, pointer, button);
        if(!ret){
            ret=internalStage.touchDown(screenX, screenY, pointer, button);
        }
        return ret;
    }

    /**
     * Applies a touch moved event to the stage and returns true if an actor
     * in the scene  handled} the event.
     * Only {@link com.guidebee.game.ui.InputListener listeners} that returned true for touchDown
     * will receive this event.
     */
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean ret=internalStageHUD.touchDragged(screenX, screenY, pointer);
        if(!ret){
            ret=internalStage.touchDragged(screenX, screenY, pointer);
        }
        return ret;

    }

    /**
     * Applies a touch up event to the stage and returns true if an actor in
     * the scene  handled} the event.
     * Only {@link com.guidebee.game.ui.InputListener listeners} that returned true for touchDown
     * will receive this event.
     */
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean ret=internalStageHUD.touchUp(screenX, screenY, pointer, button);
        if(!ret){
            ret=internalStage.touchUp(screenX, screenY, pointer, button);
        }
        return ret;
    }

    /**
     * Applies a mouse moved event to the stage and returns true if an actor in
     * the scene  handled} the event.
     * This event only occurs on the desktop.
     */
    public boolean mouseMoved(int screenX, int screenY) {
        boolean ret=internalStageHUD.mouseMoved(screenX, screenY);
        if(!ret){
            ret=internalStage.mouseMoved(screenX, screenY);
        }
        return ret;
    }

    /**
     * Applies a mouse scroll event to the stage and returns true if an actor
     * in the scene  handled} the
     * event. This event only occurs on the desktop.
     */
    public boolean scrolled(int amount) {
        boolean ret=internalStageHUD.scrolled(amount);
        if(!ret){
            ret=internalStage.scrolled(amount);
        }
        return ret;

    }

    /**
     * Applies a key down event to the actor that has
     * {@link Stage#setKeyboardFocus(Actor) keyboard focus}, if any, and returns
     * true if the event was  handled}.
     */
    public boolean keyDown(int keyCode) {
        boolean ret=internalStageHUD.keyDown(keyCode);
        if(!ret){
            ret=internalStage.keyDown(keyCode);
        }
        return ret;
    }

    /**
     * Applies a key up event to the actor that has
     * {@link Stage#setKeyboardFocus(Actor) keyboard focus}, if any, and returns true
     * if the event was  handled}.
     */
    public boolean keyUp(int keyCode) {
        boolean ret=internalStageHUD.keyUp(keyCode);
        if(!ret){
            ret=internalStage.keyUp(keyCode);
        }
        return ret;
    }

    /**
     * Applies a key typed event to the actor that has
     * {@link Stage#setKeyboardFocus(Actor) keyboard focus}, if any, and returns
     * true if the event was  handled}.
     */
    public boolean keyTyped(char character) {
        boolean ret=internalStageHUD.keyTyped(character);
        if(!ret){
            ret=internalStage.keyTyped(character);
        }
        return ret;
    }


    /**
     * Adds an actor to the root of the stage.
     *
     * @see Group#addActor(Actor)
     * @see Actor#remove()
     */
    public void addActor(Actor actor) {
        if (actor instanceof Group) {
            Group group = (Group) actor;
            internalStage.addActor(group.internalGroup);

        } else {


            internalStage.addActor(actor.internalActor);
        }

        entityEngine.addEntity(actor.entity);
    }

    /**
     * Adds an action to the root of the stage.
     *
     * @see Group#addAction(com.guidebee.game.scene.actions.Action)
     */
    public void addAction(Action action) {
        internalStage.addAction(action);
    }

    /**
     * Returns the root's child actors.
     *
     * @see Group#getChildren()
     */
    public Array<Actor> getActors() {
        Array<UIComponent>
                internalActors = internalStage.getActors();
        Array<Actor> actors = null;
        if (internalActors != null) {
            actors = new Array<Actor>();
            for (UIComponent actor : internalActors) {
                actors.add((Actor) actor.getUserObject());
            }

        }
        return actors;

    }


    /**
     * Adds the Director to this Engine.
     *
     * @param director
     */
    public void addDirector(Director director) {
        entityEngine.addDirector(director);
    }

    /**
     * Removes the Director from this Engine.
     *
     * @param director
     */
    public void removeDirector(Director director) {
        entityEngine.removeDirector(director);
    }

    /**
     * Quick Director retrieval.
     *
     * @param directorType
     * @param <T>
     * @return
     */
    public <T extends Director> T getDirector(java.lang.Class<T> directorType) {
        return entityEngine.getDirector(directorType);
    }

    /**
     * @return immutable array of all entity directors managed by the EntityEngine.
     */
    public ImmutableArray<Director> getDirectors() {
        return entityEngine.getDirectors();
    }

    /**
     * @param role
     * @return Returns immutable collection of actors for the specified Role.
     * Will return the same instance every time.
     */
    public ImmutableArray<Actor> geActorsFor(Role role) {
        ImmutableArray<Entity> entities = entityEngine.getEntitiesFor(role);
        ImmutableArray<Actor> actors = null;
        if (entities != null) {
            Array<Actor> internalActors = new Array<Actor>();
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = entities.get(i);
                internalActors.add((Actor) entity.getUserObject());
            }
            actors = new ImmutableArray<Actor>(internalActors);
        }
        return actors;
    }


    public void initWorld() {
        if (this.world != null) {
            this.world.dispose();
        }
        this.world = new World(new Vector2(0, -10f), true);
        GameEngine.world = this.world;
    }

    public void setWorld(World world) {
        if (this.world != null) {
            this.world.dispose();
        }
        this.world = world;
        GameEngine.world = this.world;
    }


    public World getWorld() {
        return world;
    }

    /**
     * Removes the root's children, actions, and listeners.
     */
    public void clear() {
        entityEngine.removeAllEntities();
        internalStage.clear();
    }

    public void clearHUDComponents(){
        internalStageHUD.clear();
    }
    /**
     * Removes the touch, keyboard, and scroll focused actors.
     */
    public void unfocusAll() {
        internalStage.unfocusAll();
    }

    /**
     * Removes the touch, keyboard, and scroll focus for the
     * specified actor and any descendants.
     */
    public void unfocus(Actor actor) {
        internalStage.unfocus(actor.internalActor);
    }

    /**
     * Sets the actor that will receive key events.
     *
     * @param actor May be null.
     */
    public void setKeyboardFocus(Actor actor) {
        internalStage.setKeyboardFocus(actor.internalActor);
    }

    /**
     * Gets the actor that will receive key events.
     *
     * @return May be null.
     */
    public Actor getKeyboardFocus() {
        UIComponent
                actor = internalStage.getKeyboardFocus();
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }

    /**
     * Sets the actor that will receive scroll events.
     *
     * @param actor May be null.
     */
    public void setScrollFocus(Actor actor) {
        internalStage.setScrollFocus(actor.internalActor);
    }

    /**
     * Gets the actor that will receive scroll events.
     *
     * @return May be null.
     */
    public Actor getScrollFocus() {
        UIComponent
                actor = internalStage.getScrollFocus();
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }

    public Batch getBatch() {
        return internalStage.getBatch();
    }

    public Viewport getViewport() {
        return internalStage.getViewport();
    }

    public void setViewport(Viewport viewport) {
        internalStage.setViewport(viewport);
    }

    /**
     * The viewport's world width.
     */
    public float getWidth() {
        return internalStage.getWidth();
    }

    /**
     * The viewport's world height.
     */
    public float getHeight() {
        return internalStage.getHeight();
    }

    /**
     * The viewport's camera.
     */
    public Camera getCamera() {
        return internalStage.getCamera();
    }

    /**
     * Returns the root group which holds all actors in the stage.
     */
    public Group getRoot() {
        UIContainer UIContainer = internalStage.getRoot();
        if (UIContainer != null) {
            return (Group) UIContainer.getUserObject();
        }
        return null;
    }

    /**
     * Returns the {@link Actor} at the specified location in stage coordinates.
     * Hit testing is performed in the order the actors
     * were inserted into the stage, last inserted actors being tested first.
     * To get stage coordinates from screen coordinates, use
     * {@link #screenToStageCoordinates(Vector2)}.
     *
     * @return May be null if no actor was hit.
     */
    public Actor hit(float stageX, float stageY) {
        UIComponent actor
                = internalStage.hit(stageX, stageY, true);
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }

    /**
     * Transforms the screen coordinates to stage coordinates.
     *
     * @param screenCoords IInput screen coordinates and output for
     *                     resulting stage coordinates.
     */
    public Vector2 screenToStageCoordinates(Vector2 screenCoords) {
        return internalStage.screenToStageCoordinates(screenCoords);
    }

    /**
     * Transforms the stage coordinates to screen coordinates.
     *
     * @param stageCoords IInput stage coordinates and output for
     *                    resulting screen coordinates.
     */
    public Vector2 stageToScreenCoordinates(Vector2 stageCoords) {
        return internalStage.stageToScreenCoordinates(stageCoords);
    }

    /**
     * Transforms the coordinates to screen coordinates. The coordinates can be
     * anywhere in the stage since the transform matrix
     * describes how to convert them. The transform matrix is typically
     * obtained from {@link Batch#getTransformMatrix()} during
     * {@link Actor#draw(Batch, float)}.
     *
     * @see Actor#localToStageCoordinates(Vector2)
     */
    public Vector2 toScreenCoordinates(Vector2 coords, Matrix4 transformMatrix) {
        return internalStage.toScreenCoordinates(coords, transformMatrix);
    }

    /**
     * Calculates window scissor coordinates from local coordinates using the
     * batch's current transformation matrix.
     *
     * @see ScissorStack#calculateScissors(com.guidebee.game.camera.Camera, float, float, float, float,
     * Matrix4, Rectangle, Rectangle)
     */
    public void calculateScissors(Rectangle localRect, Rectangle scissorRect) {
        internalStage.calculateScissors(localRect, scissorRect);
    }

    /**
     * The default color that can be used by actors to draw debug lines.
     */
    public Color getDebugColor() {
        return internalStage.getDebugColor();
    }

    /**
     * If true, debug lines are shown for actors even when {@link Actor#isVisible()} is false.
     */
    public void setDebugInvisible(boolean debugInvisible) {
        internalStage.setDebugInvisible(debugInvisible);
    }

    /**
     * If true, debug lines are shown for all actors.
     */
    public void setDebugAll(boolean debugAll) {
        internalStage.setDebugAll(debugAll);
    }

    /**
     * If true, debug is enabled only for the actor under the mouse. Can be
     * combined with {@link #setDebugAll(boolean)}.
     */
    public void setDebugUnderMouse(boolean debugUnderMouse) {
        internalStage.setDebugUnderMouse(debugUnderMouse);
    }

    /**
     * If true, debug is enabled only for the parent of the actor under the
     * mouse. Can be combined with
     * {@link #setDebugAll(boolean)}.
     */
    public void setDebugParentUnderMouse(boolean debugParentUnderMouse) {
        internalStage.setDebugParentUnderMouse(debugParentUnderMouse);
    }

    /**
     * If not {@lin
     * nk Debug#none}, debug is enabled only for the first ascendant
     * of the actor under the mouse that is a table. Can
     * be combined with {@link #setDebugAll(boolean)}.
     *
     * @param debugTableUnderMouse May be null .
     */
    public void setDebugTableUnderMouse(Table.Debug debugTableUnderMouse) {
        internalStage.setDebugTableUnderMouse(debugTableUnderMouse);
    }

    public void draw() {



        internalStage.resetCamera();
        if (scenery != null) {
            scenery.renderBackgroundLayers();
        }
        internalStage.draw();
        if (scenery != null) {
            scenery.renderForegroundLayers();
        }

        internalStageHUD.draw();
    }

    /**
     * If true, debug is enabled only for the first ascendant of the actor under
     * the mouse that is a table. Can be combined with
     * {@link #setDebugAll(boolean)}.
     */
    public void setDebugTableUnderMouse(boolean debugTableUnderMouse) {
        internalStage.setDebugTableUnderMouse(debugTableUnderMouse);
    }

    @Override
    public void dispose() {
        internalStageHUD.setUserObject(null);
        internalStage.dispose();
        internalStageHUD.dispose();
        if (this.world != null) {
            this.world.dispose();
        }


    }

    public final static void collisionQuery(Collidable [] collidables,int collisionType,
                                            CollisionListener collisionListener){
        if(collisionListener!=null) {
            for (int i = 0; i < collidables.length - 1; i++) {
                for (int j = i + 1; j < collidables.length; j++) {
                    Collidable obj1 = collidables[i];
                    Collidable obj2 = collidables[j];
                    if (collisionQuery(obj1, obj2, collisionType)) {
                        Collision collision = new Collision(obj1, obj2,collisionType);
                        collisionListener.collisionDetected(collision);
                    }
                }
            }
        }
    }

    public final static boolean collisionQuery(Collidable collidable,Collidable otherCollidable){
        return collisionQuery(collidable, otherCollidable, Collidable.BOUNDING_CIRCLE);
    }

    public final static boolean collisionQuery(Collidable collidable,
                                               Collidable otherCollidable, int collisionType) {

        boolean result = true;
        if (collidable.isEnabled() && otherCollidable.isEnabled()) {
            if ((collisionType & Collidable.BOUNDING_RECT) == Collidable.BOUNDING_RECT) {
                result &= collidable.getBoundingAABB().overlaps(otherCollidable.getBoundingAABB());
            }
            if ((collisionType & Collidable.BOUNDING_CIRCLE) == Collidable.BOUNDING_CIRCLE) {
                result &= collidable.getBoundingCircle().overlaps(otherCollidable.getBoundingCircle());
            }
            if (((collisionType & Collidable.BOUNDING_AREA) == Collidable.BOUNDING_AREA)
                    || ((collisionType & Collidable.BOX2D_CONTACT) == Collidable.BOX2D_CONTACT)) {
                float[] vertices1 = collidable.getBoundingPolygon().getVertices();
                float[] vertices2 = otherCollidable.getBoundingPolygon().getVertices();

                int[] xpoints1 = new int[vertices1.length / 2];
                int[] xpoints2 = new int[vertices1.length / 2];
                int[] ypoints1 = new int[vertices1.length / 2];
                int[] ypoints2 = new int[vertices1.length / 2];

                for (int i = 0; i < vertices1.length / 2; i++) {
                    xpoints1[i] = Math.round(vertices1[i * 2]);
                    ypoints1[i] = Math.round(vertices1[i * 2 + 1]);
                    xpoints2[i] = Math.round(vertices2[i * 2]);
                    ypoints2[i] = Math.round(vertices2[i * 2 + 1]);
                }

                com.guidebee.drawing.geometry.Polygon polygon1
                        = new com.guidebee.drawing.geometry.Polygon(xpoints1, ypoints1, xpoints1.length);

                com.guidebee.drawing.geometry.Polygon polygon2
                        = new com.guidebee.drawing.geometry.Polygon(xpoints2, ypoints2, xpoints2.length);

                Area area1 = new Area(polygon1);
                Area area2 = new Area(polygon2);

                area1.intersect(area2);

                result &= !area1.isEmpty();
            }
        } else {
            result = false;
        }

        return result;

    }

}
