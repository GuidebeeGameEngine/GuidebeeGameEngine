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

import com.guidebee.game.entity.EntityEngine;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Color;
import com.guidebee.game.graphics.ShapeRenderer;
import com.guidebee.game.physics.World;
import com.guidebee.game.ui.Cullable;
import com.guidebee.game.ui.UIComponent;
import com.guidebee.game.ui.UIContainer;
import com.guidebee.game.ui.UIWindow;
import com.guidebee.game.ui.actions.Action;
import com.guidebee.game.ui.actions.TweenAction;
import com.guidebee.math.Matrix4;
import com.guidebee.math.Vector2;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.SnapshotArray;

/**
 * internal group as a bridge to engine.scene.UIContainer
 */
class InternalGroup extends UIContainer {
    Group proxyGroup;





    public InternalGroup(Group proxyGroup,String name) {
        super(name);
        this.proxyGroup = proxyGroup;
    }


    public void act(float delta) {
        super.act(delta);
        proxyGroup.act(delta);
    }


    /**
     * Draws the group and its children. The default implementation
     * calls {@link #applyTransform(com.guidebee.game.graphics.Batch, Matrix4)}
     * if needed, then
     * {@link #drawChildren(com.guidebee.game.graphics.Batch, float)},
     * then {@link #resetTransform(com.guidebee.game.graphics.Batch)} if needed.
     */
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);
        proxyGroup.draw(batch,parentAlpha);
    }


    /**
     * Draws all children. {@link #applyTransform(Batch, Matrix4)} should be
     * called before and {@link #resetTransform(Batch)} after
     * this method if {@link #setTransform(boolean) transform} is true.
     * If {@link #setTransform(boolean) transform} is false these
     * methods don't need to be called, children positions are temporarily
     * offset by the group position when drawn. This method
     * avoids drawing children completely outside the
     * {@link #setCullingArea(Rectangle) culling area}, if set.
     */
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        proxyGroup.drawChildren(batch, parentAlpha);
    }

    /**
     * Draws all children. {@link #applyTransform(Batch, Matrix4)} should be
     * called before and {@link #resetTransform(Batch)} after
     * this method if {@link #setTransform(boolean) transform} is true. If
     * {@link #setTransform(boolean) transform} is false these
     * methods don't need to be called, children positions are temporarily
     * offset by the group position when drawn. This method
     * avoids drawing children completely outside the {@link #setCullingArea(Rectangle)
     * culling area}, if set.
     */
    protected void drawDebugChildren(ShapeRenderer shapes) {
        super.drawDebugChildren(shapes);
        proxyGroup.drawDebugChildren(shapes);
    }

    /**
     * Draws this actor's debug lines if {@link #getDebug()} is true and,
     * regardless of {@link #getDebug()}, calls
     * {@link Actor#drawDebug(ShapeRenderer)}
     * on each child.
     */
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        proxyGroup.drawDebug(shapes);
    }

    /**
     * Returns the transform for this group's coordinate system.
     */
    protected Matrix4 computeTransform() {
        return super.computeTransform();
    }

    /**
     * Set the batch's transformation matrix, often with the result of
     * {@link #computeTransform()}. Note this causes the batch to
     * be flushed. {@link #resetTransform(Batch)} will restore the transform
     * to what it was before this call.
     */
    protected void applyTransform(Batch batch, Matrix4 transform) {
        super.applyTransform(batch, transform);
    }

    /**
     * Restores the batch transform to what it was before
     * {@link #applyTransform(Batch, Matrix4)}. Note this causes the batch to be
     * flushed.
     */
    protected void resetTransform(Batch batch) {
        super.resetTransform(batch);
    }

    /**
     * Set the shape renderer transformation matrix, often with the result
     * of {@link #computeTransform()}. Note this causes the
     * shape renderer to be flushed. {@link #resetTransform(ShapeRenderer)}
     * will restore the transform to what it was before this
     * call.
     */
    protected void applyTransform(ShapeRenderer shapes, Matrix4 transform) {
        super.applyTransform(shapes, transform);
    }

    /**
     * Restores the shape renderer transform to what it was before
     * {@link #applyTransform(Batch, Matrix4)}. Note this causes the
     * shape renderer to be flushed.
     */
    protected void resetTransform(ShapeRenderer shapes) {
        super.resetTransform(shapes);
    }

    /**
     * Called when actors are added to or removed from the group.
     */
    protected void childrenChanged() {
        proxyGroup.childrenChanged();
    }

    protected void setStage(UIWindow stage) {
        super.setStage(stage);
    }

    protected void setParent(UIContainer parent) {
        super.setParent(parent);
    }

}

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Container may contain other actors.
 * <p>
 * Actors have a z-order equal to the order they were inserted into the group.
 * Actors inserted later will be drawn on top of
 * actors added earlier. Touch events that hit more than one actor are
 * distributed to topmost actors first.
 *
 * @author mzechner
 * @author Nathan Sweet
 * @author James Shen
 */
