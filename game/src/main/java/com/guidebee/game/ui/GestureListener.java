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

import com.guidebee.game.input.GestureDetector;
import com.guidebee.game.input.GestureDetector.GestureAdapter;
import com.guidebee.math.Vector2;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Detects tap, long press, fling, pan, zoom, and pinch gestures on an component. If
 * there is only a need to detect tap, use
 * {@link ClickListener}.
 *
 * @author Nathan Sweet
 * @see GestureDetector
 */
public class GestureListener implements EventListener {
    static final Vector2 tmpCoords = new Vector2();

    private final GestureDetector detector;
    InputEvent event;
    UIComponent component, touchDownTarget;

    /**
     * @see GestureDetector#GestureDetector
     * (com.guidebee.game.input.GestureDetector.GestureListener)
     */
    public GestureListener() {
        this(20, 0.4f, 1.1f, 0.15f);
    }

    /**
     * @see GestureDetector#GestureDetector(float, float, float, float,
     * com.guidebee.game.input.GestureDetector.GestureListener)
     */
    public GestureListener(float halfTapSquareSize, float tapCountInterval,
                           float longPressDuration, float maxFlingDelay) {
        detector = new GestureDetector(halfTapSquareSize, tapCountInterval,
                longPressDuration, maxFlingDelay, new GestureAdapter() {
            private final Vector2 initialPointer1 = new Vector2()
                    ,
                    initialPointer2 = new Vector2();
            private final Vector2 pointer1 = new Vector2()
                    ,
                    pointer2 = new Vector2();

            public boolean tap(float windowX, float windowY, int count, int button) {
                component.windowToLocalCoordinates(tmpCoords.set(windowX, windowY));
                GestureListener.this.tap(event, tmpCoords.x, tmpCoords.y, count, button);
                return true;
            }

            public boolean longPress(float windowX, float windowY) {
                component.windowToLocalCoordinates(tmpCoords.set(windowX, windowY));
                return GestureListener.this.longPress(component, tmpCoords.x, tmpCoords.y);
            }

            public boolean fling(float velocityX, float velocityY, int button) {
                GestureListener.this.fling(event, velocityX, velocityY, button);
                return true;
            }

            public boolean pan(float windowX, float windowY, float deltaX, float deltaY) {
                component.windowToLocalCoordinates(tmpCoords.set(windowX, windowY));
                GestureListener.this.pan(event, tmpCoords.x, tmpCoords.y, deltaX, deltaY);
                return true;
            }

            public boolean zoom(float initialDistance, float distance) {
                GestureListener.this.zoom(event, initialDistance, distance);
                return true;
            }

            public boolean pinch(Vector2 windowInitialPointer1, Vector2 windowInitialPointer2,
                                 Vector2 windowPointer1,
                                 Vector2 windowPointer2) {
                component.windowToLocalCoordinates(initialPointer1.set(windowInitialPointer1));
                component.windowToLocalCoordinates(initialPointer2.set(windowInitialPointer2));
                component.windowToLocalCoordinates(pointer1.set(windowPointer1));
                component.windowToLocalCoordinates(pointer2.set(windowPointer2));
                GestureListener.this.pinch(event, initialPointer1, initialPointer2,
                        pointer1, pointer2);
                return true;
            }
        });
    }

    public boolean handle(Event e) {
        if (!(e instanceof InputEvent)) return false;
        InputEvent event = (InputEvent) e;

        switch (event.getType()) {
            case touchDown:
                component = event.getListenerComponent();
                touchDownTarget = event.getTarget();
                detector.touchDown(event.getWindowX(), event.getWindowY(),
                        event.getPointer(), event.getButton());
                component.windowToLocalCoordinates(tmpCoords.set(event.getWindowX(),
                        event.getWindowY()));
                touchDown(event, tmpCoords.x, tmpCoords.y, event.getPointer(),
                        event.getButton());
                return true;
            case touchUp:
                if (event.isTouchFocusCancel()) return false;
                this.event = event;
                component = event.getListenerComponent();
                detector.touchUp(event.getWindowX(), event.getWindowY(), event.getPointer(),
                        event.getButton());
                component.windowToLocalCoordinates(tmpCoords.set(event.getWindowX(),
                        event.getWindowY()));
                touchUp(event, tmpCoords.x, tmpCoords.y, event.getPointer(),
                        event.getButton());
                return true;
            case touchDragged:
                this.event = event;
                component = event.getListenerComponent();
                detector.touchDragged(event.getWindowX(), event.getWindowY(),
                        event.getPointer());
                return true;
        }
        return false;
    }

    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void tap(InputEvent event, float x, float y, int count, int button) {
    }

    /**
     * If true is returned, additional gestures will not be triggered. No event is
     * provided because this event is triggered by time
     * passing, not by an InputEvent.
     */
    public boolean longPress(UIComponent component, float x, float y) {
        return false;
    }

    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
    }

    /**
     * The delta is the difference in window coordinates since the last pan.
     */
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
    }

    public void zoom(InputEvent event, float initialDistance, float distance) {
    }

    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2,
                      Vector2 pointer1, Vector2 pointer2) {
    }

    public GestureDetector getGestureDetector() {
        return detector;
    }

    public UIComponent getTouchDownTarget() {
        return touchDownTarget;
    }
}
