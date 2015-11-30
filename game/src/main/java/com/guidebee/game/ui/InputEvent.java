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

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Event for component input: touch, mouse, keyboard, and scroll.
 *
 * @see InputListener
 */
public class InputEvent extends Event {
    private Type type;
    private float windowX, windowY;
    private int pointer, button, keyCode, scrollAmount;
    private char character;
    private UIComponent relatedComponent;

    public void reset() {
        super.reset();
        relatedComponent = null;
        button = -1;
    }

    /**
     * The window x coordinate where the event occurred. Valid for: touchDown,
     * touchDragged, touchUp, mouseMoved, enter, and exit.
     */
    public float getWindowX() {
        return windowX;
    }

    public void setWindowX(float windowX) {
        this.windowX = windowX;
    }

    /**
     * The window x coordinate where the event occurred. Valid for: touchDown,
     * touchDragged, touchUp, mouseMoved, enter, and exit.
     */
    public float getWindowY() {
        return windowY;
    }

    public void setWindowY(float windowY) {
        this.windowY = windowY;
    }

    /**
     * The type of input event.
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * The pointer index for the event. The first touch is index 0,
     * second touch is index 1, etc. Always -1 on desktop. Valid for:
     * touchDown, touchDragged, touchUp, enter, and exit.
     */
    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    /**
     * The index for the mouse button pressed. Always 0 on Android.
     * Valid for: touchDown and touchUp.
     *
     * @see com.guidebee.game.Input.Buttons
     */
    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    /**
     * The key code of the key that was pressed. Valid for: keyDown and keyUp.
     */
    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * The character for the key that was type. Valid for: keyTyped.
     */
    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    /**
     * The amount the mouse was scrolled. Valid for: scrolled.
     */
    public int getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    /**
     * The component related to the event. Valid for: enter and exit. For enter,
     * this is the component being exited, or null. For exit,
     * this is the component being entered, or null.
     */
    public UIComponent getRelatedComponent() {
        return relatedComponent;
    }

    /**
     * @param relatedComponent May be null.
     */
    public void setRelatedComponent(UIComponent relatedComponent) {
        this.relatedComponent = relatedComponent;
    }

    /**
     * Sets componentCoords to this event's coordinates relative to the specified component.
     *
     * @param componentCoords Output for resulting coordinates.
     */
    public Vector2 toCoordinates(UIComponent component, Vector2 componentCoords) {
        componentCoords.set(windowX, windowY);
        component.windowToLocalCoordinates(componentCoords);
        return componentCoords;
    }

    /**
     * Returns true of this event is a touchUp triggered by {@link UIWindow#cancelTouchFocus()}.
     */
    public boolean isTouchFocusCancel() {
        return windowX == Integer.MIN_VALUE || windowY == Integer.MIN_VALUE;
    }

    public String toString() {
        return type.toString();
    }

    /**
     * Types of low-level input events supported by window2d.
     */
    static public enum Type {
        /**
         * A new touch for a pointer on the window was detected
         */
        touchDown,
        /**
         * A pointer has stopped touching the window.
         */
        touchUp,
        /**
         * A pointer that is touching the window has moved.
         */
        touchDragged,
        /**
         * The mouse pointer has moved (without a mouse button being active).
         */
        mouseMoved,
        /**
         * The mouse pointer or an active touch have entered (i.e.,
         * {@link UIComponent#hit(float, float, boolean) hit}) an component.
         */
        enter,
        /**
         * The mouse pointer or an active touch have exited an component.
         */
        exit,
        /**
         * The mouse scroll wheel has changed.
         */
        scrolled,
        /**
         * A keyboard key has been pressed.
         */
        keyDown,
        /**
         * A keyboard key has been released.
         */
        keyUp,
        /**
         * A keyboard key has been pressed and released.
         */
        keyTyped
    }
}