public class Group extends Actor implements Cullable {

    /**
     * internal group as a bridge to sence.UIContainer.
     */
    protected InternalGroup internalGroup;

    public Group(){
        this(Group.class.getName());
    }

    public Group(String name) {
        super(name);
        internalGroup = new InternalGroup(this,name);
        internalGroup.setUserObject(this);


    }

    public void act(float delta) {

    }

    /**
     * Draws the group and its children. The default implementation
     * calls {@link #applyTransform(com.guidebee.game.graphics.Batch, Matrix4)}
     * if needed, then
     * {@link #drawChildren(com.guidebee.game.graphics.Batch, float)},
     * then {@link #resetTransform(com.guidebee.game.graphics.Batch)} if needed.
     */
    public void draw(Batch batch, float parentAlpha){
    }

    /**
     * Draws all children. {@link #applyTransform(Batch, Matrix4)} should be
     * called before and {@link #resetTransform(Batch)} after
     * this method if {@link #setTransform(boolean) transform} is true.
     * If {@link #setTransform(boolean) transform} is false these
     * methods don't need to be called, children positions are temporarily
     * offset by the group position when drawn. This method
     * avoids drawing children completely outside the
     * {@link #setCullingArea(Rectangle) culling area}, if set.
     */
    protected void drawChildren(Batch batch, float parentAlpha) {


    }


    /**
     * Returns an ordered list of child actors in this group.
     */
    public SnapshotArray<Actor> getChildren() {

        SnapshotArray<UIComponent>
                internalActors = internalGroup.getChildren();
        SnapshotArray<Actor> actors = null;
        if (internalActors != null) {
            actors = new SnapshotArray<Actor>();
            for (UIComponent actor : internalActors) {
                actors.add((Actor) actor.getUserObject());
            }

        }
        return actors;
    }


    /**
     * When true (the default), the Batch is transformed so children are
     * drawn in their parent's coordinate system. This has a
     * performance impact because {@link Batch#flush()} must be done before
     * and after the transform. If the actors in a group are
     * not rotated or scaled, then the transform for the group can be set to
     * false. In this case, each child's position will be
     * offset by the group's position for drawing, causing the children to
     * appear in the correct location even though the Batch has
     * not been transformed.
     */
    public void setTransform(boolean transform) {
        internalGroup.setTransform(transform);
    }

    /**
     * Called when actors are added to or removed from the group.
     */
    protected void childrenChanged() {

    }

    /**
     * Returns the transform for this group's coordinate system.
     */
    protected Matrix4 computeTransform() {
        return internalGroup.computeTransform();
    }

    /**
     * Restores the shape renderer transform to what it was before
     * {@link #applyTransform(Batch, Matrix4)}. Note this causes the
     * shape renderer to be flushed.
     */
    protected void resetTransform(ShapeRenderer shapes) {
        internalGroup.resetTransform(shapes);
    }

    /**
     * Set the shape renderer transformation matrix, often with the result
     * of {@link #computeTransform()}. Note this causes the
     * shape renderer to be flushed. {@link #resetTransform(ShapeRenderer)}
     * will restore the transform to what it was before this
     * call.
     */
    protected void applyTransform(ShapeRenderer shapes, Matrix4 transform) {
        internalGroup.applyTransform(shapes, transform);
    }

    /**
     * Set the batch's transformation matrix, often with the result of
     * {@link #computeTransform()}. Note this causes the batch to
     * be flushed. {@link #resetTransform(Batch)} will restore the transform
     * to what it was before this call.
     */
    protected void applyTransform(Batch batch, Matrix4 transform) {
        internalGroup.applyTransform(batch, transform);
    }

