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


import com.guidebee.game.Collidable;
import com.guidebee.game.GameEngine;
import com.guidebee.game.ui.UIComponent;
import com.guidebee.game.ui.UIContainer;
import com.guidebee.game.ui.UIWindow;
import com.guidebee.game.entity.DataTrait;
import com.guidebee.game.entity.Entity;
import com.guidebee.game.entity.EntityEngine;
import com.guidebee.game.entity.utils.ImmutableArray;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Color;
import com.guidebee.game.graphics.ShapeRenderer;
import com.guidebee.game.graphics.Sprite;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.physics.Body;
import com.guidebee.game.physics.BodyDef;
import com.guidebee.game.physics.ChainShape;
import com.guidebee.game.physics.CircleShape;
import com.guidebee.game.physics.EdgeShape;
import com.guidebee.game.physics.FixtureDef;
import com.guidebee.game.physics.PolygonShape;
import com.guidebee.game.physics.Shape;
import com.guidebee.game.physics.World;
import com.guidebee.game.scene.actions.Action;
import com.guidebee.game.scene.actions.TweenAction;
import com.guidebee.math.Vector2;
import com.guidebee.math.geometry.Circle;
import com.guidebee.math.geometry.Polygon;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.collections.Array;

/**
 * Internal UIComponent act as a bridge to engine.sence.UIComponent.
 */
class InternalActor extends UIComponent {

    Actor proxyActor;

    public InternalActor(Actor proxyActor, String name) {
        super(name);
        this.proxyActor = proxyActor;
    }


    /**
     * Draws the actor. The batch is configured to draw in the parent's
     * coordinate system.
     * {@link com.guidebee.game.graphics.Batch#draw(com.guidebee.game.graphics.TextureRegion,
     * float, float, float, float, float, float, float, float, float)
     * This draw method} is convenient to draw a rotated and scaled TextureRegion.
     * {@link com.guidebee.game.graphics.Batch#begin()} has already been called on
     * the batch. If {@link com.guidebee.game.graphics.Batch#end()} is
     * called to draw without the batch then {@link com.guidebee.game.graphics.Batch#begin()}
     * must be called before the
     * method returns.
     * <p/>
     * The default implementation does nothing.
     *
     * @param parentAlpha Should be multiplied with the actor's alpha, allowing
     *                    a parent's alpha to affect all children.
     */
    public void draw(Batch batch, float parentAlpha) {
        proxyActor.draw(batch, parentAlpha);
    }

    /**
     * Updates the actor based on time. Typically this is called each frame
     * by {@link Stage#act(float)}.
     * <p/>
     * The default implementation calls {@link Action#act(float)} on
     * each action and removes actions that are complete.
     *
     * @param delta Time in seconds since the last frame.
     */
    public void act(float delta) {
        super.act(delta);
        proxyActor.resetSpriteDataTrait();
        proxyActor.act(delta);
    }

