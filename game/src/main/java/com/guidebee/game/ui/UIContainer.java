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
package com.guidebee.game.ui;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.ShapeRenderer;
import com.guidebee.math.Matrix3;
import com.guidebee.math.Matrix4;
import com.guidebee.math.Vector2;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.SnapshotArray;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * 2D scene graph node that may contain other actors.
 * <p>
 * Actors have a z-order equal to the order they were inserted into the group.
 * Actors inserted later will be drawn on top of
 * actors added earlier. Touch events that hit more than one component are
 * distributed to topmost actors first.
 *
 * @author mzechner
 * @author Nathan Sweet
 */
public class UIContainer extends UIComponent implements Cullable {
    static private final Vector2 tmp = new Vector2();

    final SnapshotArray<UIComponent> children = new SnapshotArray(true, 4, UIComponent.class);
    private final Matrix3 localTransform = new Matrix3();
    private final Matrix3 worldTransform = new Matrix3();
    private final Matrix4 computedTransform = new Matrix4();
    private final Matrix4 oldTransform = new Matrix4();
    boolean transform = true;
    private Rectangle cullingArea;


    public UIContainer(){
        this(UIContainer.class.getName());
    }

    public UIContainer(String name){
        super(name);
    }

    public void act(float delta) {
        super.act(delta);
        UIComponent[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++)
            actors[i].act(delta);
        children.end();
    }

    /**
     * Draws the group and its children. The default implementation
     * calls {@link #applyTransform(com.guidebee.game.graphics.Batch, Matrix4)}
     * if needed, then
     * {@link #drawChildren(com.guidebee.game.graphics.Batch, float)},
     * then {@link #resetTransform(com.guidebee.game.graphics.Batch)} if needed.
     */
    public void draw(Batch batch, float parentAlpha) {
        if (transform) applyTransform(batch, computeTransform());
        drawChildren(batch, parentAlpha);
        if (transform) resetTransform(batch);
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
        parentAlpha *= this.color.a;
        SnapshotArray<UIComponent> children = this.children;
        UIComponent[] actors = children.begin();
        Rectangle cullingArea = this.cullingArea;
        if (cullingArea != null) {
            // Draw children only if inside culling area.
            float cullLeft = cullingArea.x;
            float cullRight = cullLeft + cullingArea.width;
            float cullBottom = cullingArea.y;
            float cullTop = cullBottom + cullingArea.height;
            if (transform) {
                for (int i = 0, n = children.size; i < n; i++) {
                    UIComponent child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.x, cy = child.y;
                    if (cx <= cullRight && cy <= cullTop && cx + child.width
                            >= cullLeft && cy + child.height >= cullBottom)
                        child.draw(batch, parentAlpha);
                }
                batch.flush();
            } else {
                // No transform for this group, offset each child.
                float offsetX = x, offsetY = y;
                x = 0;
                y = 0;
                for (int i = 0, n = children.size; i < n; i++) {
                    UIComponent child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.x, cy = child.y;
                    if (cx <= cullRight && cy <= cullTop && cx + child.width
                            >= cullLeft && cy + child.height >= cullBottom) {
                        child.x = cx + offsetX;
                        child.y = cy + offsetY;
                        child.draw(batch, parentAlpha);
                        child.x = cx;
                        child.y = cy;
                    }
                }
                x = offsetX;
                y = offsetY;
            }
        } else {
            // No culling, draw all children.
            if (transform) {
                for (int i = 0, n = children.size; i < n; i++) {
                    UIComponent child = actors[i];
                    if (!child.isVisible()) continue;
                    child.draw(batch, parentAlpha);
                }
                batch.flush();
            } else {
                // No transform for this group, offset each child.
                float offsetX = x, offsetY = y;
                x = 0;
                y = 0;
                for (int i = 0, n = children.size; i < n; i++) {
                    UIComponent child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.x, cy = child.y;
                    child.x = cx + offsetX;
                    child.y = cy + offsetY;
                    child.draw(batch, parentAlpha);
                    child.x = cx;
                    child.y = cy;
                }
                x = offsetX;
                y = offsetY;
            }
        }
        children.end();
    }

    /**
     * Draws this component's debug lines if {@link #getDebug()} is true and,
     * regardless of {@link #getDebug()}, calls
     * {@link UIComponent#drawDebug(ShapeRenderer)}
     * on each child.
     */
    public void drawDebug(ShapeRenderer shapes) {
        drawDebugBounds(shapes);
        if (transform) applyTransform(shapes, computeTransform());
        drawDebugChildren(shapes);
        if (transform) resetTransform(shapes);
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
        SnapshotArray<UIComponent> children = this.children;
        UIComponent[] actors = children.begin();
        // No culling, draw all children.
        if (transform) {
            for (int i = 0, n = children.size; i < n; i++) {
                UIComponent child = actors[i];
                if (!child.isVisible()) continue;
                child.drawDebug(shapes);
            }
            shapes.flush();
        } else {
            // No transform for this group, offset each child.
            float offsetX = x, offsetY = y;
            x = 0;
            y = 0;
            for (int i = 0, n = children.size; i < n; i++) {
                UIComponent child = actors[i];
                if (!child.isVisible()) continue;
                float cx = child.x, cy = child.y;
                child.x = cx + offsetX;
                child.y = cy + offsetY;
                child.drawDebug(shapes);
                child.x = cx;
                child.y = cy;
            }
            x = offsetX;
            y = offsetY;
        }
        children.end();
    }