    /**
     * Restores the batch transform to what it was before
     * {@link #applyTransform(Batch, Matrix4)}. Note this causes the batch to be
     * flushed.
     */
    protected void resetTransform(Batch batch) {
        internalGroup.resetTransform(batch);
    }

    /**
     * Draws all children. {@link #applyTransform(Batch, Matrix4)} should be
     * called before and {@link #resetTransform(Batch)} after
     * this method if {@link #setTransform(boolean) transform} is true. If
     * {@link #setTransform(boolean) transform} is false these
     * methods don't need to be called, children positions are temporarily
     * offset by the group position when drawn. This method
     * avoids drawing children completely outside the {@link #setCullingArea(Rectangle)
     * culling area}, if set.
     */
    protected void drawDebugChildren(ShapeRenderer shapes) {

    }

    /**
     * Draws this actor's debug lines if {@link #getDebug()} is true and,
     * regardless of {@link #getDebug()}, calls
     * {@link Actor#drawDebug(ShapeRenderer)}
     * on each child.
     */
    public void drawDebug(ShapeRenderer shapes) {

    }

    @Override
    public void setCullingArea(Rectangle cullingArea) {
        internalGroup.setCullingArea(cullingArea);
    }


    /**
     * If true, {@link #drawDebug(ShapeRenderer)} will be called for this group and,
     * optionally, all children recursively.
     */
    public void setDebug(boolean enabled, boolean recursively) {
        internalGroup.setDebug(enabled,recursively);
    }

    public Actor hit(float x, float y) {
        UIComponent actor = internalGroup.hit(x, y, true);
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }

    /**
     * Removes this actor from its parent, if it has a parent.
     *
     * @see Group#removeActor(Actor)
     */
    public boolean remove() {
        entity.setUserObject(null);
        internalGroup.setUserObject(null);
        EntityEngine entityEngine=getStage().entityEngine;
        entityEngine.removeEntity(entity);
        Stage stage=(Stage)entityEngine.getUserObject();
        World world=stage.getWorld();
        if(world!=null && body!=null){
            stage.bodiesTobeDeleted.add(body);
        }
        return internalGroup.remove();
    }

    /**
     * Adds an actor as a child of this group. The actor is first
     * removed from its parent group, if any.
     *
     * @see #remove()
     */
    public void addActor(Actor actor) {
        internalGroup.addActor(actor.internalActor);
        actor.setParent(this);
        actor.setStage(getStage());
    }

    public void addAction(Action action) {
        internalGroup.addAction(action);
        if(action instanceof TweenAction){
            if(action.getActor()!=internalGroup){
                throw new IllegalArgumentException("Tween action target mismatch");
            }
            TweenAction tweenAction=(TweenAction)action;
            tweenAction.start();
        }

    }

    public void removeAction(Action action) {
        internalGroup.removeAction(action);
    }

    public Array<Action> getActions() {
        return internalGroup.getActions();
    }

    /**
     * Removes all actions on this actor.
     */
    public void clearActions() {
        internalGroup.clearActions();
    }



    /**
     * Adds an actor as a child of this group, at a specific index.
     * The actor is first removed from its parent group, if any.
     *
     * @param index May be greater than the number of children.
     */
    public void addActorAt(int index, Actor actor) {
        internalGroup.addActorAt(index, actor.internalActor);
        actor.setParent(this);
        actor.setStage(getStage());
    }

    /**
     * Adds an actor as a child of this group, immediately before
     * another child actor. The actor is first removed from its parent
     * group, if any.
     */
    public void addActorBefore(Actor actorBefore, Actor actor) {
        internalGroup.addActorBefore(actorBefore.internalActor, actor.internalActor);
        actor.setParent(this);
        actor.setStage(getStage());
    }

    /**
     * Returns the stage that this actor is currently in, or null if not in a stage.
     */
    public Stage getStage() {
        UIWindow stage = internalGroup.getStage();
        if (stage != null) {
            return (Stage) stage.getUserObject();
        }

        return null;
    }

    /**
     * Removes an actor from this group. all the actor will remove itself.
     * after removeActor, this actor is no longer useable.
     * {@link com.guidebee.game.ui.actions.Action#setPool(com.guidebee.utils.Pool) pool},
     * if any. This is not done automatically.
     */
    public boolean removeActor(Actor actor) {
        actor.remove();
        actor.setParent(null);
        actor.setStage(null);
        return internalGroup.removeActor(actor.internalActor);
    }

