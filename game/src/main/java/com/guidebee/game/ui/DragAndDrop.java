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

import com.guidebee.math.Vector2;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.ObjectMap;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Manages drag and drop operations through registered drag sources and drop
 * targets.
 *
 * @author Nathan Sweet
 */
public class DragAndDrop {
    static final Vector2 tmpVector = new Vector2();

    Payload payload;
    UIComponent dragComponent;
    Target target;
    boolean isValidTarget;
    Array<Target> targets = new Array();
    ObjectMap<Source, DragListener> sourceListeners = new ObjectMap();
    private float tapSquareSize = 8;
    private int button;
    float dragComponentX = 14, dragComponentY = -20;
    float touchOffsetX, touchOffsetY;
    long dragStartTime;
    int dragTime = 250;
    int activePointer = -1;

    public void addSource(final Source source) {
        DragListener listener = new DragListener() {
            public void dragStart(InputEvent event, float x, float y,
                                  int pointer) {
                if (activePointer != -1) {
                    event.stop();
                    return;
                }

                activePointer = pointer;

                dragStartTime = System.currentTimeMillis();
                payload = source.dragStart(event, getTouchDownX(),
                        getTouchDownY(), pointer);
                event.stop();
            }

            public void drag(InputEvent event, float x, float y, int pointer) {
                if (payload == null) return;
                if (pointer != activePointer) return;

                UIWindow stage = event.getStage();

                Touchable dragComponentTouchable = null;
                if (dragComponent != null) {
                    dragComponentTouchable = dragComponent.getTouchable();
                    dragComponent.setTouchable(Touchable.disabled);
                }

                // Find target.
                Target newTarget = null;
                isValidTarget = false;
                float stageX = event.getStageX() + touchOffsetX,
                        stageY = event.getStageY() + touchOffsetY;
                UIComponent hit = event.getStage().hit(stageX, stageY, true);
                // Prefer touchable components.
                if (hit == null) hit = event.getStage().hit(stageX, stageY, false);
                if (hit != null) {
                    for (int i = 0, n = targets.size; i < n; i++) {
                        Target target = targets.get(i);
                        if (!target.component.isAscendantOf(hit)) continue;
                        newTarget = target;
                        target.component.stageToLocalCoordinates(tmpVector.set(stageX, stageY));
                        isValidTarget = target.drag(source, payload, tmpVector.x,
                                tmpVector.y, pointer);
                        break;
                    }
                }
                if (newTarget != target) {
                    if (target != null) target.reset(source, payload);
                    target = newTarget;
                }

                if (dragComponent != null) dragComponent.setTouchable(dragComponentTouchable);

                // Add/remove and position the drag component.
                UIComponent component = null;
                if (target != null) component = isValidTarget
                        ? payload.validDragComponent : payload.invalidDragComponent;
                if (component == null) component = payload.dragComponent;
                if (component == null) return;
                if (dragComponent != component) {
                    if (dragComponent != null) dragComponent.remove();
                    dragComponent = component;
                    stage.addComponent(component);
                }
                float componentX = event.getStageX() + dragComponentX;
                float componentY = event.getStageY() + dragComponentY - component.getHeight();
                if (componentX < 0) componentX = 0;
                if (componentY < 0) componentY = 0;
                if (componentX + component.getWidth() > stage.getWidth())
                    componentX = stage.getWidth() - component.getWidth();
                if (componentY + component.getHeight() > stage.getHeight())
                    componentY = stage.getHeight() - component.getHeight();
                component.setPosition(componentX, componentY);
            }

            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (pointer != activePointer) return;
                activePointer = -1;
                if (payload == null) return;

                if (System.currentTimeMillis() - dragStartTime < dragTime)
                    isValidTarget = false;
                if (dragComponent != null) dragComponent.remove();
                if (isValidTarget) {
                    float stageX = event.getStageX() + touchOffsetX,
                            stageY = event.getStageY() + touchOffsetY;
                    target.component.stageToLocalCoordinates(tmpVector.set(stageX, stageY));
                    target.drop(source, payload, tmpVector.x, tmpVector.y, pointer);
                }
                source.dragStop(event, x, y, pointer, payload, isValidTarget ? target : null);
                if (target != null) target.reset(source, payload);
                payload = null;
                target = null;
                isValidTarget = false;
                dragComponent = null;
            }
        };
        listener.setTapSquareSize(tapSquareSize);
        listener.setButton(button);
        source.component.addCaptureListener(listener);
        sourceListeners.put(source, listener);
    }

    public void removeSource(Source source) {
        DragListener dragListener = sourceListeners.remove(source);
        source.component.removeCaptureListener(dragListener);
    }

    public void addTarget(Target target) {
        targets.add(target);
    }

    public void removeTarget(Target target) {
        targets.removeValue(target, true);
    }

    /**
     * Removes all targets and sources.
     */
    public void clear() {
        targets.clear();
        for (ObjectMap.Entry<Source, DragListener> entry : sourceListeners.entries())
            entry.key.component.removeCaptureListener(entry.value);
        sourceListeners.clear();
    }

    /**
     * Sets the distance a touch must travel before being considered a drag.
     */
    public void setTapSquareSize(float halfTapSquareSize) {
        tapSquareSize = halfTapSquareSize;
    }

    /**
     * Sets the button to listen for, all other buttons are ignored. Default
     * is {@link com.guidebee.game.Input.Buttons#LEFT}. Use -1 for any button.
     */
    public void setButton(int button) {
        this.button = button;
    }

    public void setDragComponentPosition(float dragComponentX, float dragComponentY) {
        this.dragComponentX = dragComponentX;
        this.dragComponentY = dragComponentY;
    }

    /**
     * Sets an offset in stage coordinates from the touch position which
     * is used to determine the drop location. Default is 0,0.
     */
    public void setTouchOffset(float touchOffsetX, float touchOffsetY) {
        this.touchOffsetX = touchOffsetX;
        this.touchOffsetY = touchOffsetY;
    }

    public boolean isDragging() {
        return payload != null;
    }

    /**
     * Returns the current drag component, or null.
     */
    public UIComponent getDragComponent() {
        return dragComponent;
    }

    /**
     * Time in milliseconds that a drag must take before a drop will be
     * considered valid. This ignores an accidental drag and drop
     * that was meant to be a click. Default is 250.
     */
    public void setDragTime(int dragMillis) {
        this.dragTime = dragMillis;
    }

    /**
     * A target where a payload can be dragged from.
     *
     * @author Nathan Sweet
     */
    static abstract public class Source {
        final UIComponent component;

        public Source(UIComponent component) {
            if (component == null)
                throw new IllegalArgumentException("component cannot be null.");
            this.component = component;
        }

        /**
         * @return May be null.
         */
        abstract public Payload dragStart(InputEvent event, float x, float y, int pointer);

        /**
         * @param payload null if dragStart returned null.
         * @param target  null if not dropped on a valid target.
         */
        public void dragStop(InputEvent event, float x, float y, int pointer,
                             Payload payload, Target target) {
        }

        public UIComponent getComponent() {
            return component;
        }
    }

    /**
     * A target where a payload can be dropped to.
     *
     * @author Nathan Sweet
     */
    static abstract public class Target {
        final UIComponent component;

        public Target(UIComponent component) {
            if (component == null)
                throw new IllegalArgumentException("component cannot be null.");
            this.component = component;
            UIWindow stage = component.getStage();
            if (stage != null && component == stage.getRoot())
                throw new IllegalArgumentException(
                        "The stage root cannot be a drag and drop target.");
        }

        /**
         * Called when the object is dragged over the target. The coordinates
         * are in the target's local coordinate system.
         *
         * @return true if this is a valid target for the object.
         */
        abstract public boolean drag(Source source, Payload payload, float x,
                                     float y, int pointer);

        /**
         * Called when the object is no longer over the target, whether because
         * the touch was moved or a drop occurred.
         */
        public void reset(Source source, Payload payload) {
        }

        abstract public void drop(Source source, Payload payload, float x,
                                  float y, int pointer);

        public UIComponent getComponent() {
            return component;
        }
    }

    /**
     * The payload of a drag and drop operation. Components can be optionally provided
     * to follow the cursor and change when over a
     * target.
     */
    static public class Payload {
        UIComponent dragComponent, validDragComponent, invalidDragComponent;
        Object object;

        public void setDragComponent(UIComponent dragComponent) {
            this.dragComponent = dragComponent;
        }

        public UIComponent getDragComponent() {
            return dragComponent;
        }

        public void setValidDragComponent(UIComponent validDragComponent) {
            this.validDragComponent = validDragComponent;
        }

        public UIComponent getValidDragComponent() {
            return validDragComponent;
        }

        public void setInvalidDragComponent(UIComponent invalidDragComponent) {
            this.invalidDragComponent = invalidDragComponent;
        }

        public UIComponent getInvalidDragComponent() {
            return invalidDragComponent;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
