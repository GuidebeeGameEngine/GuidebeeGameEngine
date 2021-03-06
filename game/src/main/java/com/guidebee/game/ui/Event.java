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

import com.guidebee.utils.Pool;

/**
 * The base class for all events.
 * <p/>
 * By default an event will "bubble" up through an component's parent's handlers
 * (see {@link #setBubbles(boolean)}).
 * <p/>
 * An component's capture listeners can {@link #stop()} an event to prevent child
 * components from seeing it.
 * <p/>
 * An Event may be marked as "handled" which will end its propagation outside of
 * the Window (see {@link #handle()}). The default
 * {@link UIComponent#fire(Event)} will mark events handled if an {@link EventListener}
 * returns true.
 * <p/>
 * A cancelled event will be stopped and handled. Additionally, many components will
 * undo the side-effects of a canceled event. (See
 * {@link #cancel()}.)
 *
 * @see InputEvent
 * @see UIComponent#fire(Event)
 */
public class Event implements Pool.Poolable {
    private UIWindow window;
    private UIComponent targetComponent;
    private UIComponent listenerComponent;
    private boolean capture; // true means event occurred during the
    // capture phase
    private boolean bubbles = true; // true means propagate to target's parents
    private boolean handled; // true means the event was handled
    // (the window will eat the input)
    private boolean stopped; // true means event propagation was stopped
    private boolean cancelled; // true means propagation was stopped and any
    // action that this event would cause should not happen

    /**
     * Marks this event as handled. This does not affect event propagation inside
     * scene2d, but causes the {@link UIWindow} event
     * methods to return false, which will eat the event so it is not passed on
     * to the application under the window.
     */
    public void handle() {
        handled = true;
    }

    /**
     * Marks this event cancelled. This {@link #handle() handles} the event
     * and {@link #stop() stops} the event propagation. It
     * also cancels any default action that would have been taken by the code
     * that fired the event. Eg, if the event is for a
     * checkbox being checked, cancelling the event could uncheck the checkbox.
     */
    public void cancel() {
        cancelled = true;
        stopped = true;
        handled = true;
    }

    /**
     * Marks this event has being stopped. This halts event propagation.
     * Any other listeners on the {@link #getListenerComponent()
     * listener component} are notified, but after that no other listeners are notified.
     */
    public void stop() {
        stopped = true;
    }

    public void reset() {
        window = null;
        targetComponent = null;
        listenerComponent = null;
        capture = false;
        bubbles = true;
        handled = false;
        stopped = false;
        cancelled = false;
    }

    /**
     * Returns the component that the event originated from.
     */
    public UIComponent getTarget() {
        return targetComponent;
    }

    public void setTarget(UIComponent targetComponent) {
        this.targetComponent = targetComponent;
    }

    /**
     * Returns the component that this listener is attached to.
     */
    public UIComponent getListenerComponent() {
        return listenerComponent;
    }

    public void setListenerComponent(UIComponent listenerComponent) {
        this.listenerComponent = listenerComponent;
    }

    public boolean getBubbles() {
        return bubbles;
    }

    /**
     * If true, after the event is fired on the target component, it will also
     * be fired on each of the parent components, all the way to
     * the root.
     */
    public void setBubbles(boolean bubbles) {
        this.bubbles = bubbles;
    }

    /**
     * {@link #handle()}
     */
    public boolean isHandled() {
        return handled;
    }

    /**
     * @see #stop()
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * @see #cancel()
     */
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    /**
     * If true, the event was fired during the capture phase.
     *
     * @see UIComponent#fire(Event)
     */
    public boolean isCapture() {
        return capture;
    }

    public void setWindow(UIWindow window) {
        this.window = window;
    }

    /**
     * The window for the component the event was fired on.
     */
    public UIWindow getWindow() {
        return window;
    }
}