    /**
     * Removes all actors from this group.
     */
    public void clearChildren() {

        SnapshotArray<Actor> actors=getChildren();
        for(Actor actor:actors){
            removeActor(actor);
        }

        internalGroup.clearChildren();

    }

    /**
     * Removes all children, actions, and listeners from this group.
     */
    public void clear() {
        SnapshotArray<Actor> actors=getChildren();
        for(Actor actor:actors){
            removeActor(actor);
        }
        internalGroup.clear();
    }

    /**
     * Returns the first actor found with the specified name. Note this
     * recursively compares the name of every actor in the group.
     */
    public <T extends Actor> T findActor(String name) {
        UIComponent actor = internalGroup.findActor(name);
        if (actor != null) {
            return (T) actor.getUserObject();
        }
        return null;

    }

    protected void setStage(Stage stage) {
        super.setStage(stage);
        internalGroup.setStage(stage.internalStage);

    }

    /**
     * Returns true if this actor is the same as or is the descendant of the
     * specified actor.
     */
    public boolean isDescendantOf(Actor actor) {
        if (actor == null)
            throw new IllegalArgumentException("actor cannot be null.");

        if(actor instanceof Group){
          Group group=(Group)actor;
          return internalGroup.isDescendantOf(group.internalGroup);
        }

        return internalGroup.isDescendantOf(actor.internalActor);
    }


    /**
     * Returns true if this actor is the same as or is the ascendant of the specified actor.
     */
    public boolean isAscendantOf(Actor actor) {
        if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
        if(actor instanceof Group){
            Group group=(Group)actor;
            return internalGroup.isAscendantOf(group.internalGroup);
        }
        return internalGroup.isAscendantOf(actor.internalActor);
    }

    /**
     * Returns true if the actor's parent is not null.
     */
    public boolean hasParent() {
        return internalGroup.hasParent();
    }

    /**
     * Returns the parent actor, or null if not in a group.
     */
    public Group getParent() {
        UIContainer UIContainer = internalGroup.getParent();
        if (UIContainer != null) {
            return (Group) UIContainer.getUserObject();
        }
        return null;
    }

    /**
     * Called by the framework when an actor is added to or removed from a group.
     *
     * @param parent May be null if the actor has been removed from the parent.
     */
    protected void setParent(Group parent) {
        internalGroup.setParent(parent.internalGroup);
    }


    public boolean isVisible() {
        return internalGroup.isVisible();
    }

    /**
     * Add x and y to current position
     */
    public void moveBy(float x, float y) {
        internalGroup.moveBy(x, y);
        dataTrait.x = internalGroup.getX();
        dataTrait.y = internalGroup.getY();
        resetSprite();


    }

    /**
     * Get the X position of the actor (bottom edge of actor)
     */
    public float getX() {
        return internalGroup.getX();
    }

    /**
     * Get the Y position of the actor (bottom edge of actor)
     */
    public float getY() {
        return internalGroup.getY();
    }


    /**
     * Adds the specified rotation to the current rotation.
     */
    public void rotateBy(float amountInDegrees) {
        internalGroup.rotateBy(amountInDegrees);
        dataTrait.rotation = internalGroup.getRotation();
        resetSprite();

    }

    /**
     * Adds the specified scale to the current scale.
     */
    public void scaleBy(float scale) {
        internalGroup.scaleBy(scale);
        dataTrait.scaleX = internalGroup.getScaleX();
        dataTrait.scaleY = internalGroup.getScaleY();
        resetSprite();

    }

    /**
     * Adds the specified scale to the current scale.
     */
    public void scaleBy(float scaleX, float scaleY) {
        internalGroup.scaleBy(scaleX, scaleY);
        dataTrait.scaleX = internalGroup.getScaleX();
        dataTrait.scaleY = internalGroup.getScaleY();
        resetSprite();
    }


    public float getRotation() {
        return internalGroup.getRotation();
    }

    /**
     * Set bounds the x, y, width, and height.
     */
    public void setBounds(float x, float y, float width, float height) {
        internalGroup.setBounds(x, y, width, height);
        dataTrait.x = x;
        dataTrait.y = y;
        dataTrait.width = width;
        dataTrait.height = height;
        resetSprite();

    }