    /**
     * Draws a rectange for the bounds of this actor if {@link #getDebug()} is true.
     */
    protected void drawDebugBounds(ShapeRenderer shapes) {
        super.drawDebugBounds(shapes);
        proxyActor.drawDebugBounds(shapes);
    }

    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        proxyActor.drawDebug(shapes);
    }

    /**
     * Called when the actor's position has been changed.
     */
    protected void positionChanged() {
        super.positionChanged();
        proxyActor.positionChanged();
    }

    /**
     * Called when the actor's size has been changed.
     */
    protected void sizeChanged() {
        super.sizeChanged();
        proxyActor.sizeChanged();
    }

    /**
     * Called by the framework when this actor or any parent is added to a
     * group that is in the stage.
     *
     * @param stage May be null if the actor or any parent is no longer in a stage.
     */
    protected void setStage(UIWindow stage) {
        super.setStage(stage);


    }

    /**
     * Called by the framework when an actor is added to or removed from a group.
     *
     * @param parent May be null if the actor has been removed from the parent.
     */
    protected void setParent(UIContainer parent) {
        super.setParent(parent);
    }


}

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * An actor has a position, rectangular size, origin,
 * scale, rotation, Z index, and color. The position
 * corresponds to the unrotated, unscaled bottom left corner of the actor.
 * The position is relative to the actor's parent. The
 * origin is relative to the position and is used for scale and rotation.
 * <p>
 * An actor has a list of in progress
 * {@link com.guidebee.game.scene.actions.Action actions} that are applied
 * to the actor (often over time). These are generally
 * used to change the presentation of the actor (moving it, resizing it, etc).
 * See {@link #act(float)}, {@link com.guidebee.game.scene.actions.Action} and its
 * many subclasses.
 * <p>
 *
 * @author mzechner
 * @author Nathan Sweet
 * @author James Shen
 */
public class Actor implements Collidable {

    /**
     * internal actor
     */
    protected InternalActor internalActor;

    /**
     * associated entity object (from entity system)
     */
    public Entity entity;

    /**
     * associated sprite object.
     */
    protected Sprite sprite;

    /**
     * associated box2d body object.
     */
    protected Body body;

    /**
     * actor data trait (geometry, rotation, texture etc)
     */
    protected ActorDataTrait dataTrait;

    private Object userObject;

    private boolean selfControl=false;


    private float offsetX=0;

    private float offsetY=0;


    private boolean collisionEnabled=true;

    private Circle boundingCircle=new Circle();

    private Polygon boundingPolygon =new Polygon();


    /**
     * Draws a rectangle for the bounds of this actor if {@link #getDebug()} is true.
     */
    protected void drawDebugBounds(ShapeRenderer shapes) {

    }

    /**
     * Construct ,default name is class name
     */
    public Actor() {
        this(Actor.class.getName());
    }


    /**
     * constructor
     * @param name  name of the actor.
     */
    public Actor(String name) {
        internalActor = new InternalActor(this, name);
        entity = new Entity();
        dataTrait = new ActorDataTrait();
        dataTrait.boundingRect = new Rectangle();
        dataTrait.color = internalActor.getColor().cpy();
        entity.add(dataTrait);
        dataTrait.name=name;
        internalActor.setUserObject(this);
        entity.setUserObject(this);
    }


    /**
     * Draws the actor. The batch is configured to draw in the parent's
     * coordinate system.
     * {@link com.guidebee.game.graphics.Batch#draw(com.guidebee.game.graphics.TextureRegion,
     * float, float, float, float, float, float, float, float, float)
     * This draw method} is convenient to draw a rotated and scaled TextureRegion.
     * {@link com.guidebee.game.graphics.Batch#begin()} has already been called on
     * the batch. If {@link com.guidebee.game.graphics.Batch#end()} is
     * called to draw without the batch then {@link com.guidebee.game.graphics.Batch#begin()}
     * must be called before the
     * method returns.
     * <p/>
     * The default implementation does nothing.
     *
     * @param parentAlpha Should be multiplied with the actor's alpha, allowing
     *                    a parent's alpha to affect all children.
     */
    public void draw(Batch batch, float parentAlpha) {
        if(sprite!=null){
            sprite.draw(batch,parentAlpha);
        }
    }


    /**
     * Updates the actor based on time. Typically this is called each frame
     * by {@link Stage#act(float)}.
     * <p/>
     * The default implementation calls {@link Action#act(float)} on
     * each action and removes actions that are complete.
     *
     * @param delta Time in seconds since the last frame.
     */
    public void act(float delta) {
        if(body!=null ){
            if(selfControl){
                resetBodyWithSprite();
            }
            else{
                resetSpriteWithBody();
            }
        }

    }


    public void setSelfControl(Boolean control){
        this.selfControl=control;
        //if(body!=null && selfControl){
        //   body.setType(BodyDef.BodyType.KinematicBody);
        //}
    }

    /**
     * Returns the deepest actor that contains the specified point and is
     * touchable and
     * {@link #isVisible() visible}, or null if no actor was hit. The point
     * is specified in the actor's local coordinate system (0,0
     * is the bottom left of the actor and width,height is the upper right).
     * <p/>
     * This method is used to delegate touchDown, mouse, and enter/exit events.
     * If this method returns null, those events will not
     * occur on this UIComponent.
     * <p/>
     * The default implementation returns this actor if the point is within this
     * actor's bounds.
     *
     * @see com.guidebee.game.ui.Touchable
     */
    public Actor hit(float x, float y) {
        UIComponent actor = internalActor.hit(x, y, true);
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }


    /**
     * Init box2d body
     * @param bodyType body type
     * @param rect   the rectangle area for box2d body (in pixels). the box2d rectangle
     *               area size can be different with sprite area. normally they are equal
     */
    public void initBody(BodyDef.BodyType bodyType,Rectangle rect){

        initBody(bodyType, Shape.Type.Polygon, rect, 1.0f, 0, 1.0f);
    }

    /**
     * Init box2d body ,the box2d rectangle size is same as sprite size.
     * @param bodyType body type
     */
    public void initBody(BodyDef.BodyType bodyType){
        Rectangle rectangle=new Rectangle(0,0,getWidth(),getHeight());
        initBody(bodyType, Shape.Type.Polygon, rectangle, 1.0f, 0, 1.0f);
    }

    /**
     * Init box2d body
     * @param rectangle
     */
    public void initBody(Rectangle rectangle){
        initBody(BodyDef.BodyType.DynamicBody, Shape.Type.Polygon, rectangle, 1.0f, 0, 1.0f);
    }

    /**
     * Init box2d body ,the box2d rectangle size is same as sprite size.
     */
    public void initBody(){
        Rectangle rectangle = new Rectangle(0, 0, getWidth(), getHeight());
        initBody(BodyDef.BodyType.DynamicBody,Shape.Type.Polygon, rectangle, 1.0f,0,1.0f);
    }

    /**
     * Add a shape for existing body.
     * @param type
     * @param polygonShapes
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     * @param isSensor
     */
    public void addBodyShape(BodyDef.BodyType type ,Shape [] polygonShapes,Rectangle rect,
                        float density,float restitution,float friction,boolean isSensor){
        initBody(type,polygonShapes,rect,density,restitution,friction,
                true,isSensor,false,(short)0,(short)0,(short)0);

    }

    /**
     *
     * @param type
     * @param polygonShape
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     * @param isSensor
     */
    public void addBodyShape(BodyDef.BodyType type ,Shape polygonShape,Rectangle rect,
                             float density,float restitution,float friction,boolean isSensor){
        Shape [] polygonShapes=new Shape[1];
        polygonShapes[0]=polygonShape;
        initBody(type,polygonShapes,rect,density,restitution,friction,
                true,isSensor,false,(short)0,(short)0,(short)0);

    }

    /**
     * Add a shape for existing body.
     * @param type
     * @param polygonShapes
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     * @param isSensor
     */
    public void addBodyShape(BodyDef.BodyType type ,Shape [] polygonShapes,Rectangle rect,
                             float density,float restitution,float friction,boolean isSensor,
                             short categoryBits,short maskBits,short groupIndex){
        initBody(type,polygonShapes,rect,density,restitution,friction,
                true,isSensor,true,categoryBits,maskBits,groupIndex);

    }

    /**
     *
     * @param type
     * @param polygonShape
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     * @param isSensor
     */
    public void addBodyShape(BodyDef.BodyType type ,Shape polygonShape,Rectangle rect,
                             float density,float restitution,float friction,boolean isSensor,
                             short categoryBits,short maskBits,short groupIndex){
        Shape [] polygonShapes=new Shape[1];
        polygonShapes[0]=polygonShape;
        initBody(type,polygonShapes,rect,density,restitution,friction,
                true,isSensor,true,categoryBits,maskBits,groupIndex);

    }

    /**
     * Init box2d body
     * @param type
     * @param polygonShape
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     */
    public void initBody(BodyDef.BodyType type ,Shape polygonShape,Rectangle rect,
                         float density,float restitution,float friction){
        Shape [] polygonShapes=new Shape[1];
        polygonShapes[0]=polygonShape;
        initBody(type,polygonShapes,rect,density,restitution,friction);
    }


    /**
     * Init a body
     * @param type body type
     * @param polygonShapes  shapes for this body
     * @param rect    bound rectangle
     * @param density
     * @param restitution
     * @param friction
     * @param keepBody  keep existing body or create a new body
     * @param isSensor
     * @param useFilter
     * @param categoryBits
     * @param maskBits
     * @param groupIndex
     */
    public void initBody(BodyDef.BodyType type ,Shape[] polygonShapes,Rectangle rect,
                         float density,float restitution,float friction,boolean keepBody,
                         boolean isSensor,boolean useFilter,
                         short categoryBits,short maskBits,short groupIndex){
        if(GameEngine.world==null){
            throw new RuntimeException("Please initialize game engine world first");
        }
        if(this.body!=null && !keepBody){
            GameEngine.world.destroyBody(body);
        }

        BodyDef bodyDef=new BodyDef();
        bodyDef.type=type;

        bodyDef.position.set(GameEngine.toBox2D((getX()+ rect.getX()+ rect.getWidth()/2)),
                GameEngine.toBox2D(getY() + rect.getY()+ rect.getHeight()/2));
        this.body = GameEngine.world.createBody(bodyDef);
        offsetX = (getWidth()-rect.getWidth())/2-rect.getX();
        offsetY = (getHeight()-rect.getHeight())/2-rect.getY();
        setOrigin(getOriginX() - offsetX, getOriginY() - offsetY);
        for(int i=0;i<polygonShapes.length;i++) {

            Shape shape = polygonShapes[i];
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = density;
            fixtureDef.restitution = restitution;
            fixtureDef.friction = friction;
            fixtureDef.isSensor=isSensor;
            if(useFilter) {
                fixtureDef.filter.categoryBits = categoryBits;
                fixtureDef.filter.maskBits = maskBits;
                fixtureDef.filter.groupIndex = groupIndex;
            }
            body.createFixture(fixtureDef);
            shape.dispose();
        }
        body.setUserData(this);
    }

    /**
     * Init box2d body
     * @param type
     * @param polygonShapes
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     */
    public void initBody(BodyDef.BodyType type ,Shape[] polygonShapes,Rectangle rect,
                         float density,float restitution,float friction){
        initBody(type,polygonShapes,rect,density,restitution,friction,
                false,false,false,(short)0,(short)0,(short)0);

    }

    /**
     * Init box2d body ,the shape can be circle and rect, for other type uses rect.
     * @param type
     * @param shapeType
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     */
    public void initBody(BodyDef.BodyType type ,Shape.Type shapeType,Rectangle rect,
                         float density,float restitution,float friction){
        if(GameEngine.world==null){
            throw new RuntimeException("Please initialize game engine world first");
        }
        if(this.body!=null){
            GameEngine.world.destroyBody(body);
        }

        float scaleX=getScaleX();
        float scaleY=getScaleY();
        BodyDef bodyDef=new BodyDef();
        bodyDef.type=type;
        bodyDef.position.set(GameEngine.toBox2D((getX()+ rect.getX()+ rect.getWidth()/2)),
                GameEngine.toBox2D(getY() + rect.getY()+ rect.getHeight()/2));
        this.body = GameEngine.world.createBody(bodyDef);
        offsetX = (getWidth()-rect.getWidth())/2-rect.getX();
        offsetY = (getHeight()-rect.getHeight())/2-rect.getY();
        Shape shape;
        setOrigin(getOriginX() - offsetX, getOriginY() - offsetY);
        if(shapeType==Shape.Type.Circle){
            shape  = new CircleShape();
            shape.setRadius(GameEngine.toBox2D(Math.min(rect.getWidth()*scaleX/2,
                    rect.getHeight()*scaleY/2)));

        }else{
            shape  =  new PolygonShape();
            PolygonShape polygonShape=(PolygonShape)shape;
            polygonShape.setAsBox(GameEngine.toBox2D(rect.getWidth()*scaleX/2),
                    GameEngine.toBox2D(rect.getHeight()*scaleY/2));

        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution=restitution;
        fixtureDef.friction=friction;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();
    }


    /**
     * Initialize a chain shape.
     * @param chainShape
     * @param density
     * @param restitution
     * @param friction
     */
    public void initChainBody(ChainShape chainShape,float density,
                              float restitution,float friction){
        initChainBody(BodyDef.BodyType.StaticBody, chainShape, density, restitution, friction);
    }
    /**
     * Initialize a chain shape.
     * @param chainShape
     * @param density
     * @param restitution
     * @param friction
     */
    public void initChainBody(BodyDef.BodyType bodyType,ChainShape chainShape,float density,
                              float restitution,float friction){
        if(GameEngine.world==null){
            throw new RuntimeException("Please initialize game engine world first");
        }
        if(this.body!=null){
            GameEngine.world.destroyBody(body);
        }
        BodyDef bodyDef3 = new BodyDef();
        bodyDef3.type = bodyType;
        bodyDef3.position.set(GameEngine.toBox2D(getX()),GameEngine.toBox2D(getY()));
        FixtureDef fixtureDef3 = new FixtureDef();

        fixtureDef3.shape = chainShape;
        fixtureDef3.restitution=restitution;
        fixtureDef3.friction=friction;
        fixtureDef3.density=density;
        body= GameEngine.world.createBody(bodyDef3);
        body.createFixture(fixtureDef3);
        body.setUserData(this);


    }

    /**
     * Init an edge body.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param density
     * @param restitution
     * @param friction
     */
    public void initEdgeBody(float x1,float y1,float x2,float y2,
                             float density,float restitution,float friction){
        if(GameEngine.world==null){
            throw new RuntimeException("Please initialize game engine world first");
        }
        if(this.body!=null){
            GameEngine.world.destroyBody(body);
        }

        BodyDef bodyDef3 = new BodyDef();
        bodyDef3.type = BodyDef.BodyType.StaticBody;
        bodyDef3.position.set(GameEngine.toBox2D(getX()),GameEngine.toBox2D(getY()));
        FixtureDef fixtureDef3 = new FixtureDef();
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(GameEngine.toBox2D(x1) ,GameEngine.toBox2D(y1),
                GameEngine.toBox2D(x2),GameEngine.toBox2D(y2));
        fixtureDef3.shape = edgeShape;
        fixtureDef3.restitution=restitution;
        fixtureDef3.friction=friction;
        fixtureDef3.density=density;
        body= GameEngine.world.createBody(bodyDef3);
        body.createFixture(fixtureDef3);
        body.setUserData(this);
        edgeShape.dispose();

    }

    public final boolean collidesWith(Collidable otherCollidable){
        return collidesWith(otherCollidable, Collidable.BOUNDING_CIRCLE);
    }


    public final boolean collidesWith(Collidable otherCollidable,int collisionType){
       return Stage.collisionQuery(this, otherCollidable, collisionType);

    }

    public void setBody(Body body){
        this.body=body;
        this.body.setUserData(this);
    }


    public Body getBody(){
        return body;
    }

    /**
     * Removes this actor from its parent, if it has a parent.
     *
     * @see Group#removeActor(Actor)
     */
    public boolean remove() {
        internalActor.setUserObject(null);
        entity.setUserObject(null);

        EntityEngine entityEngine=getStage().entityEngine;
        entityEngine.removeEntity(entity);
        Stage stage=(Stage)entityEngine.getUserObject();
        World world=stage.getWorld();
        if(world!=null && body!=null){
            stage.bodiesTobeDeleted.add(body);
        }
        clear();
        return internalActor.remove();
    }


    public void addAction(Action action) {
        internalActor.addAction(action);
        if(action instanceof TweenAction){
            if(action.getActor()!=internalActor){
                throw new IllegalArgumentException("Tween action target mismatch");
            }
            TweenAction tweenAction=(TweenAction)action;
            tweenAction.start();
        }

    }

    public void removeAction(Action action) {
        internalActor.removeAction(action);
    }

    public Array<Action> getActions() {
        return internalActor.getActions();
    }

    /**
     * Removes all actions on this actor.
     */
    public void clearActions() {
        internalActor.clearActions();
    }


    /**
     * Removes all actions and listeners on this actor.
     */
    public void clear() {
        internalActor.clear();
    }

    /**
     * Returns the stage that this actor is currently in, or null if not in a stage.
     */
    public Stage getStage() {
        UIWindow stage = internalActor.getStage();
        if (stage != null) {
            return (Stage) stage.getUserObject();
        }

        return null;
    }

    /**
     * Called by the framework when this actor or any parent is added to a
     * group that is in the stage.
     *
     * @param stage May be null if the actor or any parent is no longer in a stage.
     */
    protected void setStage(Stage stage) {
        if(stage!=null) {
            internalActor.setStage(stage.internalStage);

        }else{
            internalActor.setStage(null);
        }
    }


    /**
     * Returns true if this actor is the same as or is the descendant of the
     * specified actor.
     */
    public boolean isDescendantOf(Actor actor) {
        if (actor == null)
            throw new IllegalArgumentException("actor cannot be null.");
        return internalActor.isDescendantOf(actor.internalActor);
    }

    /**
     * Returns true if this actor is the same as or is the ascendant of the specified actor.
     */
    public boolean isAscendantOf(Actor actor) {
        if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
        return internalActor.isAscendantOf(actor.internalActor);
    }

    public boolean overlaps(Actor other){
        return dataTrait.boundingRect.overlaps(other.dataTrait.boundingRect);
    }

    /**
     * Returns true if the actor's parent is not null.
     */
    public boolean hasParent() {
        return internalActor.hasParent();
    }

    /**
     * Returns the parent actor, or null if not in a group.
     */
    public Group getParent() {
        UIContainer UIContainer = internalActor.getParent();
        if (UIContainer != null) {
            return (Group) UIContainer.getUserObject();
        }
        return null;
    }


    /**
     * Called when the actor's position has been changed.
     */
    protected void positionChanged() {

    }

    /**
     * Called when the actor's size has been changed.
     */
    protected void sizeChanged() {
    }

    /**
     * Called by the framework when an actor is added to or removed from a group.
     *
     * @param parent May be null if the actor has been removed from the parent.
     */
    protected void setParent(Group parent) {
        if(parent!=null) {
            internalActor.setParent(parent.internalGroup);
        }else{
            internalActor.setParent(null);
        }
    }


    public boolean isVisible() {
        return internalActor.isVisible();
    }


    /**
     * Retrieves application specific object for convenience.
     */
    public Object getUserObject() {
        return userObject;
    }


    /**
     * Sets an application specific object for convenience.
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }


    /**
     * Add x and y to current position
     */
    public void moveBy(float x, float y) {
        internalActor.moveBy(x, y);
        dataTrait.x = internalActor.getX();
        dataTrait.y = internalActor.getY();
        resetSprite();


    }

    /**
     * Get the X position of the actor (bottom edge of actor)
     */
    public float getX() {
        return internalActor.getX();
    }

    /**
     * Get the Y position of the actor (bottom edge of actor)
     */
    public float getY() {
        return internalActor.getY();
    }


    /**
     * Adds the specified rotation to the current rotation.
     */
    public void rotateBy(float amountInDegrees) {
        internalActor.rotateBy(amountInDegrees);
        dataTrait.rotation = internalActor.getRotation();
        resetSprite();

    }

    /**
     * Adds the specified scale to the current scale.
     */
    public void scaleBy(float scale) {
        internalActor.scaleBy(scale);
        dataTrait.scaleX = internalActor.getScaleX();
        dataTrait.scaleY = internalActor.getScaleY();
        resetSprite();

    }

    /**
     * Adds the specified scale to the current scale.
     */
    public void scaleBy(float scaleX, float scaleY) {
        internalActor.scaleBy(scaleX, scaleY);
        dataTrait.scaleX = internalActor.getScaleX();
        dataTrait.scaleY = internalActor.getScaleY();
        resetSprite();
    }


    public float getRotation() {
        return internalActor.getRotation();
    }

    /**
     * Set bounds the x, y, width, and height.
     */
    public void setBounds(float x, float y, float width, float height) {
        internalActor.setBounds(x, y, width, height);
        dataTrait.x = x;
        dataTrait.y = y;
        dataTrait.width = width;
        dataTrait.height = height;
        resetSprite();

    }

    public float getOriginX() {
        return internalActor.getOriginX();
    }

    public float getCenterX() {
        return internalActor.getCenterX();
    }

    public float getCenterY() {
        return internalActor.getCenterY();
    }


    /**
     * Set position of UIComponent centered on x, y
     */
    public void setCenterPosition(float x, float y) {
        internalActor.setCenterPosition(x, y);
        dataTrait.x = internalActor.getX();
        dataTrait.y = internalActor.getY();
        dataTrait.boundingRect.x = dataTrait.x;
        dataTrait.boundingRect.y = dataTrait.y;
        resetSprite();
    }

    public void setColor(Color color) {
        internalActor.setColor(color);
        dataTrait.color.set(color);

    }


    public void setColor(float r, float g, float b, float a) {
        internalActor.setColor(r, g, b, a);
        dataTrait.color.set(r, g, b, a);
    }

    /**
     * Returns the color the actor will be tinted when drawn. The returned
     * instance can be modified to change the color.
     */
    public Color getColor() {
        return internalActor.getColor();
    }

    /**
     * Retrieve custom actor name set with {@link Actor#setName(String)},
     * used for easier identification
     */
    public String getName() {
        return internalActor.getName();
    }

    /**
     * Sets a name for easier identification of the actor in application code.
     *
     * @see Group#findActor(String)
     */
    public void setName(String name) {
        internalActor.setName(name);
        dataTrait.name=name;
    }


    /**
     * Add a DataTrait to this actor.If a DataTrait of the same type already
     * exists, it'll be replaced.
     * @param dataTrait
     * @return
     */
    public Actor addDataTrait(DataTrait dataTrait){
        entity.add(dataTrait);
        return this;
    }

    /**
     * Removes the DataTrait of the specified type. Since there is only ever
     * one dataTrait of one type, we don't need an instance reference.
     * @param dataTraitClass
     * @return
     */
    public DataTrait removeDataTrait(java.lang.Class<? extends DataTrait> dataTraitClass){
        return entity.remove(dataTraitClass);
    }

    /**
     * Removes all the DataTrait's from the actor.
     */
    public void removeAllDataTraits(){
        entity.removeAll();
    }

    /**
     *
     * @return immutable collection with all the Entity DataTraits.
     */
    public ImmutableArray<DataTrait> getDataTraits(){
        return entity.getDataTraits();
    }

    /**
     * Retrieve a dataTrait from this Entity by class.
     * @param dataTraitClass the class of the dataTrait to be retrieved.
     * @return the instance of the specified DataTrait attached to this Entity,
     * or null if no such DataTrait exists.
     */
    public <T extends DataTrait> T getDataTrait(java.lang.Class<T> dataTraitClass){
        return entity.getDataTrait(dataTraitClass);
    }


    /**
     * Returns y plus height.
     */
    public float getTop() {
        return internalActor.getTop();
    }

    /**
     * Returns x plus width.
     */
    public float getRight() {
        return internalActor.getRight();
    }


    public void setHeight(float height) {
        internalActor.setHeight(height);
        dataTrait.height = height;
        resetSprite();
    }

    /**
     * Sets the origin X and origin Y.
     */
    public void setOrigin(float originX, float originY) {
        internalActor.setOrigin(originX, originY);
        dataTrait.originX = originX;
        dataTrait.originY = originY;
        resetSprite();
    }

    public float getScaleX() {
        return internalActor.getScaleX();
    }

    public void setOriginX(float originX) {
        internalActor.setOriginX(originX);
        dataTrait.originX = internalActor.getOriginX();
    }

    public float getOriginY() {
        return internalActor.getOriginY();
    }

    public void setOriginY(float originY) {
        internalActor.setOriginY(originY);
        dataTrait.originY = internalActor.getOriginY();
        resetSprite();
    }

    /**
     * Set position of UIComponent to x, y (using bottom left corner of UIComponent)
     */
    public void setPosition(float x, float y) {
        internalActor.setPosition(x, y);
        dataTrait.x = x;
        dataTrait.y = y;
        resetSprite();
    }

    public void setRotation(float degrees) {
        internalActor.setRotation(degrees);
        dataTrait.rotation = degrees;
        resetSprite();

    }


    /**
     * Sets the scale X and scale Y.
     */
    public void setScale(float scaleX, float scaleY) {
        internalActor.setScale(scaleX, scaleY);
        dataTrait.scaleX = scaleX;
        dataTrait.scaleY = scaleY;
        resetSprite();
    }

    /**
     * Sets the scale for both X and Y
     */
    public void setScale(float scaleXY) {
        internalActor.setScale(scaleXY);
        dataTrait.scaleX = scaleXY;
        dataTrait.scaleY = scaleXY;
        resetSprite();
    }

    public void setScaleX(float scaleX) {
        internalActor.setScaleX(scaleX);
        dataTrait.scaleX = scaleX;
        resetSprite();
    }

    public float getScaleY() {
        return internalActor.getScaleY();
    }

    public void setScaleY(float scaleY) {
        internalActor.setScaleY(scaleY);
        dataTrait.scaleY = scaleY;
        resetSprite();
    }

    /**
     * Sets the width and height.
     */
    public void setSize(float width, float height) {
        internalActor.setSize(width, height);
        dataTrait.width = width;
        dataTrait.height = height;
        resetSprite();
    }

    /**
     * If false, the actor will not be drawn and will not receive touch events.
     * Default is true.
     */
    public void setVisible(boolean visible) {
        internalActor.setVisible(visible);
        dataTrait.visible = visible;
    }

    public float getHeight() {
        return internalActor.getHeight();
    }

    public void setWidth(float width) {
        internalActor.setWidth(width);
        dataTrait.width = width;
        resetSprite();
    }

    public void setX(float x) {
        internalActor.setX(x);
        dataTrait.x = x;
        resetSprite();
    }

    public void setY(float y) {
        internalActor.setY(y);
        dataTrait.y = y;
        resetSprite();
    }

    /**
     * Sets the z-index of this actor. The z-index is the index into the
     * parent's , where a
     * lower index is below a higher index. Setting a z-index higher than
     * the number of children will move the child to the front.
     * Setting a z-index less than zero is invalid.
     */
    public void setZIndex(int index) {
        internalActor.setZIndex(index);
        dataTrait.zIndex = index;
    }

    /**
     * Returns the z-index of this actor.
     *
     * @see #setZIndex(int)
     */
    public int getZIndex() {
        return internalActor.getZIndex();
    }

    public float getWidth() {
        return internalActor.getWidth();
    }

    /**
     * Calls {@link #clipBegin(float, float, float, float)}
     * to clip this actor's bounds.
     */
    public boolean clipBegin() {
        return internalActor.clipBegin();
    }

    /**
     * Clips the specified screen aligned rectangle, specified relative to the
     * transform matrix of the stage's Batch. The transform
     * matrix and the stage's camera must not have rotational components.
     * Calling this method must be followed by a call to
     * {@link #clipEnd()} if true is returned.
     *
     * @return false if the clipping area is zero and no drawing should occur.
     * @see ScissorStack
     */
    public boolean clipBegin(float x, float y, float width, float height) {
        return internalActor.clipBegin(x, y, width, height);
    }

    /**
     * Ends clipping begun by {@link #clipBegin(float, float, float, float)}.
     */
    public void clipEnd() {
        internalActor.clipEnd();
    }

    /**
     * Transforms the specified point in screen coordinates to the actor's
     * local coordinate system.
     */
    public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
        return internalActor.screenToLocalCoordinates(screenCoords);

    }


    /**
     * Transforms the specified point in the stage's coordinates to the
     * actor's local coordinate system.
     */
    public Vector2 stageToLocalCoordinates(Vector2 stageCoords) {
        return internalActor.stageToLocalCoordinates(stageCoords);
    }

    /**
     * Transforms the specified point in the actor's coordinates to be
     * in the stage's coordinates.
     *
     * @see Stage#toScreenCoordinates(Vector2, com.guidebee.math.Matrix4)
     */
    public Vector2 localToStageCoordinates(Vector2 localCoords) {
        return internalActor.localToStageCoordinates(localCoords);
    }

    /**
     * Transforms the specified point in the actor's coordinates to be
     * in the parent's coordinates.
     */
    public Vector2 localToParentCoordinates(Vector2 localCoords) {
        return internalActor.localToParentCoordinates(localCoords);
    }

    /**
     * Converts coordinates for this actor to those of a parent actor. The
     * ascendant does not need to be a direct parent.
     */
    public Vector2 localToAscendantCoordinates(Actor ascendant, Vector2 localCoords) {
        return internalActor.localToAscendantCoordinates(ascendant.internalActor, localCoords);
    }

    /**
     * Converts the coordinates given in the parent's coordinate system to
     * this actor's coordinate system.
     */
    public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
        return internalActor.parentToLocalCoordinates(parentCoords);
    }

    /**
     * Draws this actor's debug lines if {@link #getDebug()} is true.
     */
    public void drawDebug(ShapeRenderer shapes) {

    }

    /**
     * If true, {@link #drawDebug(ShapeRenderer)} will be called for this actor.
     */
    public void setDebug(boolean enabled) {
        internalActor.setDebug(enabled);
    }

    public boolean getDebug() {
        return internalActor.getDebug();
    }

    /**
     * Calls {@link #setDebug(boolean)} with {@code true}.
     */
    public Actor debug() {
        UIComponent actor = internalActor.debug();
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }

    public String toString() {
        return internalActor.toString();
    }


    /**
     * Adds the specified size to the current size.
     */
    public void sizeBy(float size) {
        internalActor.sizeBy(size);
        dataTrait.width = internalActor.getWidth();
        dataTrait.height = internalActor.getHeight();
        resetSprite();
    }

    /**
     * Adds the specified size to the current size.
     */
    public void sizeBy(float width, float height) {
        internalActor.sizeBy(width, height);
        dataTrait.width = internalActor.getWidth();
        dataTrait.height = internalActor.getHeight();
        resetSprite();
    }

    /**
     * Changes the z-order for this actor so it is in back of all siblings.
     */
    public void toBack() {
        internalActor.toBack();
        dataTrait.zIndex = internalActor.getZIndex();
    }

    /**
     * Changes the z-order for this actor so it is in front of all siblings.
     */
    public void toFront() {
        internalActor.toFront();
        dataTrait.zIndex = internalActor.getZIndex();
    }

    public void setSprite(Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("Sprite cannot be null");
        }
        this.sprite = sprite;
        setActorDataTrait();

    }

    public void setTextureRegion(TextureRegion textureRegion){
        if(this.sprite!=null){
            sprite.setRegion(textureRegion);
        }else {
            setSprite(new Sprite(textureRegion));
        }
    }

    public void setTexture(Texture texture) {

        if(this.sprite!=null){
            sprite.setRegion(texture);
        }else {
            setSprite(new Sprite(texture));
        }

    }

    public void setAlpha(float a) {
        if (sprite != null) {
            sprite.setAlpha(a);
        }
        dataTrait.alpha = a;

    }


    public void resetSpriteWithBody(){
        if (sprite != null) {
            sprite.setPosition(body.getPosition().x * GameEngine.pixelToBox2DUnit
                            -sprite.getWidth()/2+offsetX,
                    body.getPosition().y* GameEngine.pixelToBox2DUnit
                            -sprite.getHeight()/2+offsetY);
            sprite.setRotation((float)Math.toDegrees(body.getAngle()));
            setActorDataTrait();

        }
    }

    void resetSprite(){
        if (sprite != null) {
            sprite.setPosition(dataTrait.x, dataTrait.y);
            sprite.setScale(dataTrait.scaleX, dataTrait.scaleY);
            sprite.setOrigin(dataTrait.originX, dataTrait.originY);
            sprite.setRotation(dataTrait.rotation);
        }
        if(selfControl){
            resetBodyWithSprite();
        }

        if(body!=null &&  getBody().getType()== BodyDef.BodyType.KinematicBody){
            resetBodyWithSprite();
        }
    }

    public void resetBodyWithSprite(){
        if(body!=null){
            body.setTransform(GameEngine.toBox2D(dataTrait.x+dataTrait.width/2-offsetX),
                    GameEngine.toBox2D(dataTrait.y + dataTrait.height / 2-offsetY),
                    (float)Math.toRadians(dataTrait.rotation));
        }
    }

    void resetSpriteDataTrait() {
        dataTrait.boundingRect.x = internalActor.getX();
        dataTrait.boundingRect.y = internalActor.getY();
        dataTrait.boundingRect.width = internalActor.getWidth();
        dataTrait.boundingRect.height = internalActor.getHeight();
        dataTrait.centerX = internalActor.getX() + internalActor.getWidth() / 2;
        dataTrait.centerY = internalActor.getY() + internalActor.getHeight() / 2;
        dataTrait.color = internalActor.getColor();
        dataTrait.height = internalActor.getHeight();
        dataTrait.originX = internalActor.getOriginX();
        dataTrait.originY = internalActor.getOriginY();
        dataTrait.rotation = internalActor.getRotation();
        dataTrait.scaleX = internalActor.getScaleX();
        dataTrait.scaleY = internalActor.getScaleY();
        dataTrait.width = internalActor.getWidth();
        dataTrait.x = internalActor.getX();
        dataTrait.y = internalActor.getY();
        resetSprite();

    }


    protected void setActorDataTrait() {
        dataTrait.boundingRect.x = sprite.getX();
        dataTrait.boundingRect.y = sprite.getY();
        dataTrait.boundingRect.width = sprite.getWidth();
        dataTrait.boundingRect.height = sprite.getHeight();
        dataTrait.centerX = sprite.getX() + sprite.getWidth() / 2;
        dataTrait.centerY = sprite.getY() + sprite.getHeight() / 2;
        dataTrait.color = sprite.getColor();
        dataTrait.height = sprite.getHeight();
        dataTrait.originX = sprite.getOriginX();
        dataTrait.originY = sprite.getOriginY();
        dataTrait.rotation = sprite.getRotation();
        dataTrait.scaleX = sprite.getScaleX();
        dataTrait.scaleY = sprite.getScaleY();
        dataTrait.actor = this;
        dataTrait.width = sprite.getWidth();
        dataTrait.x = sprite.getX();
        dataTrait.y = sprite.getY();

        setX(dataTrait.x);
        setY(dataTrait.y);
        setWidth(dataTrait.width);
        setHeight(dataTrait.height);
        setScale(dataTrait.scaleX, dataTrait.scaleY);
        setRotation(dataTrait.rotation);
        setColor(dataTrait.color);
        setOrigin(dataTrait.originX, dataTrait.originY);

    }

    public void setCollisionEnabled(Boolean value){
        collisionEnabled=value;
    }

    @Override
    public boolean isEnabled() {
        return collisionEnabled;
    }

    @Override
    public Rectangle getBoundingAABB() {

        if(sprite!=null) {
            return sprite.getBoundingRectangle();
        }else{
            return new Rectangle(getX(),getY(),getWidth(),getHeight());
        }
    }



    @Override
    public Polygon getBoundingPolygon() {
        float []vertices =new float[]{getX(),getY(),getX()+getWidth(),getY(),
                getX()+getWidth(),getY()+getHeight(),getX(),getY()+getHeight()};
        boundingPolygon.setVertices(vertices);
        boundingPolygon.setOrigin(getX()+getWidth()/2,getY()+getHeight()/2);
        boundingPolygon.rotate(getRotation());
        return boundingPolygon;
     }

    @Override
    public Circle getBoundingCircle() {
       boundingCircle.setPosition(getCenterX(),getCenterY());
       boundingCircle.setRadius(Math.min(getWidth()/2,getHeight()/2));
       return boundingCircle;
    }

    /**
     * Datatrait for actors.
     */
    public static class ActorDataTrait extends DataTrait {
        /**
         * Color of the sprite
         */
        public Color color;

        public float centerX;

        public float centerY;

        public Rectangle boundingRect;

        public float originX;

        public float originY;

        public float x;

        public float y;

        public float width;

        public float height;

        public float scaleX;

        public float scaleY;

        public float rotation;

        public float alpha;

        public Actor actor;

        public int zIndex;

        public boolean visible;

        public String name;

    }
}