    /**
     * Returns the transform for this group's coordinate system.
     */
    protected Matrix4 computeTransform() {
        Matrix3 worldTransform = this.worldTransform;
        Matrix3 localTransform = this.localTransform;

        float originX = this.originX;
        float originY = this.originY;
        float rotation = this.rotation;
        float scaleX = this.scaleX;
        float scaleY = this.scaleY;

        if (originX != 0 || originY != 0)
            localTransform.setToTranslation(originX, originY);
        else
            localTransform.idt();
        if (rotation != 0) localTransform.rotate(rotation);
        if (scaleX != 1 || scaleY != 1) localTransform.scale(scaleX, scaleY);
        if (originX != 0 || originY != 0) localTransform.translate(-originX, -originY);
        localTransform.trn(x, y);

        // Find the first parent that transforms.
        UIContainer parentUIContainer = parent;
        while (parentUIContainer != null) {
            if (parentUIContainer.transform) break;
            parentUIContainer = parentUIContainer.parent;
        }

        if (parentUIContainer != null) {
            worldTransform.set(parentUIContainer.worldTransform);
            worldTransform.mul(localTransform);
        } else {
            worldTransform.set(localTransform);
        }

        computedTransform.set(worldTransform);
        return computedTransform;
    }

    /**
     * Set the batch's transformation matrix, often with the result of
     * {@link #computeTransform()}. Note this causes the batch to
     * be flushed. {@link #resetTransform(Batch)} will restore the transform
     * to what it was before this call.
     */
    protected void applyTransform(Batch batch, Matrix4 transform) {
        oldTransform.set(batch.getTransformMatrix());
        batch.setTransformMatrix(transform);
    }

    /**
     * Restores the batch transform to what it was before
     * {@link #applyTransform(Batch, Matrix4)}. Note this causes the batch to be
     * flushed.
     */
    protected void resetTransform(Batch batch) {
        batch.setTransformMatrix(oldTransform);
    }

    /**
     * Set the shape renderer transformation matrix, often with the result
     * of {@link #computeTransform()}. Note this causes the
     * shape renderer to be flushed. {@link #resetTransform(ShapeRenderer)}
     * will restore the transform to what it was before this
     * call.
     */
    protected void applyTransform(ShapeRenderer shapes, Matrix4 transform) {
        oldTransform.set(shapes.getTransformMatrix());
        shapes.setTransformMatrix(transform);
    }

    /**
     * Restores the shape renderer transform to what it was before
     * {@link #applyTransform(Batch, Matrix4)}. Note this causes the
     * shape renderer to be flushed.
     */
    protected void resetTransform(ShapeRenderer shapes) {
        shapes.setTransformMatrix(oldTransform);
    }

    /**
     * Children completely outside of this rectangle will not be drawn.
     * This is only valid for use with unrotated and unscaled
     * actors!
     */
    public void setCullingArea(Rectangle cullingArea) {
        this.cullingArea = cullingArea;
    }

    public UIComponent hit(float x, float y, boolean touchable) {
        if (touchable && getTouchable() == Touchable.disabled) return null;
        Vector2 point = tmp;
        UIComponent[] childrenArray = children.items;
        for (int i = children.size - 1; i >= 0; i--) {
            UIComponent child = childrenArray[i];
            if (!child.isVisible()) continue;
            child.parentToLocalCoordinates(point.set(x, y));
            UIComponent hit = child.hit(point.x, point.y, touchable);
            if (hit != null) return hit;
        }
        return super.hit(x, y, touchable);
    }

    /**
     * Called when actors are added to or removed from the group.
     */
    protected void childrenChanged() {
    }

    /**
     * Adds an component as a child of this group. The component is first
     * removed from its parent group, if any.
     *
     * @see #remove()
     */
    public void addComponent(UIComponent component) {
        component.remove();
        children.add(component);
        component.setParent(this);
        component.setWindow(getStage());
        childrenChanged();
    }

    /**
     * Adds an component as a child of this group, at a specific index.
     * The component is first removed from its parent group, if any.
     *
     * @param index May be greater than the number of children.
     */
    public void addComponentAt(int index, UIComponent component) {
        component.remove();
        if (index >= children.size)
            children.add(component);
        else
            children.insert(index, component);
        component.setParent(this);
        component.setWindow(getStage());
        childrenChanged();
    }

    /**
     * Adds an component as a child of this group, immediately before
     * another child component. The component is first removed from its parent
     * group, if any.
     */
    public void addComponentBefore(UIComponent actorBefore, UIComponent component) {
        component.remove();
        int index = children.indexOf(actorBefore, true);
        children.insert(index, component);
        component.setParent(this);
        component.setWindow(getStage());
        childrenChanged();
    }