    public float getOriginX() {
        return internalGroup.getOriginX();
    }

    public float getCenterX() {
        return internalGroup.getCenterX();
    }

    public float getCenterY() {
        return internalGroup.getCenterY();
    }


    /**
     * Set position of UIComponent centered on x, y
     */
    public void setCenterPosition(float x, float y) {
        internalGroup.setCenterPosition(x, y);
        dataTrait.x = internalGroup.getX();
        dataTrait.y = internalGroup.getY();
        dataTrait.boundingRect.x = dataTrait.x;
        dataTrait.boundingRect.y = dataTrait.y;
        resetSprite();
    }

    public void setColor(Color color) {
        internalGroup.setColor(color);
        dataTrait.color.set(color);

    }


    public void setColor(float r, float g, float b, float a) {
        internalGroup.setColor(r, g, b, a);
        dataTrait.color.set(r, g, b, a);
    }

    /**
     * Returns the color the actor will be tinted when drawn. The returned
     * instance can be modified to change the color.
     */
    public Color getColor() {
        return internalGroup.getColor();
    }

    /**
     * Retrieve custom actor name set with {@link Actor#setName(String)},
     * used for easier identification
     */
    public String getName() {
        return internalGroup.getName();
    }

    /**
     * Sets a name for easier identification of the actor in application code.
     *
     * @see Group#findActor(String)
     */
    public void setName(String name) {
        internalGroup.setName(name);
    }


    /**
     * Returns y plus height.
     */
    public float getTop() {
        return internalGroup.getTop();
    }

    /**
     * Returns x plus width.
     */
    public float getRight() {
        return internalGroup.getRight();
    }


    public void setHeight(float height) {
        internalGroup.setHeight(height);
        dataTrait.height = height;
        resetSprite();
    }

    /**
     * Sets the origin X and origin Y.
     */
    public void setOrigin(float originX, float originY) {
        internalGroup.setOrigin(originX, originY);
        dataTrait.originX = originX;
        dataTrait.originY = originY;
        resetSprite();
    }

    public float getScaleX() {
        return internalGroup.getScaleX();
    }

    public void setOriginX(float originX) {
        internalGroup.setOriginX(originX);
        dataTrait.originX = internalGroup.getOriginX();
    }

    public float getOriginY() {
        return internalGroup.getOriginY();
    }

    public void setOriginY(float originY) {
        internalGroup.setOriginY(originY);
        dataTrait.originY = internalGroup.getOriginY();
        resetSprite();
    }

    /**
     * Set position of UIComponent to x, y (using bottom left corner of UIComponent)
     */
    public void setPosition(float x, float y) {
        internalGroup.setPosition(x, y);
        dataTrait.x = x;
        dataTrait.y = y;
        resetSprite();
    }

    public void setRotation(float degrees) {
        internalGroup.setRotation(degrees);
        dataTrait.rotation = degrees;
        resetSprite();

    }


    /**
     * Sets the scale X and scale Y.
     */
    public void setScale(float scaleX, float scaleY) {
        internalGroup.setScale(scaleX, scaleY);
        dataTrait.scaleX = scaleX;
        dataTrait.scaleY = scaleY;
        resetSprite();
    }

    /**
     * Sets the scale for both X and Y
     */
    public void setScale(float scaleXY) {
        internalGroup.setScale(scaleXY);
        dataTrait.scaleX = scaleXY;
        dataTrait.scaleY = scaleXY;
        resetSprite();
    }

    public void setScaleX(float scaleX) {
        internalGroup.setScaleX(scaleX);
        dataTrait.scaleX = scaleX;
        resetSprite();
    }

    public float getScaleY() {
        return internalGroup.getScaleY();
    }

    public void setScaleY(float scaleY) {
        internalGroup.setScaleY(scaleY);
        dataTrait.scaleY = scaleY;
        resetSprite();
    }

    /**
     * Sets the width and height.
     */
    public void setSize(float width, float height) {
        internalGroup.setSize(width, height);
        dataTrait.width = width;
        dataTrait.height = height;
        resetSprite();
    }

    /**
     * If false, the actor will not be drawn and will not receive touch events.
     * Default is true.
     */
    public void setVisible(boolean visible) {
        internalGroup.setVisible(visible);
        dataTrait.visible = visible;
    }

    public float getHeight() {
        return internalGroup.getHeight();
    }

