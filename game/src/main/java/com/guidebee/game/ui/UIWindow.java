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

import com.guidebee.game.GameEngine;
import com.guidebee.game.InputAdapter;
import com.guidebee.game.camera.Camera;
import com.guidebee.game.camera.OrthographicCamera;
import com.guidebee.game.camera.viewports.ScalingViewport;
import com.guidebee.game.camera.viewports.Viewport;
import com.guidebee.game.engine.graphics.opengles.IGL20;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Color;
import com.guidebee.game.graphics.ShapeRenderer;
import com.guidebee.game.graphics.SpriteBatch;
import com.guidebee.game.ui.InputEvent.Type;
import com.guidebee.game.ui.Table.Debug;
import com.guidebee.game.ui.actions.Action;
import com.guidebee.math.Matrix4;
import com.guidebee.math.Vector2;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.Disposable;
import com.guidebee.utils.Pool;
import com.guidebee.utils.Pools;
import com.guidebee.utils.Scaling;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.SnapshotArray;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * A 2D scene graph containing hierarchies of {@link UIComponent components}. Window handles
 * the viewport and distributes input events.
 * <p>
 * {@link #setViewport(Viewport)} controls the coordinates used within the window
 * and sets up the camera used to convert between
 * window coordinates and screen coordinates.
 * <p>
 * A window must receive input events so it can distribute them to components. This is
 * typically done by passing the window to
 * {@link com.guidebee.game.Input#setInputProcessor(com.guidebee.game.InputProcessor)
 * GameEngine.input.setInputProcessor}. An {@link com.guidebee.game.InputMultiplexer} may be
 * used to handle input events before or after the window does. If an component handles
 * an event by returning true from the input
 * method, then the window's input method will also return true, causing subsequent
 * InputProcessors to not receive the event.
 * <p>
 * The Window and its constituents (like Components and Listeners) are not thread-safe
 * and should only be updated and queried from a
 * single thread (presumably the main render thread). Methods should be reentrant,
 * so you can update Components and Windows from within
 * callbacks and handlers.
 *
 * @author mzechner
 * @author Nathan Sweet
 */
public class UIWindow extends InputAdapter implements Disposable {
    static private final Vector2 componentCoords = new Vector2();
    static boolean debug;

    private Viewport viewport;
    private final Batch batch;
    private boolean ownsBatch;
    private final UIContainer root;
    private final Vector2 windowCoords = new Vector2();
    private final UIComponent[] pointerOverComponents = new UIComponent[20];
    private final boolean[] pointerTouched = new boolean[20];
    private final int[] pointerScreenX = new int[20];
    private final int[] pointerScreenY = new int[20];
    private int mouseScreenX, mouseScreenY;
    private UIComponent mouseOverComponent;
    private UIComponent keyboardFocus, scrollFocus;
    private final SnapshotArray<TouchFocus>
            touchFocuses = new SnapshotArray(true, 4, TouchFocus.class);

    private ShapeRenderer debugShapes;
    private boolean debugInvisible, debugAll, debugUnderMouse, debugParentUnderMouse;
    private Debug debugTableUnderMouse = Debug.none;
    private final Color debugColor = new Color(0, 1, 0, 0.85f);
    private Object userObject;

    /**
     * Creates a window with a {@link com.guidebee.game.camera.viewports.ScalingViewport}
     * set to {@link com.guidebee.utils.Scaling#fill}. The window will use its own {@link Batch} which
     * will be disposed when the window is disposed.
     */
    public UIWindow() {
        this(new ScalingViewport(Scaling.stretch, GameEngine.graphics.getWidth(),
                        GameEngine.graphics.getHeight(), new OrthographicCamera()),
                new SpriteBatch());
        ownsBatch = true;
    }

    /**
     * Creates a window with the specified viewport. The window will use its own
     * {@link com.guidebee.game.graphics.Batch} which will be disposed when the window
     * is disposed.
     */
    public UIWindow(Viewport viewport) {
        this(viewport, new SpriteBatch());
        ownsBatch = true;
    }

    /**
     * Creates a window with the specified viewport and batch. This can be used to
     * avoid creating a new batch (which can be somewhat
     * slow) if multiple windows are used during an application's life time.
     *
     * @param batch Will not be disposed if {@link #dispose()} is called, handle
     *              disposal yourself.
     */
    public UIWindow(Viewport viewport, Batch batch) {
        if (viewport == null)
            throw new IllegalArgumentException("viewport cannot be null.");
        if (batch == null)
            throw new IllegalArgumentException("batch cannot be null.");
        this.viewport = viewport;
        this.batch = batch;

        root = new UIContainer();
        root.setWindow(this);

        viewport.update(GameEngine.graphics.getWidth(),
                GameEngine.graphics.getHeight(), true);
    }


    public void drawExtra(Batch batch){

    }

    public void resetCamera(){
        Camera camera = viewport.getCamera();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void draw() {

        Camera camera = viewport.getCamera();
        camera.update();
        if (!root.isVisible()) return;

        Batch batch = this.batch;
        if (batch != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            root.draw(batch, 1);
            drawExtra(batch);
            batch.end();
        }

        if (debug) drawDebug();
    }


    /**
     * Returns the first component found with the specified name. Note this
     * recursively compares the name of every component in the group.
     */
    public <T extends UIComponent> T findComponent(String name) {
        return root.findComponent(name);
    }

    private void drawDebug() {
        if (debugShapes == null) {
            debugShapes = new ShapeRenderer();
            debugShapes.setAutoShapeType(true);
        }

        if (debugUnderMouse || debugParentUnderMouse
                || debugTableUnderMouse != Debug.none) {
            screenToWindowCoordinates(windowCoords.set(GameEngine.input.getX(),
                    GameEngine.input.getY()));
            UIComponent component = hit(windowCoords.x, windowCoords.y, true);
            if (component == null) return;

            if (debugParentUnderMouse && component.parent != null) component = component.parent;

            if (debugTableUnderMouse == Debug.none)
                component.setDebug(true);
            else {
                while (component != null) {
                    if (component instanceof Table) break;
                    component = component.parent;
                }
                if (component == null) return;
                ((Table) component).debug(debugTableUnderMouse);
            }

            if (debugAll && component instanceof UIContainer) ((UIContainer) component).debugAll();

            disableDebug(root, component);
        } else {
            if (debugAll) root.debugAll();
        }

        GameEngine.gl.glEnable(IGL20.GL_BLEND);
        debugShapes.setProjectionMatrix(viewport.getCamera().combined);
        debugShapes.begin();
        root.drawDebug(debugShapes);
        debugShapes.end();
    }

    /**
     * Disables debug on all components recursively except the specified component and any children.
     */
    private void disableDebug(UIComponent component, UIComponent except) {
        if (component == except) return;
        component.setDebug(false);
        if (component instanceof UIContainer) {
            SnapshotArray<UIComponent> children = ((UIContainer) component).children;
            for (int i = 0, n = children.size; i < n; i++)
                disableDebug(children.get(i), except);
        }
    }

    /**
     * Calls {@link #act(float)} with {@link com.guidebee.game.Graphics#getDeltaTime()}.
     */
    public void act() {
        act(Math.min(GameEngine.graphics.getDeltaTime(), 1 / 30f));
    }

    /**
     * Calls the {@link UIComponent#act(float)} method on each component in the window.
     * Typically called each frame. This method also fires
     * enter and exit events.
     *
     * @param delta Time in seconds since the last frame.
     */
    public void act(float delta) {
        // Update over components. Done in act() because components may change position,
        // which can fire enter/exit without an input event.
        for (int pointer = 0, n = pointerOverComponents.length; pointer < n; pointer++) {
            UIComponent overLast = pointerOverComponents[pointer];
            // Check if pointer is gone.
            if (!pointerTouched[pointer]) {
                if (overLast != null) {
                    pointerOverComponents[pointer] = null;
                    screenToWindowCoordinates(windowCoords.set(pointerScreenX[pointer],
                            pointerScreenY[pointer]));
                    // Exit over last.
                    InputEvent event = Pools.obtain(InputEvent.class);
                    event.setType(InputEvent.Type.exit);
                    event.setWindow(this);
                    event.setWindowX(windowCoords.x);
                    event.setWindowY(windowCoords.y);
                    event.setRelatedComponent(overLast);
                    event.setPointer(pointer);
                    overLast.fire(event);
                    Pools.free(event);
                }
                continue;
            }
            // Update over component for the pointer.
            pointerOverComponents[pointer] = fireEnterAndExit(overLast, pointerScreenX[pointer],
                    pointerScreenY[pointer], pointer);
        }

        root.act(delta);
    }

    private UIComponent fireEnterAndExit(UIComponent overLast, int screenX, int screenY, int pointer) {
        // Find the component under the point.
        screenToWindowCoordinates(windowCoords.set(screenX, screenY));
        UIComponent over = hit(windowCoords.x, windowCoords.y, true);
        if (over == overLast) return overLast;

        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setWindowX(windowCoords.x);
        event.setWindowY(windowCoords.y);
        event.setPointer(pointer);
        // Exit overLast.
        if (overLast != null) {
            event.setType(InputEvent.Type.exit);
            event.setRelatedComponent(over);
            overLast.fire(event);
        }
        // Enter over.
        if (over != null) {
            event.setType(InputEvent.Type.enter);
            event.setRelatedComponent(overLast);
            over.fire(event);
        }
        Pools.free(event);
        return over;
    }

    /**
     * Applies a touch down event to the window and returns true if an
     * component in the scene {@link com.guidebee.game.ui.Event#handle() handled} the event.
     */
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < viewport.getScreenX()
                || screenX >= viewport.getScreenX() + viewport.getScreenWidth())
            return false;
        if (GameEngine.graphics.getHeight() - screenY < viewport.getScreenY()
                || GameEngine.graphics.getHeight() - screenY
                >= viewport.getScreenY() + viewport.getScreenHeight())
            return false;

        pointerTouched[pointer] = true;
        pointerScreenX[pointer] = screenX;
        pointerScreenY[pointer] = screenY;

        screenToWindowCoordinates(windowCoords.set(screenX, screenY));

        InputEvent event = Pools.obtain(InputEvent.class);
        event.setType(Type.touchDown);
        event.setWindow(this);
        event.setWindowX(windowCoords.x);
        event.setWindowY(windowCoords.y);
        event.setPointer(pointer);
        event.setButton(button);

        UIComponent target = hit(windowCoords.x, windowCoords.y, true);
        if (target == null) target = root;

        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a touch moved event to the window and returns true if an component
     * in the scene {@link com.guidebee.game.ui.Event#handle() handled} the event.
     * Only {@link com.guidebee.game.ui.InputListener listeners} that returned true for touchDown
     * will receive this event.
     */
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        pointerScreenX[pointer] = screenX;
        pointerScreenY[pointer] = screenY;
        mouseScreenX = screenX;
        mouseScreenY = screenY;

        if (touchFocuses.size == 0) return false;

        screenToWindowCoordinates(windowCoords.set(screenX, screenY));

        InputEvent event = Pools.obtain(InputEvent.class);
        event.setType(Type.touchDragged);
        event.setWindow(this);
        event.setWindowX(windowCoords.x);
        event.setWindowY(windowCoords.y);
        event.setPointer(pointer);

        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        TouchFocus[] focuses = touchFocuses.begin();
        for (int i = 0, n = touchFocuses.size; i < n; i++) {
            TouchFocus focus = focuses[i];
            if (focus.pointer != pointer) continue;
            event.setTarget(focus.target);
            event.setListenerComponent(focus.listenerComponent);
            if (focus.listener.handle(event)) event.handle();
        }
        touchFocuses.end();

        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a touch up event to the window and returns true if an component in
     * the scene {@link com.guidebee.game.ui.Event#handle() handled} the event.
     * Only {@link com.guidebee.game.ui.InputListener listeners} that returned true for touchDown
     * will receive this event.
     */
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        pointerTouched[pointer] = false;
        pointerScreenX[pointer] = screenX;
        pointerScreenY[pointer] = screenY;

        if (touchFocuses.size == 0) return false;

        screenToWindowCoordinates(windowCoords.set(screenX, screenY));

        InputEvent event = Pools.obtain(InputEvent.class);
        event.setType(Type.touchUp);
        event.setWindow(this);
        event.setWindowX(windowCoords.x);
        event.setWindowY(windowCoords.y);
        event.setPointer(pointer);
        event.setButton(button);

        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        TouchFocus[] focuses = touchFocuses.begin();
        for (int i = 0, n = touchFocuses.size; i < n; i++) {
            TouchFocus focus = focuses[i];
            if (focus.pointer != pointer || focus.button != button) continue;
            if (!touchFocuses.removeValue(focus, true)) continue; // Touch focus already gone.
            event.setTarget(focus.target);
            event.setListenerComponent(focus.listenerComponent);
            if (focus.listener.handle(event)) event.handle();
            Pools.free(focus);
        }
        touchFocuses.end();

        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a mouse moved event to the window and returns true if an component in
     * the scene {@link com.guidebee.game.ui.Event#handle() handled} the event.
     * This event only occurs on the desktop.
     */
    public boolean mouseMoved(int screenX, int screenY) {
        if (screenX < viewport.getScreenX()
                || screenX >= viewport.getScreenX() + viewport.getScreenWidth())
            return false;
        if (GameEngine.graphics.getHeight() - screenY < viewport.getScreenY()
                || GameEngine.graphics.getHeight() - screenY
                >= viewport.getScreenY() + viewport.getScreenHeight())
            return false;

        mouseScreenX = screenX;
        mouseScreenY = screenY;

        screenToWindowCoordinates(windowCoords.set(screenX, screenY));

        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setType(Type.mouseMoved);
        event.setWindowX(windowCoords.x);
        event.setWindowY(windowCoords.y);

        UIComponent target = hit(windowCoords.x, windowCoords.y, true);
        if (target == null) target = root;

        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a mouse scroll event to the window and returns true if an component
     * in the scene {@link com.guidebee.game.ui.Event#handle() handled} the
     * event. This event only occurs on the desktop.
     */
    public boolean scrolled(int amount) {
        UIComponent target = scrollFocus == null ? root : scrollFocus;

        screenToWindowCoordinates(windowCoords.set(mouseScreenX, mouseScreenY));

        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setType(InputEvent.Type.scrolled);
        event.setScrollAmount(amount);
        event.setWindowX(windowCoords.x);
        event.setWindowY(windowCoords.y);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a key down event to the component that has
     * {@link UIWindow#setKeyboardFocus(UIComponent) keyboard focus}, if any, and returns
     * true if the event was {@link com.guidebee.game.ui.Event#handle() handled}.
     */
    public boolean keyDown(int keyCode) {
        UIComponent target = keyboardFocus == null ? root : keyboardFocus;
        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setType(InputEvent.Type.keyDown);
        event.setKeyCode(keyCode);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a key up event to the component that has
     * {@link UIWindow#setKeyboardFocus(UIComponent) keyboard focus}, if any, and returns true
     * if the event was {@link com.guidebee.game.ui.Event#handle() handled}.
     */
    public boolean keyUp(int keyCode) {
        UIComponent target = keyboardFocus == null ? root : keyboardFocus;
        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setType(InputEvent.Type.keyUp);
        event.setKeyCode(keyCode);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Applies a key typed event to the component that has
     * {@link UIWindow#setKeyboardFocus(UIComponent) keyboard focus}, if any, and returns
     * true if the event was {@link com.guidebee.game.ui.Event#handle() handled}.
     */
    public boolean keyTyped(char character) {
        UIComponent target = keyboardFocus == null ? root : keyboardFocus;
        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setType(InputEvent.Type.keyTyped);
        event.setCharacter(character);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    /**
     * Adds the listener to be notified for all touchDragged and touchUp
     * events for the specified pointer and button. The component
     * will be used as the {@link com.guidebee.game.ui.Event#getListenerComponent() listener component}
     * and {@link com.guidebee.game.ui.Event#getTarget() target}.
     */
    public void addTouchFocus(EventListener listener, UIComponent listenerComponent,
                              UIComponent target, int pointer, int button) {
        TouchFocus focus = Pools.obtain(TouchFocus.class);
        focus.listenerComponent = listenerComponent;
        focus.target = target;
        focus.listener = listener;
        focus.pointer = pointer;
        focus.button = button;
        touchFocuses.add(focus);
    }

    /**
     * Removes the listener from being notified for all touchDragged and touchUp
     * events for the specified pointer and button. Note
     * the listener may never receive a touchUp event if this method is used.
     */
    public void removeTouchFocus(EventListener listener, UIComponent listenerComponent,
                                 UIComponent target, int pointer, int button) {
        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        for (int i = touchFocuses.size - 1; i >= 0; i--) {
            TouchFocus focus = touchFocuses.get(i);
            if (focus.listener == listener && focus.listenerComponent == listenerComponent
                    && focus.target == target
                    && focus.pointer == pointer && focus.button == button) {
                touchFocuses.removeIndex(i);
                Pools.free(focus);
            }
        }
    }

    /**
     * Sends a touchUp event to all listeners that are registered to receive
     * touchDragged and touchUp events and removes their
     * touch focus. This method removes all touch focus listeners, but sends a
     * touchUp event so that the state of the listeners
     * remains consistent (listeners typically expect to receive touchUp eventually).
     * The location of the touchUp is
     * {@link Integer#MIN_VALUE}. Listeners can use {@link InputEvent#isTouchFocusCancel()}
     * to ignore this event if needed.
     */
    public void cancelTouchFocus() {
        cancelTouchFocus(null, null);
    }

    /**
     * Cancels touch focus for all listeners except the specified listener.
     *
     * @see #cancelTouchFocus()
     */
    public void cancelTouchFocus(EventListener listener, UIComponent component) {
        InputEvent event = Pools.obtain(InputEvent.class);
        event.setWindow(this);
        event.setType(InputEvent.Type.touchUp);
        event.setWindowX(Integer.MIN_VALUE);
        event.setWindowY(Integer.MIN_VALUE);

        // Cancel all current touch focuses except for the specified listener,
        // allowing for concurrent modification, and never
        // cancel the same focus twice.
        SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
        TouchFocus[] items = touchFocuses.begin();
        for (int i = 0, n = touchFocuses.size; i < n; i++) {
            TouchFocus focus = items[i];
            if (focus.listener == listener && focus.listenerComponent == component)
                continue;
            if (!touchFocuses.removeValue(focus, true)) continue;
             // Touch focus already gone.
            event.setTarget(focus.target);
            event.setListenerComponent(focus.listenerComponent);
            event.setPointer(focus.pointer);
            event.setButton(focus.button);
            focus.listener.handle(event);
            // Cannot return TouchFocus to pool, as it may still be in use
            // (eg if cancelTouchFocus is called from touchDragged).
        }
        touchFocuses.end();

        Pools.free(event);
    }

    /**
     * Adds an component to the root of the window.
     *
     * @see UIContainer#addComponent(UIComponent)
     * @see UIComponent#remove()
     */
    public void addComponent(UIComponent component) {
        root.addComponent(component);
    }

    /**
     * Adds an action to the root of the window.
     *
     * @see UIContainer#addAction(com.guidebee.game.ui.actions.Action)
     */
    public void addAction(Action action) {
        root.addAction(action);
    }

    /**
     * Returns the root's child components.
     *
     * @see UIContainer#getChildren()
     */
    public Array<UIComponent> getComponents() {
        return root.children;
    }

    /**
     * Adds a listener to the root.
     *
     * @see UIComponent#addListener(EventListener)
     */
    public boolean addListener(EventListener listener) {
        return root.addListener(listener);
    }

    /**
     * Removes a listener from the root.
     *
     * @see UIComponent#removeListener(EventListener)
     */
    public boolean removeListener(EventListener listener) {
        return root.removeListener(listener);
    }

    /**
     * Adds a capture listener to the root.
     *
     * @see UIComponent#addCaptureListener(EventListener)
     */
    public boolean addCaptureListener(EventListener listener) {
        return root.addCaptureListener(listener);
    }

    /**
     * Removes a listener from the root.
     *
     * @see UIComponent#removeCaptureListener(EventListener)
     */
    public boolean removeCaptureListener(EventListener listener) {
        return root.removeCaptureListener(listener);
    }

    /**
     * Removes the root's children, actions, and listeners.
     */
    public void clear() {
        unfocusAll();
        root.clear();
    }

    /**
     * Removes the touch, keyboard, and scroll focused components.
     */
    public void unfocusAll() {
        scrollFocus = null;
        keyboardFocus = null;
        cancelTouchFocus();
    }

    /**
     * Removes the touch, keyboard, and scroll focus for the specified component and any descendants.
     */
    public void unfocus(UIComponent component) {
        if (scrollFocus != null && scrollFocus.isDescendantOf(component))
            scrollFocus = null;
        if (keyboardFocus != null && keyboardFocus.isDescendantOf(component))
            keyboardFocus = null;
    }

    /**
     * Sets the component that will receive key events.
     *
     * @param component May be null.
     */
    public void setKeyboardFocus(UIComponent component) {
        if (keyboardFocus == component) return;
        FocusListener.FocusEvent event = Pools.obtain(FocusListener.FocusEvent.class);
        event.setWindow(this);
        event.setType(FocusListener.FocusEvent.Type.keyboard);
        UIComponent oldKeyboardFocus = keyboardFocus;
        if (oldKeyboardFocus != null) {
            event.setFocused(false);
            event.setRelatedComponent(component);
            oldKeyboardFocus.fire(event);
        }
        if (!event.isCancelled()) {
            keyboardFocus = component;
            if (component != null) {
                event.setFocused(true);
                event.setRelatedComponent(oldKeyboardFocus);
                component.fire(event);
                if (event.isCancelled()) setKeyboardFocus(oldKeyboardFocus);
            }
        }
        Pools.free(event);
    }

    /**
     * Gets the component that will receive key events.
     *
     * @return May be null.
     */
    public UIComponent getKeyboardFocus() {
        return keyboardFocus;
    }

    /**
     * Sets the component that will receive scroll events.
     *
     * @param component May be null.
     */
    public void setScrollFocus(UIComponent component) {
        if (scrollFocus == component) return;
        FocusListener.FocusEvent event = Pools.obtain(FocusListener.FocusEvent.class);
        event.setWindow(this);
        event.setType(FocusListener.FocusEvent.Type.scroll);
        UIComponent oldScrollFocus = keyboardFocus;
        if (oldScrollFocus != null) {
            event.setFocused(false);
            event.setRelatedComponent(component);
            oldScrollFocus.fire(event);
        }
        if (!event.isCancelled()) {
            scrollFocus = component;
            if (component != null) {
                event.setFocused(true);
                event.setRelatedComponent(oldScrollFocus);
                component.fire(event);
                if (event.isCancelled()) setScrollFocus(oldScrollFocus);
            }
        }
        Pools.free(event);
    }

    /**
     * Gets the component that will receive scroll events.
     *
     * @return May be null.
     */
    public UIComponent getScrollFocus() {
        return scrollFocus;
    }

    public Batch getBatch() {
        return batch;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * The viewport's world width.
     */
    public float getWidth() {
        return viewport.getWorldWidth();
    }

    /**
     * The viewport's world height.
     */
    public float getHeight() {
        return viewport.getWorldHeight();
    }

    /**
     * The viewport's camera.
     */
    public Camera getCamera() {
        return viewport.getCamera();
    }

    /**
     * Returns the root group which holds all components in the window.
     */
    public UIContainer getRoot() {
        return root;
    }

    /**
     * Returns the {@link UIComponent} at the specified location in window coordinates.
     * Hit testing is performed in the order the components
     * were inserted into the window, last inserted components being tested first.
     * To get window coordinates from screen coordinates, use
     * {@link #screenToWindowCoordinates(Vector2)}.
     *
     * @param touchable If true, the hit detection will respect the
     * {@link UIComponent#setTouchable(com.guidebee.game.ui.Touchable) touchability}.
     * @return May be null if no component was hit.
     */
    public UIComponent hit(float windowX, float windowY, boolean touchable) {
        root.parentToLocalCoordinates(componentCoords.set(windowX, windowY));
        return root.hit(componentCoords.x, componentCoords.y, touchable);
    }

    /**
     * Transforms the screen coordinates to window coordinates.
     *
     * @param screenCoords IInput screen coordinates and output for
     *                     resulting window coordinates.
     */
    public Vector2 screenToWindowCoordinates(Vector2 screenCoords) {
        viewport.unproject(screenCoords);
        return screenCoords;
    }

    /**
     * Transforms the window coordinates to screen coordinates.
     *
     * @param windowCoords IInput window coordinates and output for
     *                    resulting screen coordinates.
     */
    public Vector2 windowToScreenCoordinates(Vector2 windowCoords) {
        viewport.project(windowCoords);
        windowCoords.y = viewport.getScreenHeight() - windowCoords.y;
        return windowCoords;
    }

    /**
     * Transforms the coordinates to screen coordinates. The coordinates can be
     * anywhere in the window since the transform matrix
     * describes how to convert them. The transform matrix is typically
     * obtained from {@link Batch#getTransformMatrix()} during
     * {@link UIComponent#draw(Batch, float)}.
     *
     * @see UIComponent#localToWindowCoordinates(Vector2)
     */
    public Vector2 toScreenCoordinates(Vector2 coords, Matrix4 transformMatrix) {
        return viewport.toScreenCoordinates(coords, transformMatrix);
    }

    /**
     * Calculates window scissor coordinates from local coordinates using the
     * batch's current transformation matrix.
     *
     * @see com.guidebee.game.scene.ScissorStack#calculateScissors(com.guidebee.game.camera.Camera, float, float, float, float,
     * Matrix4, Rectangle, Rectangle)
     */
    public void calculateScissors(Rectangle localRect, Rectangle scissorRect) {
        viewport.calculateScissors(batch.getTransformMatrix(), localRect, scissorRect);
        Matrix4 transformMatrix;
        if (debugShapes != null && debugShapes.isDrawing())
            transformMatrix = debugShapes.getTransformMatrix();
        else
            transformMatrix = batch.getTransformMatrix();
        viewport.calculateScissors(transformMatrix, localRect, scissorRect);
    }

    /**
     * The default color that can be used by components to draw debug lines.
     */
    public Color getDebugColor() {
        return debugColor;
    }

    /**
     * If true, debug lines are shown for components even when {@link UIComponent#isVisible()} is false.
     */
    public void setDebugInvisible(boolean debugInvisible) {
        this.debugInvisible = debugInvisible;
    }

    /**
     * If true, debug lines are shown for all components.
     */
    public void setDebugAll(boolean debugAll) {
        if (this.debugAll == debugAll) return;
        this.debugAll = debugAll;
        if (debugAll)
            debug = true;
        else
            root.setDebug(false, true);
    }

    /**
     * If true, debug is enabled only for the component under the mouse. Can be
     * combined with {@link #setDebugAll(boolean)}.
     */
    public void setDebugUnderMouse(boolean debugUnderMouse) {
        if (this.debugUnderMouse == debugUnderMouse) return;
        this.debugUnderMouse = debugUnderMouse;
        if (debugUnderMouse)
            debug = true;
        else
            root.setDebug(false, true);
    }

    /**
     * If true, debug is enabled only for the parent of the component under the
     * mouse. Can be combined with
     * {@link #setDebugAll(boolean)}.
     */
    public void setDebugParentUnderMouse(boolean debugParentUnderMouse) {
        if (this.debugParentUnderMouse == debugParentUnderMouse) return;
        this.debugParentUnderMouse = debugParentUnderMouse;
        if (debugParentUnderMouse)
            debug = true;
        else
            root.setDebug(false, true);
    }

    /**
     * If not {@link Debug#none}, debug is enabled only for the first ascendant
     * of the component under the mouse that is a table. Can
     * be combined with {@link #setDebugAll(boolean)}.
     *
     * @param debugTableUnderMouse May be null for {@link Debug#none}.
     */
    public void setDebugTableUnderMouse(Debug debugTableUnderMouse) {
        if (debugTableUnderMouse == null) debugTableUnderMouse = Debug.none;
        if (this.debugTableUnderMouse == debugTableUnderMouse) return;
        this.debugTableUnderMouse = debugTableUnderMouse;
        if (debugTableUnderMouse != Debug.none)
            debug = true;
        else
            root.setDebug(false, true);
    }

    /**
     * If true, debug is enabled only for the first ascendant of the component under
     * the mouse that is a table. Can be combined with
     * {@link #setDebugAll(boolean)}.
     */
    public void setDebugTableUnderMouse(boolean debugTableUnderMouse) {
        setDebugTableUnderMouse(debugTableUnderMouse ? Debug.all : Debug.none);
    }

    public void dispose() {
        clear();
        if (ownsBatch) batch.dispose();
    }

    /**
     * Internal class for managing touch focus. Public only for GWT.
     *
     * @author Nathan Sweet
     */
    public static final class TouchFocus implements Pool.Poolable {
        EventListener listener;
        UIComponent listenerComponent, target;
        int pointer, button;

        public void reset() {
            listenerComponent = null;
            listener = null;
        }
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
}