    /**
     * Adds an component as a child of this group, immediately after another
     * child component. The component is first removed from its parent
     * group, if any.
     */
    public void addComponentAfter(UIComponent actorAfter, UIComponent component) {
        component.remove();
        int index = children.indexOf(actorAfter, true);
        if (index == children.size)
            children.add(component);
        else
            children.insert(index + 1, component);
        component.setParent(this);
        component.setWindow(getStage());
        childrenChanged();
    }

    /**
     * Removes an component from this group. If the component will not be used
     * again and has actions, they should be
     * {@link UIComponent#clearActions() cleared} so the actions will be returned
     * to their
     * {@link com.guidebee.game.ui.actions.Action#setPool(com.guidebee.utils.Pool) pool},
     * if any. This is not done automatically.
     */
    public boolean removeComponent(UIComponent component) {
        if (!children.removeValue(component, true)) return false;
        UIWindow stage = getStage();
        if (stage != null) stage.unfocus(component);
        component.setParent(null);
        component.setWindow(null);
        childrenChanged();
        return true;
    }

    /**
     * Removes all actors from this group.
     */
    public void clearChildren() {
        UIComponent[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            UIComponent child = actors[i];
            child.setWindow(null);
            child.setParent(null);
        }
        children.end();
        children.clear();
        childrenChanged();
    }

    /**
     * Removes all children, actions, and listeners from this group.
     */
    public void clear() {
        super.clear();
        clearChildren();
    }

    /**
     * Returns the first component found with the specified name. Note this
     * recursively compares the name of every component in the group.
     */
    public <T extends UIComponent> T findComponent(String name) {
        Array<UIComponent> children = this.children;
        for (int i = 0, n = children.size; i < n; i++)
            if (name.equals(children.get(i).getName())) return (T) children.get(i);
        for (int i = 0, n = children.size; i < n; i++) {
            UIComponent child = children.get(i);
            if (child instanceof UIContainer) {
                UIComponent component = ((UIContainer) child).findComponent(name);
                if (component != null) return (T) component;
            }
        }
        return null;
    }

    protected void setWindow(UIWindow stage) {
        super.setWindow(stage);
        UIComponent[] childrenArray = children.items;
        for (int i = 0, n = children.size; i < n; i++)
            childrenArray[i].setWindow(stage);
    }

    /**
     * Swaps two actors by index. Returns false if the swap did not occur
     * because the indexes were out of bounds.
     */
    public boolean swapComponent(int first, int second) {
        int maxIndex = children.size;
        if (first < 0 || first >= maxIndex) return false;
        if (second < 0 || second >= maxIndex) return false;
        children.swap(first, second);
        return true;
    }

    /**
     * Swaps two actors. Returns false if the swap did not occur because
     * the actors are not children of this group.
     */
    public boolean swapComponent(UIComponent first, UIComponent second) {
        int firstIndex = children.indexOf(first, true);
        int secondIndex = children.indexOf(second, true);
        if (firstIndex == -1 || secondIndex == -1) return false;
        children.swap(firstIndex, secondIndex);
        return true;
    }

    /**
     * Returns an ordered list of child actors in this group.
     */
    public SnapshotArray<UIComponent> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return children.size > 0;
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
        this.transform = transform;
    }

    public boolean isTransform() {
        return transform;
    }

    /**
     * Converts coordinates for this group to those of a descendant component.
     * The descendant does not need to be a direct child.
     *
     * @throws IllegalArgumentException if the specified component is not a
     * descendant of this group.
     */
    public Vector2 localToDescendantCoordinates(UIComponent descendant, Vector2 localCoords) {
        UIContainer parent = descendant.parent;
        if (parent == null)
            throw new IllegalArgumentException("Child is not a descendant: " + descendant);
        // First convert to the component's parent coordinates.
        if (parent != this) localToDescendantCoordinates(parent, localCoords);
        // Then from each parent down to the descendant.
        descendant.parentToLocalCoordinates(localCoords);
        return localCoords;
    }

    /**
     * If true, {@link #drawDebug(ShapeRenderer)} will be called for this group and,
     * optionally, all children recursively.
     */
    public void setDebug(boolean enabled, boolean recursively) {
        setDebug(enabled);
        if (recursively) {
            for (UIComponent child : children) {
                if (child instanceof UIContainer) {
                    ((UIContainer) child).setDebug(enabled, recursively);
                } else {
                    child.setDebug(enabled);
                }
            }
        }
    }

    /**
     * Calls {@link #setDebug(boolean, boolean)} with {@code true, true}.
     */
    public UIContainer debugAll() {
        setDebug(true, true);
        return this;
    }

    /**
     * Prints the component hierarchy recursively for debugging purposes.
     */
    public void print() {
        print("");
    }

    private void print(String indent) {
        UIComponent[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            System.out.println(indent + actors[i]);
            if (actors[i] instanceof UIContainer) ((UIContainer) actors[i]).print(indent + "|  ");
        }
        children.end();
    }
}