    public void setWidth(float width) {
        internalGroup.setWidth(width);
        dataTrait.width = width;
        resetSprite();
    }

    public void setX(float x) {
        internalGroup.setX(x);
        dataTrait.x = x;
        resetSprite();
    }

    public void setY(float y) {
        internalGroup.setY(y);
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
        internalGroup.setZIndex(index);
        dataTrait.zIndex = index;
    }

    /**
     * Returns the z-index of this actor.
     *
     * @see #setZIndex(int)
     */
    public int getZIndex() {
        return internalGroup.getZIndex();
    }

    public float getWidth() {
        return internalGroup.getWidth();
    }

    /**
     * Calls {@link #clipBegin(float, float, float, float)}
     * to clip this actor's bounds.
     */
    public boolean clipBegin() {
        return internalGroup.clipBegin();
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
        return internalGroup.clipBegin(x, y, width, height);
    }

    /**
     * Ends clipping begun by {@link #clipBegin(float, float, float, float)}.
     */
    public void clipEnd() {
        internalGroup.clipEnd();
    }

    /**
     * Transforms the specified point in screen coordinates to the actor's
     * local coordinate system.
     */
    public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
        return internalGroup.screenToLocalCoordinates(screenCoords);

    }


    /**
     * Transforms the specified point in the stage's coordinates to the
     * actor's local coordinate system.
     */
    public Vector2 stageToLocalCoordinates(Vector2 stageCoords) {
        return internalGroup.stageToLocalCoordinates(stageCoords);
    }

    /**
     * Transforms the specified point in the actor's coordinates to be
     * in the stage's coordinates.
     *
     * @see Stage#toScreenCoordinates(Vector2, com.guidebee.math.Matrix4)
     */
    public Vector2 localToStageCoordinates(Vector2 localCoords) {
        return internalGroup.localToStageCoordinates(localCoords);
    }

    /**
     * Transforms the specified point in the actor's coordinates to be
     * in the parent's coordinates.
     */
    public Vector2 localToParentCoordinates(Vector2 localCoords) {
        return internalGroup.localToParentCoordinates(localCoords);
    }

    /**
     * Converts coordinates for this actor to those of a parent actor. The
     * ascendant does not need to be a direct parent.
     */
    public Vector2 localToAscendantCoordinates(Actor ascendant, Vector2 localCoords) {
        if(ascendant instanceof Group){
            Group group=(Group)ascendant;
            return internalGroup.localToAscendantCoordinates(group.internalGroup, localCoords);

        }
        return internalGroup.localToAscendantCoordinates(ascendant.internalActor, localCoords);
    }

    /**
     * Converts the coordinates given in the parent's coordinate system to
     * this actor's coordinate system.
     */
    public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
        return internalGroup.parentToLocalCoordinates(parentCoords);
    }


    /**
     * If true, {@link #drawDebug(ShapeRenderer)} will be called for this actor.
     */
    public void setDebug(boolean enabled) {
        internalGroup.setDebug(enabled);
    }

    public boolean getDebug() {
        return internalGroup.getDebug();
    }

    /**
     * Calls {@link #setDebug(boolean)} with {@code true}.
     */
    public Actor debug() {
        UIComponent actor = internalGroup.debug();
        if (actor != null) {
            return (Actor) actor.getUserObject();
        }
        return null;
    }

    public String toString() {
        return internalGroup.toString();
    }


    /**
     * Adds the specified size to the current size.
     */
    public void sizeBy(float size) {
        internalGroup.sizeBy(size);
        dataTrait.width = internalGroup.getWidth();
        dataTrait.height = internalGroup.getHeight();
        resetSprite();
    }

    /**
     * Adds the specified size to the current size.
     */
    public void sizeBy(float width, float height) {
        internalGroup.sizeBy(width, height);
        dataTrait.width = internalGroup.getWidth();
        dataTrait.height = internalGroup.getHeight();
        resetSprite();
    }

    /**
     * Changes the z-order for this actor so it is in back of all siblings.
     */
    public void toBack() {
        internalGroup.toBack();
        dataTrait.zIndex = internalGroup.getZIndex();
    }

    /**
     * Changes the z-order for this actor so it is in front of all siblings.
     */
    public void toFront() {
        internalGroup.toFront();
        dataTrait.zIndex = internalGroup.getZIndex();
    }







}
